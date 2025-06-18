/**
 * 
 */
package rs.neks.administration.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Customer;

/**
 * @author nemanja
 *
 */
@SuppressWarnings("unchecked")
@Repository(value = "customerDao")
public class CustomerDaoImpl extends CommonRepositoryImp<Customer> implements CustomerDao {

	@Override
	public Customer findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
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
	public List<Customer> findAllInvoicesOwners(LocalDateTime from, LocalDateTime to) {
		List<Customer> customers = null;		
		StringBuilder queryBuilder = new StringBuilder("select distinct inv.customer from rs.neks.administration.model.Invoice inv where 1=1");
		Optional.ofNullable(from).ifPresent(x -> queryBuilder.append(" and inv.createdOn >= :from"));
		Optional.ofNullable(to).ifPresent(x -> queryBuilder.append(" and inv.createdOn < :to"));
		queryBuilder.append(" order by inv.customer.name");			
		try {		
			Query query = sessionFactory.getCurrentSession().createQuery(queryBuilder.toString());
			Optional.ofNullable(from).ifPresent(x -> { query.setParameter("from", x); });
			Optional.ofNullable(to).ifPresent(x -> { query.setParameter("to", x); });
			customers = query.getResultList();
		} catch (Exception e) {
			customers = new ArrayList<Customer>(0);
		}
		return customers;
	}
	
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
