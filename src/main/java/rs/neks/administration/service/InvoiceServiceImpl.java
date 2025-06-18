/**
 * 
 */
package rs.neks.administration.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.neks.administration.dao.InvoiceDao;
import rs.neks.administration.dao.PaymentDao;
import rs.neks.administration.model.Customer;
import rs.neks.administration.model.Invoice;
import rs.neks.administration.model.Payment;
import rs.neks.administration.util.TextUtils;

/**
 * @author nemanja
 *
 */
@Service(value = "invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	private InvoiceDao invoiceDao;
	
	@Autowired
	private PaymentDao paymentDao;
	

	@Override
	public Invoice findById(int id) {
		return invoiceDao.findById(id);
	}
	
	@Override
	public Invoice findLast() {
		return invoiceDao.findLast();
	}
	
	@Override
	public Invoice findFullyById(int id) {
		return invoiceDao.findFullyById(id);
	}

	@Override
	public Invoice findByNo(String invoiceNo) {
		if(TextUtils.isEmpty(invoiceNo)) {
			
			return null;
		}
		return invoiceDao.findByNo(invoiceNo);
	}
	
	@Override
	public List<Invoice> findAll(LocalDateTime startDate, LocalDateTime endDate, Customer customer, boolean withPayments) {
		return invoiceDao.findAll(startDate, endDate, customer, withPayments);
	}
	
	@Override
	public List<Invoice> findAllSortedByCustomer(LocalDateTime startDate, LocalDateTime endDate) {
		List<Invoice> invoices = invoiceDao.findAll(startDate, endDate, null, true);
		if(invoices != null && invoices.size() > 0) {
			invoices = invoices.stream()
					.sorted(Comparator.comparingInt(i -> i.getCustomer().getId()))
					.collect(Collectors.toList());
		}
		return invoices;
		
	}

	@Override
	public boolean save(Invoice invoice) {
		return invoiceDao.save(invoice);
	}

	@Override
	public boolean checkIfInvoiceNoIsUnique(String invoiceNo) {
		if(TextUtils.isEmpty(invoiceNo)) {
			
			return false;
		}
		return invoiceDao.checkIfInvoiceNoIsUnique(invoiceNo);
	}
	
	@Override
	public boolean remove(Invoice invoice) {
		if(invoice == null) {
			return false;
		}
		return invoiceDao.remove(invoice);
	}
	
	
	@Override
	public Payment findPaymentById(Integer paymentId) {
		if(paymentId == null) {
			return null;
		}
		return paymentDao.findById(paymentId);
	}
	
	@Override
	public boolean savePayment(Payment payment) {
		if(payment == null) {
			return false;
		}
		return paymentDao.save(payment);
	}
	
	@Override
	public boolean removePaymentById(Integer paymentId) {
		if(paymentId == null) {
			// TODO loggs
			return false;
		}
		Payment payment = paymentDao.findById(paymentId);
		return paymentDao.remove(payment);
	}
}
