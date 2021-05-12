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
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.repository.hibernate.HibernateEventRepository;
import com.sahachko.servletsProject.service.EventService;
import com.sahachko.servletsProject.service.implementations.EventServiceImplementation;

@SuppressWarnings("serial")
public class EventController extends HttpServlet {
	private EventService service;
	private Gson json;
	
	public EventController() {
		this.service = new EventServiceImplementation(new HibernateEventRepository());
		this.json = new GsonBuilder().setDateFormat("yyyy-MM-dd 'at' HH:mm:ss").create();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		if(uri.equals("/servletsProject/events")) {
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
		Event event = json.fromJson(requestBody.toString(), Event.class);
		if(event.getFileId() == null || event.getFileName() == null || event.getEventAction() == null) {
			throw new NullFieldException("Field in an event object which must be assigned has null value");
		}
		event = service.saveEvent(event);
		response.setStatus(201);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(event));
	}


	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line; 
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
			requestBody.append(System.lineSeparator());
		}
		Event event = json.fromJson(requestBody.toString(), Event.class);
		if(event.getId() == null || event.getFileId() == null || event.getFileName() == null || event.getEventTime() == null || event.getEventAction() == null) {
			throw new NullFieldException("Field in an event object which must be assigned has null value");
		}
		event = service.updateEvent(event);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(event));
		
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int eventId = Integer.parseInt(request.getHeader("Id"));
		service.deleteEventById(eventId);
		response.setStatus(204);
	}
	
	private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Event> events = service.getAllEvents();
		String eventsInJsonFormat = json.toJson(events);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(eventsInJsonFormat);
	}
	
	private void getById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int eventId = Integer.parseInt(request.getHeader("Id"));
		Event event = service.getEventById(eventId);
		response.setStatus(200);
		response.setContentType("application/json");
		response.getWriter().print(json.toJson(event));
	}
	
	
}
