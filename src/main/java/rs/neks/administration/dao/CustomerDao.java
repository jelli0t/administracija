package rs.neks.administration.dao;

import java.time.LocalDateTime;
import java.util.List;

import rs.neks.administration.model.Customer;

public interface CustomerDao extends CommonRepository<Customer> {

	/**
	 * Pronalazi Customera za zadatu sifru kupca.
	 * 
	 * @param code
	 * */
	public Customer findByCode(String code);
	
	/**
	 * Pronalazi listu Customer-a.<br>
	 * [opciono] mozemo da zadamo flag da trazi samo aktivne.
	 * 
	 * @param aciveOnly
	 * */
	public List<Customer> findAll(boolean aciveOnly);
	
	/**
	 * Pronalazi listu Customer-a.<br>
	 * [opciono] mozemo da zadamo flag da trazi samo aktivne.
	 * 
	 * @param nameLike
	 * @param aciveOnly
	 * */
	public List<Customer> findAll(String nameLike, boolean aciveOnly);

	/**
	 * Find all Customers which are invoice owners.
	 * 
	 * @param from
	 * @param to
	 * */
	public List<Customer> findAllInvoicesOwners(LocalDateTime from, LocalDateTime to);
	
}
