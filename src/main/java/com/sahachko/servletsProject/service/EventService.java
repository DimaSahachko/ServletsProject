package com.sahachko.servletsProject.service;

import java.util.List;
import com.sahachko.servletsProject.model.Event;

public interface EventService {
	
	Event saveEvent(Event event);

	Event updateEvent(Event event);

	List<Event> getAllEvents();
	
	Event getEventById(int id);
	
	void deleteEventById(int id);
}
