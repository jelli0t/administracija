package rs.neks.administration.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Idable;

@Repository
public class CommonRepositoryImp<T extends Idable> implements CommonRepository<T> {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public T findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public boolean save(T t) {
		try {
			if(t.getId() == null) {
				sessionFactory.getCurrentSession().persist(t);
			} else {
				sessionFactory.getCurrentSession().merge(t);
			}
		} catch (Exception e) {
			return false;
		}		
		return true;
	}

}
