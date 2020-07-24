package rs.neks.administration.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Customer;
import rs.neks.administration.model.Invoice;
import rs.neks.administration.util.TextUtils;

@Repository(value = "invoiceDao")
public class InvoiceDaoImpl implements InvoiceDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	

	public InvoiceDaoImpl(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Invoice findById(int id) {
		Invoice invoice = null;
		String HQL = "from rs.neks.administration.model.Invoice where id = :id";
		try {		
			invoice = (Invoice) sessionFactory.getCurrentSession()
					.createQuery(HQL).setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return invoice;
	}

	@Override
	public Invoice findByNo(String invoiceNo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public List<Invoice> findAll(LocalDateTime startDate, LocalDateTime endDate, Customer customer) {
		List<Invoice> invoices = null;
		String HQL = "from rs.neks.administration.model.Invoice where 1 = 1"
				+ Optional.ofNullable(startDate).map(x -> " and createdOn >= :start_date").orElse(TextUtils.EMPTY_STRING)
				+ Optional.ofNullable(endDate).map(x -> " and createdOn < :end_date").orElse(TextUtils.EMPTY_STRING)
				+ Optional.ofNullable(customer).map(x -> " and customer = :customer").orElse(TextUtils.EMPTY_STRING)
				+ " order by createdOn";
		try {		
			Query query = sessionFactory.getCurrentSession().createQuery(HQL);
			Optional.ofNullable(startDate).ifPresent(x -> { query.setParameter("start_date", x); });
			Optional.ofNullable(endDate).ifPresent(x -> { query.setParameter("end_date", x); });
			Optional.ofNullable(customer).ifPresent(x -> { query.setParameter("customer", x); });
			invoices = query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			invoices = new ArrayList<Invoice>(0);
		}	
		return invoices;
	}
	

	@Override
	public boolean save(Invoice invoice) {
		if(invoice == null) {
			return false;
		}
		try {
			if(invoice.getId() != null) {
				invoice.setModifiedOn(LocalDateTime.now());
				sessionFactory.getCurrentSession().merge(invoice);
			} else {
				invoice.setCreatedOn(LocalDateTime.now());
				sessionFactory.getCurrentSession().persist(invoice);
			}
		} catch (Exception e) {
			System.err.println("Greska: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}