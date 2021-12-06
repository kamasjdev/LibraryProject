package repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionManager {
	private static SessionFactory sessionFactory;
	
	public static void createSessionManager(String fileName) {
		sessionFactory = new Configuration().configure(fileName).buildSessionFactory();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
