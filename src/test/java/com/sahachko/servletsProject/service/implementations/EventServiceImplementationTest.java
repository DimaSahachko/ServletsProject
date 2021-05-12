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
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.repository.EventRepository;


@ExtendWith(MockitoExtension.class)
class EventServiceImplementationTest {

	@Mock
	private EventRepository eventRepository;
	
	@InjectMocks
	private EventServiceImplementation eventService;
	
	@Test
	public void testSaveEvent_shouldCallRepositorySaveMethod() {
		Event event = mock(Event.class);
		eventService.saveEvent(event);
		verify(eventRepository).save(event);
	}
	
	@Test
	public void testSaveEvent_shouldReturnTheSameObject() {
		Event event = new Event();
		when(eventRepository.save(event)).thenReturn(event);
		assertEquals(event, eventService.saveEvent(event));
	}

	@Test
	public void testUpdateEvent_shouldCallRepositoryUpdateMethod() {
		Event event = mock(Event.class);
		eventService.updateEvent(event);
		verify(eventRepository).update(event);
	}
	
	@Test
	public void testUpdateEvent_shouldReturnTheSameObject() {
		Event event = new Event();
		when(eventRepository.update(event)).thenReturn(event);
		assertEquals(event, eventService.updateEvent(event));
	}
	
	@Test
	public void testGetAllEvents_shouldCallRepositoryGetAllMethod() {
		eventService.getAllEvents();
		verify(eventRepository).getAll();
	}
	
	@Test
	public void testGetAllEvents_shouldReturnListOfAllEvents() {
		List<Event> allEvents = Arrays.asList(new Event(), new Event());
		when(eventRepository.getAll()).thenReturn(allEvents);
		assertEquals(allEvents, eventService.getAllEvents());
	}
	
	@Test
	public void testGetEventById_shouldCallRepositoryGetByIdMethod() {
		eventService.getEventById(1);
		verify(eventRepository).getById(1);
	}
	
	@Test
	public void testGetEventById_shouldReturnGottenFromRepositoryEvent() {
		Event event = new Event();
		when(eventRepository.getById(1)).thenReturn(event);
		assertEquals(event, eventService.getEventById(1));
	}
	
	@Test
	public void testGetEventById_shouldThrowResourceNotFoundException() {
		when(eventRepository.getById(2)).thenThrow(new ResourceNotFoundException("There is no event with such id"));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(2));
		assertEquals("There is no event with such id", exception.getMessage());
	}
	
	@Test
	public void testDeleteEventById_shouldCallRepositoryDeleteByIdMethod() {
		eventService.deleteEventById(3);
		verify(eventRepository).deleteById(3);
	}
	
	@Test
	public void testDeleteEventById_shouldThrowException_whenThereIsNoEventWithSuchId() {
		doThrow(new ResourceNotFoundException("There is no event with such id")).when(eventRepository).deleteById(0);
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEventById(0));
		assertEquals("There is no event with such id", exception.getMessage());
	}
}
