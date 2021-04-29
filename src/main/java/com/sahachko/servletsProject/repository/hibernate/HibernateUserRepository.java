package com.sahachko.servletsProject.repository.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;

import org.hibernate.Session;

import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.repository.UserRepository;

public class HibernateUserRepository implements UserRepository {

	@Override
	public List<User> getAll() {
		List<User> allUsers = new ArrayList<>();
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			allUsers = session.createQuery("select distinct u from User u join fetch u.account left join fetch u.events", User.class).getResultList();
			allUsers = session.createQuery("select distinct u from User u left join fetch u.files where u in :users", User.class).setParameter("users", allUsers).getResultList();
			session.getTransaction().commit();
		}
		return allUsers;
	}

	@Override
	public User getById(Integer id) {
		User user = null;
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			user = session.createQuery("from User u join fetch u.account left join fetch u.events where u.id = :id", User.class).setParameter("id", id).getSingleResult();
			user = session.createQuery("from User u left join fetch u.files f where u = :fetchedUser", User.class).setParameter("fetchedUser", user).getSingleResult();
			session.getTransaction().commit();
		} catch (NoResultException exc) {
			exc.printStackTrace();
		}
		return user;
	}

	@Override
	public User save(User user) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		}
		
		return user;
	}

	@Override
	public User update(User user) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		} catch(OptimisticLockException exc) {
			user = null;
		}
		return user;
	}

	@Override
	public boolean deleteById(Integer id) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			User user = session.get(User.class, id);
			if(user == null) {
				session.getTransaction().commit();
				return false;
			}
			session.delete(user);
			session.getTransaction().commit();
		}
		return true;
	}
	
}
