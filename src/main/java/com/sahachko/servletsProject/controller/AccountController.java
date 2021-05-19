package com.sahachko.servletsProject.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sahachko.servletsProject.exceptions.NullFieldException;
import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.repository.hibernate.HibernateAccountRepository;
import com.sahachko.servletsProject.service.AccountService;
import com.sahachko.servletsProject.service.implementations.AccountServiceImplementation;

@SuppressWarnings("serial")
public class AccountController extends HttpServlet {
	private AccountService service;
	private Gson json;

	public AccountController() {
		super();
		this.service = new AccountServiceImplementation(new HibernateAccountRepository());
		this.json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if(servletPath.equals("/accounts")) {
			getAll(request, response);
		} else {
			getById(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line; 
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
			requestBody.append(System.lineSeparator());
		}
		Account account = json.fromJson(requestBody.toString(), Account.class);
		if (account.getFirstName() == null || account.getAccountStatus() == null || account.getBirthDate() == null
			|| account.getLastName() == null) {
			throw new NullFieldException("Field in an account object which must be assigned has null value");
		}
		account = service.saveAccount(account);
		String accountInJsonFormat = json.toJson(account);
		response.setStatus(201);
		response.setContentType("application/json");
		response.getWriter().print(accountInJsonFormat);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line; 
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
			requestBody.append(System.lineSeparator());
		}
		Account account = json.fromJson(requestBody.toString(), Account.class);
		if (account.getId() == null || account.getFirstName() == null || account.getAccountStatus() == null
			|| account.getBirthDate() == null || account.getLastName() == null || account.getSignUpDate() == null) {  
			throw new NullFieldException("Field in an account object which must be assigned has null value");
		}
		account = service.updateAccount(account);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(account));
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int accountId = Integer.parseInt(request.getHeader("Id"));
		service.deleteAccountById(accountId);
		response.setStatus(204);
	}
	
	private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Account> accounts = service.getAllAccounts();
		String accountsInJsonFormat = json.toJson(accounts);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(accountsInJsonFormat);
	}
	
	private void getById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int accountId = Integer.parseInt(request.getHeader("Id"));
		Account account = service.getAccountById(accountId);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(account));
	}
}
