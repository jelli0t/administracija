/**
 * 
 */
package rs.neks.administration.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Customer;

/**
 * @author nemanja
 *
 */
@Repository(value = "customerDao")
public class CustomerDaoImpl extends CommonRepositoryImp<Customer> implements CustomerDao {

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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> findAll(String nameLike, boolean aciveOnly) {
		List<Customer> customers = null;
		String HQL = new StringBuilder("from rs.neks.administration.model.Customer where name like :starts_with")
				.append(aciveOnly ? " and active = 1" : "")
				.append(" order by name").toString();
		try {
			customers = sessionFactory.getCurrentSession().createQuery(HQL)
					.setParameter("starts_with", nameLike + "%")
					.getResultList();
		} catch (Exception e) {
			customers = new ArrayList<Customer>(0);
		}
		return customers;
	}

}
