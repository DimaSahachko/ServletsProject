package com.sahachko.servletsProject.service;

import java.util.List;
import com.sahachko.servletsProject.model.Account;

public interface AccountService {
	
	Account saveAccount(Account account);
	
	Account updateAccount(Account account);

	List<Account> getAllAccounts();
	
	Account getAccountById(int id);
	
	void deleteAccountById(int id);
}
