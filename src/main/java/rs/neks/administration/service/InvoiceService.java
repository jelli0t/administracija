/**
 * 
 */
package rs.neks.administration.service;

import java.time.LocalDateTime;
import java.util.List;

import rs.neks.administration.model.Customer;
import rs.neks.administration.model.Invoice;

/**
 * @author nemanja
 *
 */
public interface InvoiceService {

	/**
	 * Pronalazi Customera za zadati ID.
	 * 
	 * @param id
	 * */
	public Invoice findById(int id);
	
	/**
	 * Pronalazi Fakturu za zadati br.
	 * 
	 * @param broj fakture
	 * */
	public Invoice findByNo(String invoiceNo);	
		
	/**
	 * 
	 * */
	public List<Invoice> findAll(LocalDateTime startDate, LocalDateTime endDate, Customer customer);
	
	/**
	 * Cuva novu instancu ili radi merge za postojecu.
	 * 
	 * @param customer
	 * */
	public boolean save(Invoice invoice);
	
	/**
	 * Proverava da li je prosledjeni broj fakture unikatan.
	 * 
	 * @param invoiceNo
	 * */
	public boolean checkIfInvoiceNoIsUnique(String invoiceNo);
}
