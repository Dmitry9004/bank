package transfer.core;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import account.core.AccountService;
import account.model.Account;
import account.repository.AccountRepository;
import card.core.CardService;
import card.model.Card;


@Transactional
@Component
public class PayService {
	
	private CardService service;

	public PayService(CardService service) {
		this.service = service;
	}
	
	public boolean pay(Card cardFrom, Card cardTo, double total){
		if(cardFrom.getTotal() - total < 0)
			return false;
		
		cardFrom.setTotal(cardFrom.getTotal() - total);
		cardTo.setTotal(cardTo.getTotal() + total);
		
		service.save(cardFrom);
		service.save(cardTo);
		
		return true;
	}
	
}