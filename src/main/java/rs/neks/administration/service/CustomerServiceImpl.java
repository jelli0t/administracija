package rs.neks.administration.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.neks.administration.dao.CustomerDao;
import rs.neks.administration.model.Customer;

@Service(value = "customerService")
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public Customer findById(int id) {
		return customerDao.findById(id);
	}

	
	@Override
	public List<Customer> findAll(boolean aciveOnly) {
		return customerDao.findAll(aciveOnly);
	}
	
	@Override
	public List<Customer> findAllInvoicesOwners(LocalDateTime from, LocalDateTime to) {
		return customerDao.findAllInvoicesOwners(from, to);
	}

	@Override
	public boolean save(Customer customer) {
		if(customer == null)
			return false;
		return customerDao.save(customer);
	}


	@Override
	public List<Customer> findAll(String nameLike, boolean aciveOnly) {
		return customerDao.findAll(nameLike, aciveOnly);
	}
}