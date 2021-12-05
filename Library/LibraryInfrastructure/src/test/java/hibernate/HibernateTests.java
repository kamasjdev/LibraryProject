package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;

import entities.Author;

public class HibernateTests {
	StandardServiceRegistry standardServiceRegistry;
	Metadata metadata;
	SessionFactory sessionFactory;
	
	public HibernateTests() {
		standardServiceRegistry = new StandardServiceRegistryBuilder().configure("hibernateTest.cfg.xml").build();
		metadata = new MetadataSources(standardServiceRegistry).getMetadataBuilder().build();  
		sessionFactory =  metadata.getSessionFactoryBuilder().build();
	}
	
	@Test
	public void given_valid_entity_should_add_to_database() {
		Session session = sessionFactory.openSession();  
		Transaction transaction = session.beginTransaction();
		
		Author author = new Author();    
	    author.person.firstName = "Tester1";
	    author.person.lastName = "Tester2";
	        
	    session.save(author);  
	    transaction.commit();  
	    System.out.println("successfully saved");    
	    sessionFactory.close();  
	    session.close();    
	}
}
