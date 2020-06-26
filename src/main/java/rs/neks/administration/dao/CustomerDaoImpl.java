/**
 * 
 */
package rs.neks.administration.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Customer;

/**
 * @author nemanja
 *
 */
@Repository(value = "customerDao")
@Transactional
public class CustomerDaoImpl implements CustomerDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public CustomerDaoImpl(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Customer findById(int id) {
		Customer customer = null;
		String HQL = "from rs.neks.administration.model.Customer where id = :id";
		try {		
			customer = (Customer) sessionFactory.getCurrentSession()
					.createQuery(HQL).setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return customer;
	}

	@Override
	public Customer findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> findAll(boolean aciveOnly) {
		List<Customer> customers = null;
		String HQL = new StringBuilder("from rs.neks.administration.model.Customer where 1=1")
				.append(aciveOnly ? " and active = 1" : "")
				.append(" order by name").toString();
		try {
			customers = sessionFactory.getCurrentSession().createQuery(HQL).getResultList();
		} catch (Exception e) {
			customers = new ArrayList<Customer>(0);
		}
		return customers;
	}

	
	@Override
	public boolean save(Customer customer) {
		if(customer == null) {
			return false;
		}
		try {
			if(customer.getId() != null) {
				customer.setModifiedOn(LocalDateTime.now());
				sessionFactory.getCurrentSession().merge(customer);
			} else {
				sessionFactory.getCurrentSession().persist(customer);	
			}
		} catch (Exception e) {
			System.err.println("Greska: "+e.getMessage());
			e.printStackTrace();
			return false;
		}		
		return true;
	}

}
