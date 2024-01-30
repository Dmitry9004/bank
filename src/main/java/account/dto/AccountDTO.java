package account.dto;

import java.time.LocalDate;

import account.core.EAccount;
import account.core.EStatus;
import account.model.Account;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import user.model.User;

@Getter
@Setter
public class AccountDTO {
	@NotNull
	private double rate;
	
	@NotNull
	private boolean canTakerOrAdd;
	
	@NotNull
	private int bankIdentificationCode;
	
	@NotNull
	private double total;
	
	@NotNull
	private EStatus status;
	
	@NotNull
	private EAccount typeAccount;
	
	@NotNull
	private LocalDate dateUpdate;
}
