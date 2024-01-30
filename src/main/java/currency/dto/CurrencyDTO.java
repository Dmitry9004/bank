package currency.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDTO {
	@NotNull
	private String charCode;
	@NotNull
	private String name;
	@NotNull
	private double value;
	@NotNull
	private double previous;
}
