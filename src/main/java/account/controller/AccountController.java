package account.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import account.Sum;
import account.core.AccountService;
import account.core.EAccount;
import account.core.EStatus;
import account.dto.AccountDTO;
import account.model.Account;
import card.core.CardService;
import card.model.Card;
import user.core.UserDetailsImpl;
import user.core.UserService;
import user.model.User;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	private final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private AccountService accountService;
	private UserService userService;
	private CardService cardService;
	private ModelMapper mapper;
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public AccountController(AccountService accountService, UserService userService,
			ModelMapper mapper, KafkaTemplate<String, String> kafkaTemplate, CardService cardService) {
		this.accountService = accountService;
		this.mapper = mapper;
		this.kafkaTemplate = kafkaTemplate;
		this.userService = userService;
		this.cardService = cardService;
	}
	
	@GetMapping
	public List<AccountDTO> getAllAccount(@AuthenticationPrincipal UserDetailsImpl principal){
		List<Account> account = accountService.getAllByUserId(principal.getUserId());
		
		return account.stream()
					.filter( acc -> acc.getStatus() != EStatus.CLOSED)
					.map(acc -> {
						AccountDTO dto = mapper.map(acc,AccountDTO.class);
						dto.setCanTakerOrAdd(dto.getTypeAccount().canTakeOrAdd());
						dto.setRate(dto.getTypeAccount().getRate());
						return dto;
					})
					.toList();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AccountDTO> getAccount(@AuthenticationPrincipal UserDetailsImpl principal, @PathVariable Integer id)
			throws ResponseStatusException{
		if(id <= 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);		
		
		Optional<Account> account = accountService.get(id);
		
		if(account.isEmpty() || account.get().getUserId() != principal.getUserId() 
				|| account.get().getStatus() == EStatus.CLOSED) 
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		
		AccountDTO accDTO = mapper.map(account.get(), AccountDTO.class);
		accDTO.setCanTakerOrAdd(accDTO.getTypeAccount().canTakeOrAdd());
		accDTO.setRate(accDTO.getTypeAccount().getRate());
		
		return new ResponseEntity<>(accDTO, HttpStatus.OK);
	}
	
	@PostMapping("/card/{codeCard}")
	public void createAccount(@AuthenticationPrincipal UserDetailsImpl principal, @RequestParam("type") EAccount type,
			@RequestBody Sum sum, @PathVariable Integer codeCard) 
			throws ResponseStatusException{
		if(sum.getSum() < 0 || codeCard <= 0 || sum.getSum() < type.getMinLoan())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Optional<Card> card = cardService.get(codeCard);
		
		if(card.isEmpty() || card.get().getTotal() < sum.getSum())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		User user = userService.get(principal.getUserId()).get();
		Account account = new Account();
				account.setUserId(user.getId());
				account.setTotal(sum.getSum());
				account.setTypeAccount(type);
				account.setDateUpdate(LocalDate.now());	
				account.setStatus(EStatus.OPENED);
		
		Card cardExist = card.get();
		cardExist.setTotal(cardExist.getTotal() - sum.getSum());

		try {
			accountService.save(account);
			cardService.save(cardExist);
		
			kafkaTemplate.send("account", "created", "" + account.getBankIdentificationCode());
			
			logger.info("AccountController: createAccount, type: " + type);
		}catch(Exception e){
			logger.error("AccountCotnroller: createAccount, type:" + type + "exception message: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping("/{code}/card/{codeCard}")
	public void closeAccount(@AuthenticationPrincipal UserDetailsImpl principal, 
			@PathVariable Integer code, @PathVariable Integer codeCard) 
		throws ResponseStatusException{
		if(code <= 0 || codeCard <= 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Optional<Account> account = accountService.get(code);
		Optional<Card> card = cardService.get(codeCard);
		
		if(account.isEmpty() || card.isEmpty() || account.get().getUserId() != principal.getUserId() 
				|| account.get().getStatus() == EStatus.CLOSED)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		Card cardExist = card.get();
		cardExist.setTotal(cardExist.getTotal() + account.get().getTotal());
		
		accountService.delete(account.get());
		cardService.save(cardExist);
		
		logger.info("AccountController: closeAccount, code:" + code + " typeAccount: " + account.get().getTypeAccount());
	}
	
	@PostMapping("/add/{codeAcc}/card/{codeCard}")
	public void add(@AuthenticationPrincipal UserDetailsImpl principal, 
			@PathVariable Integer codeAcc, @PathVariable Integer codeCard, @RequestBody Sum sum) 
		throws ResponseStatusException{
		if(codeAcc <= 0 || codeCard <= 0|| sum.getSum() <= 0)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Optional<Account> account = accountService.get(codeAcc);
		Optional<Card> card = cardService.get(codeCard);
		
		if(account.isEmpty() || card.isEmpty() || account.get().getUserId() != principal.getUserId()
		   || !account.get().getTypeAccount().canTakeOrAdd() || card.get().getTotal() < sum.getSum()
		   || account.get().getStatus() == EStatus.CLOSED)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Account accountExist = account.get();
		accountExist.setTotal(accountExist.getTotal() + sum.getSum());
		accountService.save(accountExist);
		
		Card cardExist = card.get();
		cardExist.setTotal(cardExist.getTotal() - sum.getSum());
		cardService.save(cardExist);
		
		logger.info("AccountController: add, code: "+ codeAcc + " sum: " + sum.getSum());
	}
	
	@PostMapping("subtract/{codeAcc}/card/{codeCard}")
	public void subtract(@AuthenticationPrincipal UserDetailsImpl principal, 
			@PathVariable Integer codeAcc, @PathVariable Integer codeCard, @RequestBody Sum sum) 
		throws ResponseStatusException{
		if(codeAcc <= 0 || codeCard <= 0 || sum.getSum() <= 0)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Optional<Account> account = accountService.get(codeAcc);
		Optional<Card> card = cardService.get(codeCard);
		
		if(account.isEmpty() || card.isEmpty() || account.get().getUserId() != principal.getUserId() 
				|| !account.get().getTypeAccount().canTakeOrAdd() 
				|| account.get().getStatus() == EStatus.CLOSED)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		if(account.get().getTotal() + sum.getSum() < account.get().getTypeAccount().getMinLoan())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Account accountExist = account.get();
		accountExist.setTotal(accountExist.getTotal() - sum.getSum());
		accountService.save(accountExist);
		
		Card cardExist = card.get();
		cardExist.setTotal(cardExist.getTotal() + sum.getSum());
		cardService.save(cardExist);
		
		logger.info("AccountController: subtract, code: "+ codeAcc + " sum: " + sum.getSum());
	}
}
