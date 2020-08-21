/**
 * 
 */
package rs.neks.administration.controller;

import java.time.LocalDateTime;
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
import rs.neks.administration.model.Invoice;
import rs.neks.administration.service.CustomerService;
import rs.neks.administration.service.InvoiceService;
import rs.neks.administration.util.Notification;

/**
 * @author nemanja
 *
 */
@Controller
@RequestMapping("/invoices")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String customersHomepage(Model model) {
		List<Invoice> invoices = invoiceService.findAll(LocalDateTime.of(2020, 1, 1, 0, 0), null, null);
		model.addAttribute("invoices", invoices);
		return "invoices";
	}

	@RequestMapping(path = "/overview", method = RequestMethod.GET)
	public String prepareInvoiceOverview(Model model) {
		List<Invoice> invoices = invoiceService.findAll(LocalDateTime.of(2020, 1, 1, 0, 0), null, null);
		model.addAttribute("invoices", invoices);
		return "fragment/invoice :: overview";
	}
	
	
	@RequestMapping(path = {"/add", "/{id}/edit"}, method = RequestMethod.GET)
	public ModelAndView prepareInvoiceEdit(@PathVariable Optional<Integer> id, ModelMap model) {
		Invoice invoice = id.map(x -> invoiceService.findById(x)).orElse(new Invoice());
		List<Customer> customers = customerService.findAll(true);
		model.addAttribute("customers", customers);
		if(!model.containsAttribute("invoice")) {
			model.addAttribute("invoice", invoice);
		}
		return new ModelAndView("fragment/invoice :: edit", model, (HttpStatus) model.getAttribute("httpStatus"));
	}
	
	
	@RequestMapping(path = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saveInvoice(@Valid @RequestBody Invoice invoice, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes, ModelMap modelMap) {
		if (bindingResult.hasErrors()) {
			modelMap.addAttribute("invoice", invoice);
			modelMap.addAttribute("httpStatus", HttpStatus.BAD_REQUEST);
			modelMap.addAllAttributes(bindingResult.getModel());
			return prepareInvoiceEdit(Optional.empty(), modelMap);
		}
//		else if(invoice.getId() == null && TextUtils.notEmpty(invoice.getInvoiceNo())) {
//			boolean isInvoiceNoUnique = invoiceService.checkIfInvoiceNoIsUnique(invoice.getInvoiceNo());
//			if(!isInvoiceNoUnique) {
//				bindingResult.addError( new ObjectError("invoiceNo", "Faktura pod ovim brojem vec postoji!"));
//				model.addAttribute("invoiceNo", "Faktura pod ovim brojem vec postoji!");
//				return prepareInvoiceEdit(Optional.empty(), model);
//			}				
//		}
		boolean result = invoiceService.save(invoice);
		Notification notification = new Notification(result, "Uspesno ste sacuvali podatke o kupcu", null);
		redirectAttributes.addFlashAttribute("notification", notification);
		redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
		return new ModelAndView("redirect:/invoices/overview");
	}
}
