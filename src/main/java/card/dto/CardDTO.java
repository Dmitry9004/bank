package card.dto;

import java.time.Period;

import account.core.EStatus;
import card.core.ECard;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDTO {
	@NotNull
	private int cardNumber;
	@NotNull
	private int validityInDaysPeriod;
	@NotNull
	private double total;
	@NotNull
	private EStatus status;
	@NotNull
	private ECard type;
}
