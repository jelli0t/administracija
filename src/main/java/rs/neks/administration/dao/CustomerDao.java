package rs.neks.administration.dao;

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
	 * 
	 * */
	public boolean save(Customer customer);
}
