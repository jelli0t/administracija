/**
 * 
 */
package rs.neks.administration.dao;

import rs.neks.administration.model.Idable;

/**
 * @author nemanja
 *
 */
public interface CommonRepository<T extends Idable> {
	
	/**
	 * Pronalazi Entitet za zadati ID.
	 * 
	 * @param id
	 * */
	public T findById(int id);
	
	/**
	 * Pronalazi poslednje uneseni entitet. Radi sort po ID.
	 * 
	 * @return t
	 * */
	public T findLast();
	
	/**
	 * Radi save/update
	 * 
	 * @param t
	 * */
	public boolean save(T t);
	
	/**
	 * uklanja prosledjeni entitet.
	 * 
	 * @param t
	 * */
	public boolean remove(T t);

}
