package com.sahachko.servletsProject.repository.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.hibernate.Session;

import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.repository.EventRepository;

public class HibernateEventRepository implements EventRepository {
	
	@Override
	public List<Event> getAll() {
		List<Event> allEvents = new ArrayList<>();
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			allEvents = session.createQuery("from Event", Event.class).getResultList();
			session.getTransaction().commit();
		}
		return allEvents;
	}

	@Override
	public Event getById(Integer id) {
		Event event = null;
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			event = session.get(Event.class, id);
			session.getTransaction().commit();
		}
		if(event == null) {
			throw new ResourceNotFoundException("There is no event with such id");
		}
		return event;
	}

	@Override
	public Event save(Event event) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			event.setEventTime(new Date());
			session.save(event);
			session.getTransaction().commit();
		}
		return event;
	}

	@Override
	public Event update(Event event) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(event);
			session.getTransaction().commit();
		} catch(OptimisticLockException exc) {
			throw new ResourceNotFoundException("There is no event with such id");
		}
		return event;
	}

	@Override
	public void deleteById(Integer id) {
		Event event = getById(id);
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(event);
			session.getTransaction().commit();
		}
	}
}
