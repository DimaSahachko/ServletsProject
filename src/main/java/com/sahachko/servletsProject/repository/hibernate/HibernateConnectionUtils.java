package com.sahachko.servletsProject.repository.hibernate;

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
			if(sessionFactory == null) {
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
				Metadata metadata = new MetadataSources(serviceRegistry)
						.addAnnotatedClass(Account.class)
						.addAnnotatedClass(UserFile.class)
						.addAnnotatedClass(Event.class)
						.addAnnotatedClass(User.class)
						.buildMetadata();
				sessionFactory = metadata.buildSessionFactory();
			}
			return sessionFactory;
		}
		
		public static void closeSessionFactory() {
			if(sessionFactory != null) {
				sessionFactory.close();
			}
		}
}
