package card.core;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import account.core.AutoUpdateAccountService;
import account.core.EStatus;
import account.model.Account;
import card.model.Card;
import card.repository.CardRepository;

@Service
public class AutoUpdateCardService {
	
	private CardRepository cardRepository;
	private final Logger logger = LoggerFactory.getLogger(AutoUpdateCardService.class);
	
	public AutoUpdateCardService(CardRepository cardRepository){
		this.cardRepository = cardRepository;
	}
	
	@Scheduled(cron = "0 0 0 * * 0") //Every week
	public void deleteCard() {
		List<Card> cards = cardRepository.findAll();
		

		//IF CARD CLOSED, CARD SHOULD BE DELETED
		List<Card> filterCards = cards.stream()
										.filter( acc -> acc != null && acc.getStatus() == EStatus.CLOSED)
										.toList();
		
		filterCards.stream()
			.forEach( card -> {
				cardRepository.delete(card);
				logger.info("AutoUpdateCardService: deleteCard, cardNumber: " + card.getCardNumber());
			});
	}
}
