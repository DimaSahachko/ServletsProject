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
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.model.EventAction;
import com.sahachko.servletsProject.service.EventService;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
	
	@Mock
	EventService service;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@InjectMocks
	EventController controller;
	
	StringWriter stringWriter;
	PrintWriter printWriter;
	
	@BeforeEach
	public void init() throws IOException {
		stringWriter = new StringWriter();;
		printWriter = new PrintWriter(stringWriter);
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetAll_whenAllEventsRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/events");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getAllEvents();
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenAllEventsRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/events");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfAllEventsToHttpResponseBody_whenAllEventsRequested() throws IOException, ServletException {
		List<Event> events = new ArrayList<>();
		Event event = new Event(1, "testFile.pdf", EventAction.UPLOADING);
		events.add(event);
		when(request.getServletPath()).thenReturn("/events");
		when(service.getAllEvents()).thenReturn(events);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("[{\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}]", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldCallServiceGetByIdMethod_whenEventByIdRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/events/event");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(service).getEventById(1);
	}
	
	@Test
	public void testDoGet_shouldSetCorrectStatusAndHeaderToHttpResponse_whenEventByIdRequested() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/events/event");
		when(request.getHeader("Id")).thenReturn("1");
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoGet_shouldWriteJsonRepsresentationOfRequestedEventToHttpResponseBody_whenEventByIdRequested() throws IOException, ServletException {
		Event event = new Event(1, "testFile.pdf", EventAction.UPLOADING);
		event.setId(100);
		when(request.getServletPath()).thenReturn("/events/event");
		when(request.getHeader("Id")).thenReturn("100");
		when(service.getEventById(100)).thenReturn(event);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doGet(request, response);
		assertEquals("{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}", stringWriter.toString());
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderHasNotBeenSent() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/events/event");
		when(request.getHeader("Id")).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoGet_shouldThrowException_whenIdHeaderIsNotWholeNumber() throws IOException, ServletException {
		when(request.getServletPath()).thenReturn("/events/event");
		when(request.getHeader("Id")).thenReturn("1.2");
		assertThrows(IllegalArgumentException.class, () -> controller.doGet(request, response));
	}
	
	@Test
	public void testDoPost_shouldCallServiceSaveEventMethod() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(service).saveEvent(any(Event.class));
	}
	
	@Test
	public void testDoPost_shouldWriteJsonRepresentationOfSavedEventToHttpResponseBody() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.saveEvent(any(Event.class))).thenAnswer(inv -> {
			Event event = inv.<Event>getArgument(0);
			return event;
		});
		controller.doPost(request, response);
		assertEquals(jsonEventObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPost_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPost(request, response);
		verify(response).setStatus(201);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenRequestSendsNullForNotNullableFields() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPost(request, response));
		assertEquals("Field in an event object which must be assigned has null value", exception.getMessage());
	}
	
	@Test
	public void testDoPost_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\"____:1,\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPost(request, response));
	}
	
	@Test
	public void testDoPut_shouldCallServiceUpdateEventMethod() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventTime\":\"2021-05-11 at 12:17:55\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		verify(service).updateEvent(any(Event.class));
	}
	
	@Test
	public void testDoPut_shouldWriteJsonRepresentationOfUpdatedEventToHttpResponseBody() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventTime\":\"2021-05-11 at 12:17:55\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		when(service.updateEvent(any(Event.class))).thenAnswer(inv -> {
			Event event = inv.<Event>getArgument(0);
			return event;
		});
		controller.doPut(request, response);
		assertEquals(jsonEventObject, stringWriter.toString());
	}
	
	@Test
	public void testDoPut_shouldSetCorrectStatusAndHeaderToHttpResponse() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventTime\":\"2021-05-11 at 12:17:55\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		controller.doPut(request, response);
		verify(response).setStatus(200);
		verify(response).setContentType("application/json");
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestSendsNullForNotNullableFields() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\":1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		Exception exception = assertThrows(NullFieldException.class, () -> controller.doPut(request, response));
		assertEquals("Field in an event object which must be assigned has null value", exception.getMessage());
	}
	
	@Test
	public void testDoPut_shouldThrowException_whenRequestHasBadJsonSyntax() throws IOException, ServletException {
		String jsonEventObject = "{\"id\":100,\"fileId\"___:1,\"fileName\":\"testFile.pdf\",\"eventAction\":\"UPLOADING\"}";
		StringReader reader = new StringReader(jsonEventObject);
		BufferedReader bufferedReader = new BufferedReader(reader);
		when(request.getReader()).thenReturn(bufferedReader);
		assertThrows(JsonSyntaxException.class, () -> controller.doPut(request, response));
	}
	
	@Test
	public void testDoDelete_shouldCallServiceDeleteEventByIdMethod() throws IOException, ServletException {
		when(request.getHeader("Id")).thenReturn("1");
		controller.doDelete(request, response);
		verify(service).deleteEventById(1);
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
