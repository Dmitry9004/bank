package account.model;

import java.time.LocalDate;


import account.core.EAccount;
import account.core.EStatus;
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
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue
	private int userId;
	@NotNull
	private int bankIdentificationCode;
	@NotNull
	private double total;
	@NotNull
	@Enumerated(EnumType.STRING)
	private EStatus status = EStatus.OPENED;
	@NotNull
	@Enumerated(EnumType.STRING)
	private EAccount typeAccount;
	@NotNull
	private LocalDate dateUpdate;
}
