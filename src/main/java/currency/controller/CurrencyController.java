package currency.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import currency.core.CurrencyService;
import currency.core.ECurrency;
import currency.dto.CurrencyDTO;
import currency.model.Currency;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
	
	private final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
	
	private CurrencyService currService;
	private ModelMapper mapper;
	
	public CurrencyController(CurrencyService service, ModelMapper mapper) {
		this.currService = service;
		this.mapper = mapper;
	}
	
	@GetMapping("/all")
	public List<CurrencyDTO> getAll(){
		List<Currency> currencyList = currService.getAll();
		
		logger.info("CurrencyController: getAll");
		
		return currencyList.stream()
				.map(curr -> mapper.map(curr, CurrencyDTO.class))
				.toList();
	}
	
	@GetMapping
	public ResponseEntity<CurrencyDTO> get(@RequestParam(value = "type") ECurrency type) 
			throws ResponseStatusException{
		if(type == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		Currency currency = currService.getCurrency(type);
		
		logger.info("CurrencyController: get, type: " + type.toString());
		
		return new ResponseEntity<>(mapper.map(currency, CurrencyDTO.class), HttpStatus.OK);
	}
}
