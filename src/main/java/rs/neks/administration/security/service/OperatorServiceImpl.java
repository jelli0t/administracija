package rs.neks.administration.security.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rs.neks.administration.security.dao.OperatorDao;
import rs.neks.administration.security.model.Operator;
import rs.neks.administration.util.TextUtils;

@Service("operatorService")
public class OperatorServiceImpl implements UserDetailsService, OperatorService {

	@Autowired
	private OperatorDao operatorDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(TextUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("");
		}		
		Operator operator = operatorDao.findByUsername(username);
		if(operator == null) {
			throw new UsernameNotFoundException("login.exception.userNotFound");
		}					
		return operator;
	}
	
	@Override
	public List<Operator> findAll(boolean activeOnly) {
		return operatorDao.findAll(activeOnly);
	}

	@Override
	public Operator findById(Integer id) {
		return operatorDao.findById(id);
	}

	@Override
	public boolean save(Operator operator) {
		if(operator != null && operator.getId() == null) {
			operator.setCreatedOn(LocalDateTime.now());
		}
		return operatorDao.save(operator);
	}
}
