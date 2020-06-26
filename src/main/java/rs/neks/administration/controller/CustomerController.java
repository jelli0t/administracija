/**
 * 
 */
package rs.neks.administration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.neks.administration.model.Customer;
import rs.neks.administration.service.CustomerService;

/**
 * @author nemanja
 *
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {	
	
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String customersHomepage(Model model) {
		List<Customer> customers = customerService.findAll(true);
		model.addAttribute("customers", customers);
		return "customers";
	}
	
	
	@RequestMapping(path = "/{id}/edit", method = RequestMethod.GET)
	public String prepareCustomerEdit(@PathVariable int id, Model model) {
		Customer customer = id > 0 
				? customerService.findById(id) 
				: new Customer();
		model.addAttribute("customer", customer);
		return "fragment/customer.html :: edit";
	}
	
	
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	public String saveCustomer(@ModelAttribute Customer customer, Model model) {
		boolean result = customerService.save(customer);
		return customersHomepage(model);
	}
	
}
