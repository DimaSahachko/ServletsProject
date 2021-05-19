package com.sahachko.servletsProject.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
import com.sahachko.servletsProject.exceptions.IncorrectHeaderException;
import com.sahachko.servletsProject.exceptions.NullFieldException;
import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.service.UserFileService;

@ExtendWith(MockitoExtension.class)
class UserFileControllerTest {

	@Mock
	UserFileService service;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@InjectMocks
	UserFileController controller;
	
	StringWriter stringWriter;
	PrintWriter printWriter;
	
	@BeforeEach
	public void init() throws IOException {
		stringWriter = new StringWriter();;
		printWriter = new PrintWriter(stringWriter);
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetAll_whenAllFilesRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/files");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getAllUsersFiles();
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenAllFilesRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/files");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfAllFilesToHttpResponseBody_whenAllFilesRequested() throws IOException, ServletException {
		List<UserFile> files = new ArrayList<>();
		UserFile file = new UserFile("testFile.pdf", 101, FileStatus.ACTIVE);
		files.add(file);
		when(request.getServletPath()).thenReturn("/files");
		when(service.getAllUsersFiles()).thenReturn(files);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("[{\"name\":\"testFile.pdf\",\"userId\":101,\"status\":\"ACTIVE\"}]", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetByIdMethod_whenFileByIdRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/files/file");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getUserFileById(1);
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenFileByIdRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/files/file");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfRequestedFileToHttpResponseBody_whenAccountByIdRequested() throws IOException, ServletException {
		UserFile file = new UserFile("testFile.pdf", 101, FileStatus.ACTIVE);
		file.setId(1);
		when(request.getServletPath()).thenReturn("/files/file");
		when(request.getHeader("Id")).thenReturn("1");
		when(service.getUserFileById(1)).thenReturn(file);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("{\"id\":1,\"name\":\"testFile.pdf\",\"userId\":101,\"status\":\"ACTIVE\"}", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderHasNotBeenSent() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/files/file");
		when(request.getHeader("Id")).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderIsNotWholeNumber() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/files/file");
		when(request.getHeader("Id")).thenReturn("1.2");
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenIdHeaderHasNotBeenSent() {
		when(request.getHeader("Id")).thenReturn(null);
		when(request.getHeader("Filename")).thenReturn("testFile.pdf");
		Exception exception = assertThrows(IncorrectHeaderException.class, () -> controller.doPost(request, response));
		assertEquals("Required \"Filename\" or \"Id\" header wasn't sent along with request", exception.getMessage());
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenFilenameHeaderHasNotBeenSent() {
		when(request.getHeader("Id")).thenReturn("1");
		when(request.getHeader("Filename")).thenReturn(null);
		Exception exception = assertThrows(IncorrectHeaderException.class, () -> controller.doPost(request, response));
		assertEquals("Required \"Filename\" or \"Id\" header wasn't sent along with request", exception.getMessage());
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenIdHeaderIsNotWholeNumber() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1.2");
		when(request.getHeader("Filename")).thenReturn("testFile.pdf");
		assertThrows(IllegalArgumentException.class, () -> controller.doPost(request, response));
	}
	
	@Test
	public void testDoPost_shouldCallServiceSaveUserFileWithCorrectArguments() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("101");
		when(request.getHeader("Filename")).thenReturn("testFile.pdf");
		when(request.getContentLength()).thenReturn(3);
		byte [] bytes = {1, 2, 3};
		when(request.getInputStream()).thenReturn(getCustomInputStream());
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		ArgumentCaptor<UserFile> fileCaptor = ArgumentCaptor.forClass(UserFile.class);
		ArgumentCaptor<byte[]> bytesCaptor = ArgumentCaptor.forClass(byte[].class);
		verify(service).saveUserFile(fileCaptor.capture(), bytesCaptor.capture(), eq("testFile.pdf"));
		UserFile file = fileCaptor.getValue();
		byte[] capturedBytes = bytesCaptor.getValue();
		assertEquals(101, file.getUserId());
		assertArrayEquals(bytes, capturedBytes);
	}
	
	@Test
	public void testDoPost_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("101");
		when(request.getHeader("Filename")).thenReturn("testFile.pdf");
		when(request.getContentLength()).thenReturn(3);
		when(request.getInputStream()).thenReturn(getCustomInputStream());
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(response).setStatus(201);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPost_shouldWriteJsonRepresentationOfSavedUserToHttpResponseBody() throws IOException, ServletException {
		UserFile file = new UserFile("testFile.pdf", 101, FileStatus.ACTIVE);
		file.setId(1);
		file.setCreated(new GregorianCalendar(2021, 05, 11, 17, 53, 27).getTime());
		when(request.getHeader("Id")).thenReturn("101");
		when(request.getHeader("Filename")).thenReturn("testFile.pdf");
		when(request.getContentLength()).thenReturn(3);
		when(request.getInputStream()).thenReturn(getCustomInputStream());
		when(service.saveUserFile(any(UserFile.class), any(byte[].class), eq("testFile.pdf"))).thenReturn(file);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		assertEquals("{\"id\":1,\"name\":\"testFile.pdf\",\"userId\":101,\"created\":\"2021-06-11 at 17:53:27\",\"status\":\"ACTIVE\"}", stringWriter.toString());
	}
	
	@Test
	public void testDoPut_shouldCallServiceUpdateUserFileMethod() throws IOException, ServletException {
		String jsonFileObject = "{\"id\":1,\"name\":\"testFile.pdf\",\"userId\":101,\"created\":\"2021-06-11 at 17:53:27\",\"status\":\"ACTIVE\"}";
		StringReader reader = new StringReader(jsonFileObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		ArgumentCaptor<UserFile> captor = ArgumentCaptor.forClass(UserFile.class);
		verify(service).updateUserFile(captor.capture());
		UserFile file = captor.getValue();
		assertEquals(1, file.getId());
		assertEquals("testFile.pdf", file.getName());
		assertEquals(101, file.getUserId());
		assertEquals(new GregorianCalendar(2021, 05, 11, 17, 53, 27).getTime(), file.getCreated());
		assertEquals(FileStatus.ACTIVE, file.getStatus());
	}
	
	@Test
	public void testDoPut_shouldWriteJsonRepresentationOfUpdatedUserFileToHttpResponseBody() throws IOException, ServletException {
		String jsonFileObject = "{\"id\":1,\"name\":\"testFile.pdf\",\"userId\":101,\"created\":\"2021-06-11 at 17:53:27\",\"status\":\"ACTIVE\"}";
		StringReader reader = new StringReader(jsonFileObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.updateUserFile(any(UserFile.class))).thenAnswer(inv -> {
			UserFile file = inv.<UserFile>getArgument(0);
			return file;
		});
		controller.doPut(request, response);
		assertEquals(jsonFileObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPut_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonFileObject = "{\"id\":1,\"name\":\"testFile.pdf\",\"userId\":101,\"created\":\"2021-06-11 at 17:53:27\",\"status\":\"ACTIVE\"}";
		StringReader reader = new StringReader(jsonFileObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestSendsNullForNotNullableFields() throws IOException, ServletException {
		String jsonFileObject = "{\"id\":1,\"name\":\"testFile.pdf\",\"userId\":101,\"status\":\"ACTIVE\"}";
		StringReader reader = new StringReader(jsonFileObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPut(request, response));
		assertEquals("Field in an UserFile object which must be assigned has null value", exception.getMessage());
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonFileObject = "{\"id\"_____:1,\"name\":\"testFile.pdf\",\"userId\":101,\"created\":\"2021-06-11 at 17:53:27\",\"status\":\"ACTIVE\"}";
		StringReader reader = new StringReader(jsonFileObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPut(request, response));
	}
	
	@Test
	public void testDoDelete_shouldCallServiceDeleteUserFileByIdMethod() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1");
		controller.doDelete(request, response);
		verify(service).deleteUserFileById(1);
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

	ServletInputStream getCustomInputStream() {
		byte [] bytes = {1, 2, 3};
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return inputStream.read();
			}
			@Override
			public boolean isFinished() {
				return false;
			}
			@Override
			public boolean isReady() {
				return false;
			}
			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}
}
