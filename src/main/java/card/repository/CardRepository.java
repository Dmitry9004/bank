package card.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import card.model.Card;
import interfaces.DetailsRepository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer>,  DetailsRepository<Card>{
	public Optional<Card> getByPhone(String phone);
}