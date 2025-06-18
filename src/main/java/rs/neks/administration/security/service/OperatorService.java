/**
 * 
 */
package rs.neks.administration.security.service;

import java.util.List;

import rs.neks.administration.security.model.Operator;

/**
 * @author jelles
 *
 */
public interface OperatorService {
	
	/**
	 * 
	 * */
	public Operator findById(Integer id);
	
	/**
	 * Finds all Operators 
	 * 
	 * @param activeOnlyc
	 * */
	public List<Operator> findAll(boolean activeOnly);
	
	public boolean save(Operator operator);

}
