package currency.core;

public enum ECurrency{
	USD("USD"),
	EUR("EUR"),
	INR("INR"),
	KZT("KZT"),
	CAD("CAD"),
	SEK("SEK"),
	JPY("JPY");
	
	String code;
	ECurrency(String code)  { this.code = code; }

	public String getCode() { return code; }
}
