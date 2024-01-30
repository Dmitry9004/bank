package card.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import account.core.EStatus;
import card.core.CardService;
import card.core.ECard;
import card.dto.CardDTO;
import card.model.Card;
import user.core.UserDetailsImpl;
import user.core.UserService;
import user.model.User;

@RestController
@RequestMapping("/card")
public class CardController {
	
	private final Logger logger = LoggerFactory.getLogger(CardController.class);
	
	private CardService cardService;
	private ModelMapper mapper;
	private UserService userService;
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public CardController(CardService service, ModelMapper mapper, UserService userSerivce,
			KafkaTemplate<String, String> kafkaTemplate) { 
		this.cardService = service; 
		this.mapper = mapper;
		this.userService = userSerivce;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@GetMapping
	public List<CardDTO> getAllByUserId(@AuthenticationPrincipal UserDetailsImpl principal){
		List<Card> cardList = cardService.getAllByUserId(principal.getUserId());
		
		return cardList.stream()
				.filter(acc -> acc.getStatus() != EStatus.CLOSED)
				.map(card -> {
					CardDTO dto = mapper.map(card, CardDTO.class);
					dto.setValidityInDaysPeriod(card.getTypeCard().getPeriodOfType().getDays());
					return dto;
				})
				.toList();
	}
	
	@GetMapping("/phone/{phone}")
	public ResponseEntity<CardDTO> getByPhone(@PathVariable String phone) 
			throws ResponseStatusException{
		if(phone == "" || phone.length() != 11)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		return get(cardService.getByPhone(phone));
	}
	
	@GetMapping("/{code}")
	public ResponseEntity<CardDTO> getByCode(@PathVariable Integer code) 
			throws ResponseStatusException{
		if(code <= 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		return get(cardService.get(code));
	}
	
	private ResponseEntity<CardDTO> get(Optional<Card> card){
		if(card.isEmpty() || card.get().getStatus() != EStatus.OPENED)
			return new ResponseEntity<>(null, HttpStatus.OK);
		
		CardDTO cardDTO = mapper.map(card.get(), CardDTO.class);
		cardDTO.setValidityInDaysPeriod(card.get().getTypeCard().getPeriodOfType().getDays());
		return new ResponseEntity<>(cardDTO, HttpStatus.OK);
	}
	
	@PostMapping
	public void openCard(@AuthenticationPrincipal UserDetailsImpl principal, @RequestParam("type") ECard type)
		throws ResponseStatusException{
		if(type == null) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	
		User user = userService.get(principal.getUserId()).get();
		Card card = new Card();
			card.setUserId(principal.getUserId());
			card.setPhone(user.getPhoneNumber());
			card.setStatus(EStatus.OPENED);
			card.setTypeCard(type);
			
		try {
			cardService.save(card);
			
			kafkaTemplate.send("card", "created", "" + card.getCardNumber());
				
			logger.info("CardController: createCard type: " + type);
		}catch(Exception e){
			logger.error("CardCotnroller: createCard type:" + type + "exception message: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("{cardNumber}")
	public void closeCard(@AuthenticationPrincipal UserDetailsImpl principal, @PathVariable Integer cardNumber) 
			throws ResponseStatusException{
		if(cardNumber <= 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Card card = cardService.get(cardNumber).get();
		
		if(principal.getUserId() != card.getUserId() || card.getStatus() == EStatus.CLOSED) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		cardService.delete(card);
		
		logger.info("CardController: closeCard cardNumber" + cardNumber + " type: " + card.getTypeCard());
	}
}
