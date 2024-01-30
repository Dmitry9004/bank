package transfer.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import account.Sum;
import account.core.EStatus;
import card.core.CardService;
import card.model.Card;
import transfer.core.PayService;
import user.core.UserDetailsImpl;
import user.dto.UserDTO;

@RestController
@RequestMapping("/pay")
public class TransferController {
	
	private final Logger logger = LoggerFactory.getLogger(TransferController.class);
	
	private PayService payService;
	private CardService cardService;
	
	public TransferController(PayService service, CardService cardService){
		this.payService = service;
		this.cardService = cardService;
	}
	
	@PostMapping("/{cardNumberFrom}/{cardNumberTo}")
	public void pay(@AuthenticationPrincipal UserDetailsImpl principal,
		@PathVariable Integer cardNumberFrom, @PathVariable Integer cardNumberTo, @RequestBody Sum total)
				throws ResponseStatusException{
		if(cardNumberFrom <= 0 || cardNumberTo <= 0 || total.getSum() <= 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Optional<Card> cardFrom = cardService.get(cardNumberFrom);
		Optional<Card> cardTo  = cardService.get(cardNumberTo);
		
		if(cardFrom.isEmpty() || cardTo.isEmpty()
		   || cardFrom.get().getUserId() != principal.getUserId() 
		   || cardFrom.get().getTotal() - total.getSum() < 0
		   || cardFrom.get().getStatus() != EStatus.OPENED
		   || cardTo.get().getStatus() != EStatus.OPENED)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		payService.pay(cardFrom.get(), cardTo.get(), total.getSum());
		logger.info("TransferController: pay, cardNumberFrom: "+ cardNumberFrom + " cardNumberTo: " + cardNumberTo + " total: " + total.getSum());
	}
}
