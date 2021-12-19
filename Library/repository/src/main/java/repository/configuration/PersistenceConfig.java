package repository.configuration;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.base.Preconditions;

import repository.AuthorRepository;
import repository.BillRepository;
import repository.BookAuthorRepository;
import repository.BookCustomerRepository;
import repository.BookRepository;
import repository.CustomerRepository;
import repository.implementation.AuthorRepositoryImpl;
import repository.implementation.BillRepositoryImpl;
import repository.implementation.BookAuthorRepositoryImpl;
import repository.implementation.BookCustomerRepositoryImpl;
import repository.implementation.BookRepositoryImpl;
import repository.implementation.CustomerRepositoryImpl;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence-maria-db.properties" })
@ComponentScan({ "entities", "repository" })
public class PersistenceConfig {
	
	@Autowired
    private Environment env;
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("entities");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());

        return em;
    }
    
    @Bean
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("jdbc.url")));
        dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("jdbc.user")));
        dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("jdbc.pass")));

        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean
    public AuthorRepository authorRepository() {
        return new AuthorRepositoryImpl();
    }
    
    @Bean
    public BillRepository billRepository() {
        return new BillRepositoryImpl();
    }
    
    @Bean
    public BookAuthorRepository bookAuthorRepository() {
        return new BookAuthorRepositoryImpl();
    }
    
    @Bean
    public BookCustomerRepository bookCustomerRepository() {
        return new BookCustomerRepositoryImpl();
    }
    
    @Bean
    public BookRepository bookRepository() {
        return new BookRepositoryImpl();
    }
    
    @Bean
    public CustomerRepository customerRepository() {
        return new CustomerRepositoryImpl();
    }
    
    private final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));

        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));

        // Envers properties
        hibernateProperties.setProperty("org.hibernate.envers.audit_table_suffix", env.getProperty("envers.audit_table_suffix"));

        return hibernateProperties;
    }
}
