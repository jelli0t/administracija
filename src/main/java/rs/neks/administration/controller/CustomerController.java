/**
 * 
 */
package rs.neks.administration.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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
	
	@RequestMapping(path = "/overview", method = RequestMethod.GET)
	public String customersOverview(Model model) {
		List<Customer> customers = customerService.findAll(true);
		model.addAttribute("customers", customers);
		return "fragment/customer :: overview";
	}
	
	
	@RequestMapping(path = "/search/{first_letter}", method = RequestMethod.GET)
	public String searchByFirstLetter(@PathVariable("first_letter") String firstLetter, Model model) {
		List<Customer> customers = customerService.findAll(firstLetter, false);
		model.addAttribute("customers", customers);
		model.addAttribute("searchParam", firstLetter);
		return "customers";
	}
	
	
	@RequestMapping(path = {"/add", "/{id}/edit"}, method = RequestMethod.GET)
	public ModelAndView prepareCustomerEdit(@PathVariable Optional<Integer> id, ModelMap model) {
		Customer customer = id.map(x -> customerService.findById(x))
				.orElse(new Customer());				
		if(!model.containsAttribute("customer")) {
			model.addAttribute("customer", customer);
		}
		return new ModelAndView("fragment/customer :: edit", model, (HttpStatus) model.getAttribute("httpStatus"));
	}
	
	
	@RequestMapping(path = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saveCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult, ModelMap modelMap, 
			RedirectAttributes redirectAttributes) {		
		if (bindingResult.hasErrors()) {
			modelMap.addAttribute("customer", customer);
			modelMap.addAttribute("httpStatus", HttpStatus.BAD_REQUEST);
			modelMap.addAllAttributes(bindingResult.getModel());
			return prepareCustomerEdit(Optional.empty(), modelMap);
		}
		
		boolean result = customerService.save(customer);
		Notification notification = new Notification(result, "Uspesno ste sacuvali podatke o kupcu", null);
		redirectAttributes.addFlashAttribute("notification", notification);
		redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
		return new ModelAndView("redirect:/customers/overview");
	}
	
}
