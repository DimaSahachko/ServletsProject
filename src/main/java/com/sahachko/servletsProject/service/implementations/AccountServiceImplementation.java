package com.sahachko.servletsProject.service.implementations;

import java.util.List;

import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.repository.AccountRepository;
import com.sahachko.servletsProject.service.AccountService;

public class AccountServiceImplementation implements AccountService {
	private AccountRepository repository;
	
	public AccountServiceImplementation(AccountRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Account saveAcount(Account account) {
		return repository.save(account);
	}

	@Override
	public Account updateAccount(Account account) {
		return repository.update(account);
	}

	@Override
	public List<Account> getAllAccounts() {
		return repository.getAll();
	}

	@Override
	public Account getAccountById(int id) {
		return repository.getById(id);
	}

	@Override
	public boolean deleteAccountById(int id) {
		return repository.deleteById(id);
	}
	
}
