package com.sahachko.servletsProject.repository.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.hibernate.Session;

import com.sahachko.servletsProject.exceptions.ResourceNotFoundException;
import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.repository.AccountRepository;

public class HibernateAccountRepository implements AccountRepository {

	@Override
	public List<Account> getAll() {
		List<Account> allAccounts = new ArrayList<>();
		try (Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			allAccounts = session.createQuery("from Account", Account.class).getResultList();
			session.getTransaction().commit();
		}
		return allAccounts;
	}

	@Override
	public Account getById(Integer id) {
		Account account = null;
		try(Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			account = session.get(Account.class, id);
			session.getTransaction().commit();
		}
		if (account == null) {
			throw new ResourceNotFoundException("There is no account with such id");
		}
		return account;
	}

	@Override
	public Account save(Account account) {
		try (Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			account.setSignUpDate(new Date());
			session.save(account);
			session.getTransaction().commit();
		}
		return account;
	}

	@Override
	public Account update(Account account) {
		try (Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(account);
			session.getTransaction().commit();
		} catch (OptimisticLockException exc) {
			throw new ResourceNotFoundException("There is no account with such id");
		}
		return account;
	}

	@Override
	public void deleteById(Integer id) {
		Account account = getById(id);
		try (Session session = HibernateConnectionUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(account);
			session.getTransaction().commit();
		}
	}

}
