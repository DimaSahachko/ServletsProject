package com.sahachko.servletsProject.repository.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.hibernate.Session;

import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.UserFileRepository;

public class HibernateUserFileRepository implements UserFileRepository {

	@Override
	public List<UserFile> getAll() {
		List<UserFile> allUsersFiles = new ArrayList<>();
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			allUsersFiles = session.createQuery("from UserFile", UserFile.class).getResultList();
			session.getTransaction().commit();
		}
		return allUsersFiles;
	}

	@Override
	public UserFile getById(Integer id) {
		UserFile file = null;
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			file = session.get(UserFile.class, id);
			session.getTransaction().commit();
		}
		if(file == null) {
			throw new ResourceNotFoundException("There is no file with such id");
		}
		return file;
	}

	@Override
	public UserFile save(UserFile file) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			file.setCreated(new Date());
			session.save(file);
			session.getTransaction().commit();
		}
		return file;
	}

	@Override
	public UserFile update(UserFile file) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			file.setUpdated(new Date());
			session.update(file);
			session.getTransaction().commit();
		} catch(OptimisticLockException exc) {
			throw new ResourceNotFoundException("There is no file with such id");
		}
		return file;
	}

	@Override
	public void deleteById(Integer id) {
		UserFile file = getById(id);
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(file);
			session.getTransaction().commit();
		}
	}
		
}
