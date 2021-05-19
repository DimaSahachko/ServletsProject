package com.sahachko.servletsProject.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static  org.mockito.Mockito.verify;
import static  org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonSyntaxException;
import com.sahachko.servletsProject.exceptions.NullFieldException;
import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.model.AccountStatus;
import com.sahachko.servletsProject.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
	
	@Mock
	AccountService service;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@InjectMocks
	AccountController controller;
	
	StringWriter stringWriter;
	PrintWriter printWriter;
	
	@BeforeEach
	public void init() throws IOException {
		stringWriter = new StringWriter();;
		printWriter = new PrintWriter(stringWriter);
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetAll_whenAllAccountsRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/accounts");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getAllAccounts();
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenAllAccountsRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/accounts");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfAllAccountsToHttpResponseBody_whenAllAccountsRequested() throws IOException, ServletException {
		List<Account> accounts = new ArrayList<>();
		Account acc1 = new Account("first", "last", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		accounts.add(acc1);
		acc1.setAccountStatus(AccountStatus.REGULAR);
		when(request.getServletPath()).thenReturn("/accounts");
		when(service.getAllAccounts()).thenReturn(accounts);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("[{\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-01-22\",\"accountStatus\":\"REGULAR\"}]", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetByIdMethod_whenAccountByIdRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/accounts/account");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getAccountById(1);
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenAccountByIdRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/accounts/account");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfRequestedAccountToHttpResponseBody_whenAccountByIdRequested() throws IOException, ServletException {
		Account acc1 = new Account("first", "last", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		when(request.getServletPath()).thenReturn("/accounts/account");
		when(request.getHeader("Id")).thenReturn("1");
		when(service.getAccountById(1)).thenReturn(acc1);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("{\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-01-22\",\"accountStatus\":\"REGULAR\"}", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderHasNotBeenSent() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/accounts/account");
		when(request.getHeader("Id")).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderIsNotWholeNumber() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/accounts/account");
		when(request.getHeader("Id")).thenReturn("1.2");
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoPost_shouldCallServiceSaveAccountMethod() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":\"1\",\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(service).saveAccount(any(Account.class));
	}
	
	@Test
	public void testDoPost_shouldWriteJsonRepresentationOfSavedAccountToHttpResponseBody() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":1,\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.saveAccount(any(Account.class))).thenAnswer(inv -> {
			Account acc = inv.<Account>getArgument(0);
			return acc;
		});
		controller.doPost(request, response);
		assertEquals(jsonAccountObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPost_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":\"1\",\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(response).setStatus(201);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenRequestSendsNullForNotNullableFields() throws IOException, ServletException {
		String jsonAccountObject = "{\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPost(request, response));
		assertEquals("Field in an account object which must be assigned has null value", exception.getMessage());
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonAccountObject = "{\"firstName\"_____\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPost(request, response));
	}
	
	@Test
	public void testDoPut_shouldCallServiceUpdateAccountMethod() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":\"1\",\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		verify(service).updateAccount(any(Account.class));
	}
	
	@Test
	public void testDoPut_shouldWriteJsonRepresentationOfUpdatedAccountToHttpResponseBody() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":1,\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.updateAccount(any(Account.class))).thenAnswer(inv -> {
			Account acc = inv.<Account>getArgument(0);
			return acc;
		});
		controller.doPut(request, response);
		assertEquals(jsonAccountObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPut_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":\"1\",\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestSendsNullForNotNullableFields() throws IOException, ServletException {
		String jsonAccountObject = "{\"firstName\":\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\",\"signUpDate\":\"2021-05-10\",\"accountStatus\":\"REGULAR\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPut(request, response));
		assertEquals("Field in an account object which must be assigned has null value", exception.getMessage());
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonAccountObject = "{\"firstName\"_____\"first\",\"lastName\":\"last\",\"birthDate\":\"1994-03-10\"}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPut(request, response));
	}
	
	@Test
	public void testDoDelete_shouldCallServiceDeleteAcountByIdMethod() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1");
		controller.doDelete(request, response);
		verify(service).deleteAccountById(1);
	}
	
	@Test
	public void testDoDelete_shouldSet204StatusToHttpResponse() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1");
		controller.doDelete(request, response);
		verify(response).setStatus(204);
	}
	
	@Test
	public void testDoDelete_shouldThrowException_whenIdHeaderHasNotBeenSent() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> controller.doDelete(request, response));
	}
	
	@Test
	public void testDoDelete_shouldThrowException_whenIdHeaderIsNotWholeNumber() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1.2");
		assertThrows(IllegalArgumentException.class, () -> controller.doDelete(request, response));
	}
}
