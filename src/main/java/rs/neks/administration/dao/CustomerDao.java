package rs.neks.administration.dao;

import java.util.List;

import rs.neks.administration.model.Customer;

public interface CustomerDao {

	/**
	 * Pronalazi Customera za zadati ID.
	 * 
	 * @param id
	 * */
	public Customer findById(int id);

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
	 * 
	 * */
	public boolean save(Customer customer);
}
