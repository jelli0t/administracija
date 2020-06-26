/**
 * 
 */
package rs.neks.administration.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author nemanja
 *
 */
@Configuration
@ComponentScan(basePackages = {"rs.neks.administration.dao", "rs.neks.administration.service"})
@PropertySource({"classpath:datasource.properties"})
@EnableTransactionManagement
public class DatasourceConfiguration {
	
	@Autowired
	private Environment env;
	
	@Bean(name = "dataSource")
	public DataSource getDataSource(){
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("mysql.driver.classname"));
		dataSource.setUrl(env.getProperty("mysql.url"));
		dataSource.setUsername(env.getProperty("mysql.username"));
		dataSource.setPassword(env.getProperty("mysql.password"));
		return dataSource;
	}
	
	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource){
		LocalSessionFactoryBuilder sessionFactory = new LocalSessionFactoryBuilder(dataSource);
		sessionFactory.scanPackages("rs.neks.administration.model");
		sessionFactory.addProperties(getHibernateProperties());
		return sessionFactory.buildSessionFactory();
	}
	
	
	
	@Autowired
	@Bean(name="transactionManager")
	HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory){
		HibernateTransactionManager txManager = new HibernateTransactionManager(sessionFactory);
		return txManager;
	}
	
	
	
	private Properties getHibernateProperties(){
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.connection.CharSet", "utf8");
		hibernateProperties.setProperty("hibernate.connection.characterEncoding", "utf8");
		hibernateProperties.setProperty("hibernate.connection.useUnicode", "true");
//		hibernateProperties.setProperty("hibernate.show_sql", "true");
//		hibernateProperties.setProperty("hibernate.format_sql", "true");
		return hibernateProperties;	
	}	
		
}
