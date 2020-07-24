/**
 * 
 */
package rs.neks.administration.dao;


/**
 * @author nemanja
 *
 */
public interface CommonRepository<T> {
	
	/**
	 * Pronalazi Entitet za zadati ID.
	 * 
	 * @param id
	 * */
	public T findById(int id);	
	
	/**
	 * Radi save/update
	 * 
	 * @param t
	 * */
	public boolean save(T t);

}
