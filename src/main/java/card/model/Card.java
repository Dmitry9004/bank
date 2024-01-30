package card.model;

import java.time.LocalDate;
import java.time.Period;

import account.core.EAccount;
import account.core.EStatus;
import account.model.Account;
import card.core.ECard;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cards")
public  class Card {
	
	@Id
	@GeneratedValue
	private int cardNumber;
	
	@NotNull
	private int userId;
	
	@NotNull
	private String phone;
	
	@NotNull
	private double total = 0;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private EStatus status = EStatus.OPENED;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private ECard typeCard;
}
