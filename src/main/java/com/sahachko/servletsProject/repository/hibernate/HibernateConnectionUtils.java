package com.sahachko.servletsProject.repository.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import com.sahachko.servletsProject.model.Account;
import com.sahachko.servletsProject.model.Event;
import com.sahachko.servletsProject.model.User;
import com.sahachko.servletsProject.model.UserFile;

public class HibernateConnectionUtils {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").applySettings(getEnvUrl()).build();
			Metadata metadata = new MetadataSources(serviceRegistry).addAnnotatedClass(Account.class)
					.addAnnotatedClass(UserFile.class).addAnnotatedClass(Event.class).addAnnotatedClass(User.class)
					.buildMetadata();
			sessionFactory = metadata.buildSessionFactory();
		}
		return sessionFactory;
	}

	public static void closeSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
	
	private static Map<String, String> getEnvUrl() {
		Map<String, String> connectionSettings = new HashMap<>();
		String url = System.getenv("JDBC_DATABASE_URL");
		String username = System.getenv("JDBC_DATABASE_USERNAME");
		String password = System.getenv("JDBC_DATABASE_PASSWORD");
		if(null != url) {
			connectionSettings.put("hibernate.connection.url", url);
		}
		if(null != username) {
			connectionSettings.put("hibernate.connection.username", username);
		}
		if(null != password) {
			connectionSettings.put("hibernate.connection.password", password);
		}
		return connectionSettings;
	}
}
