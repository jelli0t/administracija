/**
 * 
 */
package rs.neks.administration.dao;

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

	@Override
	public boolean save(Customer customer) {
		if(customer == null) {
			return false;
		}
		try {
			if(customer.getId() != null) {
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
