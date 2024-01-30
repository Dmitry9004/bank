package account.core;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import account.dto.AccountDTO;
import account.model.Account;
import account.repository.AccountRepository;
import interfaces.CustomService;
import interfaces.DetailsService;

@Service
@Transactional
public class AccountService implements CustomService<Account>, DetailsService<Account>{
	
	private AccountRepository accountRepository;
	
	
	public AccountService(AccountRepository rep) {
		this.accountRepository = rep;
	}
	
	@Override
	public Optional<Account> get(int accountCode) {
		return accountRepository.findById(accountCode);
	}

	@Override
	public List<Account> getAllByUserId(int userId) {
		return accountRepository.getAllByUserId(userId);
	}
	
	@Override
	public void save(Account account) {
		accountRepository.save(account);
	}
	
	@Override
	public List<Account> getAll() {
		return accountRepository.findAll();
	}

	@Override
	public void delete(Account t) {
		closeAccount(t);
	}
	
	private void closeAccount(Account acc) {
		if(acc == null)
			return;
		
		acc.setStatus(EStatus.CLOSED);
		accountRepository.save(acc);
	}	
}
