package com.sahachko.servletsProject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonSyntaxException;
import com.sahachko.servletsProject.exceptions.NullFieldException;
import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.model.AccountStatus;
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	@Mock
	UserService service;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@InjectMocks
	UserController controller;
	
	StringWriter stringWriter;
	PrintWriter printWriter;
	
	@BeforeEach
	public void init() throws IOException {
		stringWriter = new StringWriter();;
		printWriter = new PrintWriter(stringWriter);
	}
	
	public void testDoGet_shouldCallServiceGetAll_whenAllUsersRequested() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/servletsProject/users");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getAllUsers();
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenAllUsersRequested() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/servletsProject/users");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfAllUsersToHttpResponseBody_whenAllUsersRequested() throws IOException, ServletException {
		List<User> users = new ArrayList<>();
		User user = new User();
		Account account = new Account("Name", "Surname", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		user.setLogin("login");
		user.setAccount(account);
		user.setEvents(new ArrayList<Event>());
		user.setFiles(new ArrayList<UserFile>());
		users.add(user);
		when(request.getRequestURI()).thenReturn("/servletsProject/users");
		when(service.getAllUsers()).thenReturn(users);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("[{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}]", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetByIdMethod_whenUserByIdRequested() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/servletsProject/users/user");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getUserById(1);
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenUserByIdRequested() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/servletsProject/users/user");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfRequestedUserToHttpResponseBody_whenUserByIdRequested() throws IOException, ServletException {
		User user = new User();
		Account account = new Account("Name", "Surname", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		user.setLogin("login");
		user.setAccount(account);
		user.setEvents(new ArrayList<Event>());
		user.setFiles(new ArrayList<UserFile>());
		when(request.getRequestURI()).thenReturn("/servletsProject/users/user");
		when(request.getHeader("Id")).thenReturn("1");
		when(service.getUserById(1)).thenReturn(user);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderHasNotBeenSent() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/servletsProject/users/user");
		when(request.getHeader("Id")).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderIsNotWholeNumber() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/servletsProject/users/user");
		when(request.getHeader("Id")).thenReturn("1.2");
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoPost_shouldCallServiceSaveUserMethod() throws IOException, ServletException {
		String jsonUserObject = "{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(service).saveUser(any(User.class));
	}
	
	@Test
	public void testDoPost_shouldWriteJsonRepresentationOfSavedUserToHttpResponseBody() throws IOException, ServletException {
		String jsonUserObject = "{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.saveUser(any(User.class))).thenAnswer(inv -> {
			User user = inv.<User>getArgument(0);
			return user;
		});
		controller.doPost(request, response);
		assertEquals(jsonUserObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPost_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonUserObject = "{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(response).setStatus(201);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenRequestSendsNullForNotNullableFields() throws IOException, ServletException {
		String jsonUserObject = "{\"login\":\"login\",\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPost(request, response));
		assertEquals("Field in an user object which must be assigned has null value or vice versa", exception.getMessage());
	}
	
	@Test
	public void testDoPost_shouldTHrowExcetption_whenRequestSendsNotEmptyFilesOrEventsListWhileCreatingUser() throws IOException, ServletException {
		String jsonUserObject = "{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[{\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPost(request, response));
		assertEquals("Field in an user object which must be assigned has null value or vice versa", exception.getMessage());
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonUserObject = "{\"login\"_____:\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPost(request, response));
	}
	
	@Test
	public void testDoPut_shouldCallServiceUpdateUserMethod() throws IOException, ServletException {
		String jsonUserObject = "{\"id\":\"1\",\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonUserObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(service).updateUser(captor.capture());
		User user = captor.getValue();
		assertEquals(1, user.getId());
		assertEquals("login", user.getLogin());
		assertEquals("Name", user.getAccount().getFirstName());
		assertEquals(new ArrayList<UserFile>(), user.getFiles());
		assertEquals(new ArrayList<Event>(), user.getEvents());
	}
	
	@Test
	public void testDoPut_shouldWriteJsonRepresentationOfUpdatedUserToHttpResponseBody() throws IOException, ServletException {
		User user = new User();
		Account account = new Account("Name", "Surname", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		user.setId(1);
		user.setLogin("login");
		user.setAccount(account);
		user.setEvents(new ArrayList<Event>());
		user.setFiles(new ArrayList<UserFile>());
		String jsonAccountObject = "{\"id\":1,\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.getUserById(1)).thenReturn(user);
		controller.doPut(request, response);
		assertEquals(jsonAccountObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPut_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":1,\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
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
		String jsonAccountObject = "{\"login\":\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPut(request, response));
		assertEquals("Field in an user object which must be assigned has null value", exception.getMessage());
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonAccountObject = "{\"id\":1,\"login\"______:\"login\",\"account\":{\"firstName\":\"Name\",\"lastName\":\"Surname\",\"birthDate\":\"1994-01-22 at 00:00:00\",\"accountStatus\":\"REGULAR\"},\"files\":[],\"events\":[]}";
		StringReader reader = new StringReader(jsonAccountObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPut(request, response));
	}
	
	@Test
	public void testDoDelete_shouldCallServiceDeleteUserByIdMethod() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1");
		controller.doDelete(request, response);
		verify(service).deleteUserById(1);
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
