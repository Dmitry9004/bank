package currency.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import card.controller.CardController;
import currency.dto.CurrencyDTO;
import currency.model.Currency;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Service
public class CurrencyService {
	
	
	private final String URL = "https://www.cbr-xml-daily.ru/daily_json.js";
	private final String CURRENCY_NODE = "Valute";
	
	private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
	
	private RestTemplate template;
	private ObjectMapper mapper;
	
	public CurrencyService(RestTemplate template, ObjectMapper mapper) {
		this.template = template; 
		this.mapper = mapper;
	}
	
	public List<Currency> getAll(){
		Currency currencyCurr;
		List<Currency> list = new ArrayList<>();
		
		try {
			String json = template.getForObject(URL, String.class);
			
			for(ECurrency it : ECurrency.values()) {
				String currencyJson = mapper.readTree(json)
						.path(CURRENCY_NODE)
						.path(it.getCode())
						.toString();
				
				currencyCurr = mapper
						.readValue(currencyJson, Currency.class);
				
				list.add(currencyCurr);
			}	
			
			}catch(Exception e) {	
				logger.error("CurrencyService: getAll, error: " + e.getMessage());
		}
		
		return list;
	}
	
	public Currency getCurrency(ECurrency type) {
		Currency currency = null;
		
		try {
			String json = template.getForObject(URL, String.class);
			String currencyJson = mapper.readTree(json)
					.path(CURRENCY_NODE)
					.path(type.getCode())
					.toString();
			
			currency = mapper
					.readValue(currencyJson, Currency.class);			
		
		}catch(Exception e) {
			logger.error("CurrencyService: getCurrency, " + "type: " + type.getCode()  + " error: " + e.getMessage());
		}
		
		return currency;
	}
}
