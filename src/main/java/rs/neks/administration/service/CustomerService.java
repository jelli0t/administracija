/**
 * 
 */
package rs.neks.administration.service;

import java.time.LocalDateTime;
import java.util.List;

import rs.neks.administration.model.Customer;

/**
 * @author nemanja
 *
 */
public interface CustomerService {
	
	/**
	 * Nalazi kupca prema ID.
	 * 
	 * @param id
	 * */
	public Customer findById(int id);
	
	/**
	 * Pronalazi listu Customer-a.<br>
	 * [opciono] mozemo da zadamo flag da trazi samo aktivne.
	 * 
	 * @param aciveOnly
	 * */
	public List<Customer> findAll(boolean aciveOnly);
	
	/**
	 * Find all Customers which are invoice owners.
	 * 
	 * @param from
	 * @param to
	 * */
	public List<Customer> findAllInvoicesOwners(LocalDateTime from, LocalDateTime to);
	
	/**
	 * Pronalazi listu Customer-a.<br>
	 * [opciono] mozemo da zadamo flag da trazi samo aktivne.
	 * 
	 * @param aciveOnly
	 * */
	public List<Customer> findAll(String nameLike, boolean aciveOnly);
		
	/**
	 * 
	 * */
	public boolean save(Customer customer);

}
