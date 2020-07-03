/**
 * 
 */
package rs.neks.administration.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rs.neks.administration.model.Customer;
import rs.neks.administration.service.CustomerService;
import rs.neks.administration.util.Notification;

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
	
	@RequestMapping(path = {"/add", "/{id}/edit"}, method = RequestMethod.GET)
	public String prepareCustomerEdit(@PathVariable Optional<Integer> id, Model model) {
		Customer customer = id.map(x -> customerService.findById(x))
				.orElse(new Customer());				
		if(!model.containsAttribute("customer")) {
			model.addAttribute("customer", customer);
		}		
		return "fragment/customer :: edit";
	}
	
	
	@RequestMapping(path = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult, Model model, 
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("customer", customer);
			bindingResult.getModel().entrySet().forEach(es -> {
				model.addAttribute(es.getKey(), es.getValue());
			});
			return prepareCustomerEdit(Optional.empty(), model);
		}
		boolean result = customerService.save(customer);
		Notification notification = new Notification(result, "Uspesno ste sacuvali podatke o kupcu", null);
		redirectAttributes.addFlashAttribute("notification", notification);
		return "redirect:/customers";
	}
	
}
