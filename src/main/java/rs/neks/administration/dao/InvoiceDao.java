/**
 * 
 */
package rs.neks.administration.dao;

import java.time.LocalDateTime;
import java.util.List;

import rs.neks.administration.model.Customer;
import rs.neks.administration.model.Invoice;

/**
 * @author nemanja
 *
 */
public interface InvoiceDao {

	/**
	 * Pronalazi Customera za zadati ID.
	 * 
	 * @param id
	 * */
	public Invoice findById(int id);
	
	
	public Invoice findFullyById(int id);

	/**
	 * Pronalazi Fakturu za zadati br.
	 * 
	 * @param broj fakture
	 * */
	public Invoice findByNo(String invoiceNo);	
	
	/**
	 * Dohvata listu faktura po zadatim kriterijumima.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param customer
	 * */
	public List<Invoice> findAll(LocalDateTime startDate, LocalDateTime endDate, Customer customer);
	
	/**
	 * Cuva novu instancu ili radi merge za postojecu.
	 * 
	 * @param customer
	 * */
	public boolean save(Invoice customer);

	/**
	 * Proverava da li je prosledjeni broj fakture unikatan.
	 * 
	 * @param invoiceNo
	 * */
	public boolean checkIfInvoiceNoIsUnique(String invoiceNo);
}
