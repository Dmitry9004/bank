package currency.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties (value = { "ID", "NumCode", "Nominal",})
public class Currency {
	@JsonProperty("CharCode")
	private String charCode;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Value")
	private double value;
	
	@JsonProperty("Previous")
	private double previous;
}
