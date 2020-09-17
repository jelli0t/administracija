package rs.neks.administration.security.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import rs.neks.administration.dao.CommonRepositoryImp;
import rs.neks.administration.security.model.Operator;
import rs.neks.administration.util.TextUtils;

@Repository("operatorDao")
public class OperatorDaoImpl extends CommonRepositoryImp<Operator> implements OperatorDao {


	@Override
	public Operator findByUsername(String username) {
		if(TextUtils.isEmpty(username)) {
			return null;
		}		
		try {
			Query query = sessionFactory.getCurrentSession()
					.createQuery("from rs.neks.administration.security.model.Operator where lower(username) = :username")
					.setParameter("username", username.trim().toLowerCase());
			Operator operator = (Operator) query.getSingleResult();
			return operator;
		} catch (Exception e) {
			
		}
		return null;
	}

	
	@Override
	public List<Operator> findAll(boolean activeOnly) {
		List<Operator> operators = null;
		String HQL = new StringBuilder("from rs.neks.administration.security.model.Operator where 1=1")
				.append(activeOnly ? " and active = 1" : TextUtils.EMPTY)
				.append(" order by username")
				.toString();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(HQL);
			operators = query.getResultList();
		} catch (Exception e) {
			operators = new ArrayList<>(0);
		}
		return operators;
	}
}
