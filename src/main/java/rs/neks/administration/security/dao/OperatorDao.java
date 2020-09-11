package rs.neks.administration.security.dao;

import rs.neks.administration.dao.CommonRepository;
import rs.neks.administration.security.model.Operator;

public interface OperatorDao extends CommonRepository<Operator> {

	/**
	 * Find operator by his username
	 * 
	 * @param
	 * */
	public Operator findByUsername(String username);
	
}
