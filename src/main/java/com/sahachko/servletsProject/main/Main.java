package com.sahachko.servletsProject.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.model.AccountStatus;
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.model.EventAction;
import com.sahachko.servletsProject.model.FileStatus;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;
import com.sahachko.servletsProject.repository.AccountRepository;
import com.sahachko.servletsProject.repository.EventRepository;
import com.sahachko.servletsProject.repository.UserRepository;
import com.sahachko.servletsProject.repository.hibernate.HibernateAccountRepository;
import com.sahachko.servletsProject.repository.hibernate.HibernateConnectionUtils;
import com.sahachko.servletsProject.repository.hibernate.HibernateEventRepository;
import com.sahachko.servletsProject.repository.hibernate.HibernateUserFileRepository;
import com.sahachko.servletsProject.repository.hibernate.HibernateUserRepository;

public class Main {
	static Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	public static void main(String[] args) {
		Account account1 = new Account("Dima", "Sahachko", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		Account account2 = new Account("Jane", "Sahachko", new GregorianCalendar(1993, Calendar.DECEMBER, 15).getTime(), AccountStatus.REGULAR);
		HibernateUserRepository repo = new HibernateUserRepository();
		AccountRepository accRepo = new HibernateAccountRepository();
		accRepo.save(account1);
		accRepo.save(account2);
		User user = new User();
		user.setLogin("rer");
		Account fakeAcc = new Account("aks", "asdas", new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime(), AccountStatus.REGULAR);
		fakeAcc.setId(5);
		fakeAcc.setSignUpDate(new GregorianCalendar(1994, Calendar.JANUARY, 22).getTime());
		user.setAccount(fakeAcc);
		user = repo.save(user);
		System.out.println(user);
	}
}
