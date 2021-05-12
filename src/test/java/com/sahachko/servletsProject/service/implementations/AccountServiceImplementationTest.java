package com.sahachko.servletsProject.service.implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplementationTest {
	
	@InjectMocks
	private AccountServiceImplementation accountService;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Test
	public void testSaveAccount_shouldCallRepositorySaveMethod() {
		Account account = new Account();
		accountService.saveAccount(account);
		verify(accountRepository).save(account);
	}
	
	@Test
	public void testSaveAccount_shouldReturnTheSameObject() {
		Account account = new Account();
		when(accountRepository.save(account)).thenReturn(account);
		assertEquals(account, accountService.saveAccount(account));
	}

	@Test
	public void testUpdateAccount_shouldCallRepositoryUpdateMethod() {
		Account account = new Account();
		accountService.updateAccount(account);
		verify(accountRepository).update(account);
	}
	
	@Test
	public void testUpdateAccount_shouldReturnTheSameObject() {
		Account account = new Account();
		when(accountRepository.update(account)).thenReturn(account);
		assertEquals(account, accountService.updateAccount(account));
	}
	
	@Test
	public void testGetAllAccounts_shouldCallRepositoryGetAllMethod() {
		accountService.getAllAccounts();
		verify(accountRepository).getAll();
	}
	
	@Test
	public void testGetAllAccounts_shouldReturnListOfAllAccounts() {
		List<Account> allAccounts = Arrays.asList(new Account(), new Account());
		when(accountRepository.getAll()).thenReturn(allAccounts);
		assertEquals(allAccounts, accountService.getAllAccounts());
	}
	
	@Test
	public void testGetAccountById_shouldCallRepositoryGetByIdMethod() {
		accountService.getAccountById(1);
		verify(accountRepository).getById(1);
	}
	
	@Test
	public void testGetAccountById_shouldReturnGottenFromRepositoryAccount() {
		Account account = new Account();
		when(accountRepository.getById(1)).thenReturn(account);
		assertEquals(account, accountService.getAccountById(1));
	}
	
	@Test
	public void testGetAccountById_shouldThrowResourceNotFoundException() {
		when(accountRepository.getById(2)).thenThrow(new ResourceNotFoundException("There is no account with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(2));
		assertEquals("There is no account with such id", exception.getMessage());
	}
	
	@Test
	public void testDeleteAccountById_shouldCallRepositoryDeleteByIdMethod() {
		accountService.deleteAccountById(3);
		verify(accountRepository).deleteById(3);
	}
	
	@Test
	public void testDeleteAccountById_shouldThrowException_whenThereIsNoAccountWithSuchId() {
		doThrow(new ResourceNotFoundException("There is no account with such id")).when(accountRepository).deleteById(0);
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccountById(0));
		assertEquals("There is no account with such id", exception.getMessage());
	}
}
