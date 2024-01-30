package account.core;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import account.model.Account;
import account.repository.AccountRepository;

@Service
public class AutoUpdateAccountService {

	private final int DAYS = 28;
	private final Logger logger = LoggerFactory.getLogger(AutoUpdateAccountService.class);
	
	private AccountRepository accountRepository;
	
	@Autowired
	public AutoUpdateAccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Scheduled(cron = "0 0 0 * * *") //Every day
	public void updateAccount() {
		List<Account> accounts = accountRepository.findAll();
		
		//IF ACCOUNT CLOSED OR WAS OPENED LESS 28 DAYS, ACCOUNT NOT UPDATE
		List<Account> filterAccounts = accounts.stream()
										.filter( acc -> {
											if(acc != null && acc.getStatus() == EStatus.OPENED) {
												Period per = Period.between(acc.getDateUpdate(), LocalDate.now());
												return per.getDays() >= DAYS;
											}
												return false;
										})
										.toList();
		
		//ADD RATE ACCOUNT TO TOTAL 
		List<Account> mapAccounts = filterAccounts.stream()
										.map( acc -> {
											acc.setDateUpdate(LocalDate.now());
											acc.setTotal(acc.getTotal() + acc.getTotal() * acc.getTypeAccount().getRate());
											return acc;
										})
										.toList();
		
		accountRepository.saveAll(mapAccounts);
		
		mapAccounts.stream()
			.forEach( acc -> 
				logger.info("AutoUpdateAccountService: updateAccount, code: " + acc.getBankIdentificationCode())
			);
	}
	
	@Scheduled(cron = "0 0 0 * * 0") //Every week
	public void deleteAccount() {
		List<Account> accounts = accountRepository.findAll();

		//IF ACCOUNT CLOSED, ACCOUNT SHOULD BE DELETED
		List<Account> filterAccounts = accounts.stream()
										.filter( acc -> acc != null && acc.getStatus() == EStatus.CLOSED)
										.toList();
		
		filterAccounts.stream()
			.forEach( acc -> {
				accountRepository.delete(acc);
				logger.info("AutoUpdateAccountService: deleteAccount, code: "+acc.getBankIdentificationCode());
			});
	}
}
