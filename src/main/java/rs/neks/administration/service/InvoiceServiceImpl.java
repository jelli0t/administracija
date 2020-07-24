/**
 * 
 */
package rs.neks.administration.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.neks.administration.dao.InvoiceDao;
import rs.neks.administration.model.Customer;
import rs.neks.administration.model.Invoice;
import rs.neks.administration.util.TextUtils;

/**
 * @author nemanja
 *
 */
@Service(value = "invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	private InvoiceDao invoiceDao;
	

	@Override
	public Invoice findById(int id) {
		return invoiceDao.findById(id);
	}

	@Override
	public Invoice findByNo(String invoiceNo) {
		if(TextUtils.isEmpty(invoiceNo)) {
			
			return null;
		}
		return invoiceDao.findByNo(invoiceNo);
	}
	
	@Override
	public List<Invoice> findAll(LocalDateTime startDate, LocalDateTime endDate, Customer customer) {
		return invoiceDao.findAll(startDate, endDate, customer);
	}

	@Override
	public boolean save(Invoice invoice) {
		return invoiceDao.save(invoice);
	}

}
