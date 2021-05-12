package com.sahachko.servletsProject.service.implementations;

import java.util.List;

import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.repository.EventRepository;
import com.sahachko.servletsProject.service.EventService;

public class EventServiceImplementation implements EventService {
	
	private EventRepository repository;
	
	public EventServiceImplementation(EventRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Event saveEvent(Event event) {
		return repository.save(event);
	}

	@Override
	public Event updateEvent(Event event) {
		return repository.update(event);
	}

	@Override
	public List<Event> getAllEvents() {
		return repository.getAll();
	}

	@Override
	public Event getEventById(int id) {
		return repository.getById(id);
	}

	@Override
	public void deleteEventById(int id) {
		repository.deleteById(id);
	}
	
}
