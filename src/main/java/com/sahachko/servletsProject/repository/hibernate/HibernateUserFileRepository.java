package com.sahachko.servletsProject.repository.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.hibernate.Session;

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
			file = null;
		}
		return file;
	}

	@Override
	public boolean deleteById(Integer id) {
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			UserFile file = session.get(UserFile.class, id);
			if(file == null) {
				session.getTransaction().commit();
				return false;
			}
			session.delete(file);
			session.getTransaction().commit();
		}
		return true;
	}
		
}
