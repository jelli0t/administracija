package rs.neks.administration.test.customer;


import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import rs.neks.administration.conf.DatasourceConfiguration;
import rs.neks.administration.dao.CustomerDao;
import rs.neks.administration.model.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes = { DatasourceConfiguration.class }, 
		loader = AnnotationConfigContextLoader.class
	)
public class CustomerDaoTest {

	@Autowired
	private CustomerDao customerDao;
	

//	@Test
	public void createNewCustomerTest() {
		Customer customer = new Customer();
		customer.setName("NEKS d.o.o.");
		customer.setAltName("Proizvodno trgovinsko preduzece NEKS d.o.o.");
		customer.setCreatedOn( LocalDateTime.now() );
		customer.setModifiedOn( LocalDateTime.now() );
		customer.setPhone("011258654");
		customer.setCellPhone("0643641788");
		customer.setEmail("jelesijevicnemanja@gmail.com");
		
		
		boolean saved = customerDao.save(customer);
		System.out.println("Novi customer sacuvan!");
		
		assertTrue("", saved);
	}
	
	@Test
	public void findCustomerTest() {
		Customer customer = customerDao.findById(1);
		assertTrue("Pronadjen Customer", customer != null);
	}

}
