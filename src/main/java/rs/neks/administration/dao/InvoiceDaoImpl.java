package rs.neks.administration.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Customer;
import rs.neks.administration.model.Invoice;

@Repository(value = "invoiceDao")
@Transactional
public class InvoiceDaoImpl extends CommonRepositoryImp<Invoice> implements InvoiceDao {
	
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
	public Invoice findFullyById(int id) {
		Invoice invoice = null;
		String HQL = "from rs.neks.administration.model.Invoice inv left join fetch inv.payments where inv.id = :id";
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
	public List<Invoice> findAll(LocalDateTime startDate, LocalDateTime endDate, Customer customer, boolean withPayments) {
		List<Invoice> invoices = null;		
		StringBuilder queryBuilder = new StringBuilder("from rs.neks.administration.model.Invoice inv")
//				.append(withPayments ? " join fetch inv.payments" : TextUtils.EMPTY)
				.append(" where 1 = 1");
		Optional.ofNullable(startDate).ifPresent(x -> queryBuilder.append(" and inv.createdOn >= :start_date"));
		Optional.ofNullable(endDate).ifPresent(x -> queryBuilder.append(" and inv.createdOn < :end_date"));
		Optional.ofNullable(customer).ifPresent(x -> queryBuilder.append(" and inv.customer = :customer"));
		queryBuilder.append(" order by inv.createdOn");			
		try {		
			Query query = sessionFactory.getCurrentSession().createQuery(queryBuilder.toString());
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
//				invoice.setCreatedOn(LocalDateTime.now());
				sessionFactory.getCurrentSession().persist(invoice);
			}
		} catch (Exception e) {
			System.err.println("Greska: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean checkIfInvoiceNoIsUnique(String invoiceNo) {
		String HQL = "select count(id) from rs.neks.administration.model.Invoice where invoiceNo = :invoice_no";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(HQL).setParameter("invoice_no", invoiceNo);
			int resutl = ((Number) query.getSingleResult()).intValue();
			return resutl == 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}