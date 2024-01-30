package card.core;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import account.core.EStatus;
import card.model.Card;
import card.repository.CardRepository;
import interfaces.CustomService;
import interfaces.DetailsService;
import user.model.User;
import user.repository.UserRepository;

@Service
@Transactional
public class CardService implements CustomService<Card>, DetailsService<Card>{

	private CardRepository cardRepository;
	private UserRepository userRepository;
	
	public CardService(CardRepository cardRepository, UserRepository userRepository) {
		this.cardRepository = cardRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public Optional<Card> get(int id) {
		return cardRepository.findById(id);
	}

	@Override
	public List<Card> getAll() {
		return cardRepository.findAll();
	}	
		
	@Override
	public List<Card> getAllByUserId(int userId) {
		return cardRepository.getAllByUserId(userId);
	}

	@Override
	public void save(Card t) {
		cardRepository.save(t);
	}
	
	@Override
	public void delete(Card t) {
		this.closeCard(t);
	}
	
	public Optional<Card> getByPhone(String phone){
		return cardRepository.getByPhone(phone);
	}
	
	private void closeCard(Card card) {
		if(card == null)
				return;
				
		card.setStatus(EStatus.CLOSED);
		cardRepository.save(card);
	}

}
