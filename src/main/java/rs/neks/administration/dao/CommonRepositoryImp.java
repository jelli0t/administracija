package rs.neks.administration.dao;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Idable;

@Repository
@Transactional
public class CommonRepositoryImp<T extends Idable> implements CommonRepository<T> {

	private Class<T> entityClass;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public CommonRepositoryImp() {
		super();
		this.entityClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), CommonRepository.class);
	}

	@Override
	public T findById(int id) {
		T t = sessionFactory.getCurrentSession().get(entityClass, id);
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findLast() {
		T t = null;
		try {
			t = (T) sessionFactory.getCurrentSession()
					.createQuery("from "+ entityClass.getName() +" order by id desc")
					.setMaxResults(1)
					.getSingleResult();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}
	
	
	@Override
	public boolean save(T t) {
		try {
			if (t.getId() == null) {
				sessionFactory.getCurrentSession().persist(t);
			} else {
				sessionFactory.getCurrentSession().merge(t);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean remove(T t) {
		if (t == null) {
			return false;
		}
		try {
			sessionFactory.getCurrentSession().delete(t);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
