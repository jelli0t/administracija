/**
 * 
 */
package rs.neks.administration.controller;

import java.beans.PropertyEditorSupport;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.InitBinder;
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
import rs.neks.administration.util.DateUtils;
import rs.neks.administration.util.Notification;
import rs.neks.administration.util.TextUtils;

/**
 * @author nemanja
 *
 */
@Controller
@RequestMapping("/invoices")
public class InvoiceController extends YearAware {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String defaultOverview(Model model) {
		int year = Optional.ofNullable( model.getAttribute("year") ).map(y -> (int) y).orElse(0);
		final LocalDateTime from = DateUtils.makeOrDefault(year, 0, 1);
		final LocalDateTime to = from.plusMonths(1);
		List<Invoice> invoices = invoiceService.findAll(from, to, null, false);
		model.addAttribute("invoices", invoices);
		model.addAttribute("month", from.getMonthValue());
		model.addAttribute("year", from.getYear());
		return "invoices";
	}
	
	
	@RequestMapping(path = "/id/{invoiceId}", method = RequestMethod.GET)
	public String invoiceOverview(@PathVariable Integer invoiceId, Model model) {
		Invoice invoice = invoiceService.findById(invoiceId);
		if(invoice != null) {			
			model.addAttribute("invoices", Arrays.asList( invoice ));
			model.addAttribute("month", invoice.getCreatedOn().getMonthValue());
			model.addAttribute("year", invoice.getCreatedOn().getYear());
		}
		return "invoices";
	}
	
	
	@RequestMapping(path = {"/overview", "/overview/{year}/{month}"}, method = RequestMethod.GET)
	public String prepareInvoiceOverview(
			@PathVariable Optional<Integer> year, 
			@PathVariable Optional<Integer> month, 
			Model model) {
		final LocalDateTime from = DateUtils.makeOrDefault(year.orElse(0), month.orElse(0), 1);
		final LocalDateTime to = from.plusMonths(1);
		List<Invoice> invoices = invoiceService.findAll(from, to, null, false);
		model.addAttribute("invoices", invoices);
		model.addAttribute("month", from.getMonthValue());
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
	
	
	@RequestMapping(path = "/{id}/remove", method = RequestMethod.GET)
	public String prepareRemoving(@PathVariable Integer id, Model model) {
		if(id != null) {
			Invoice invoice = invoiceService.findById(id);
			model.addAttribute("invoice", invoice);
		}
		return "fragment/invoice :: remove";
	}
	
	
	@RequestMapping(path = "/remove", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ModelAndView removeInvoice(@RequestBody Invoice invoice, RedirectAttributes redirectAttributes, ModelMap modelMap) {
		StringBuilder uriBuilder = new StringBuilder("redirect:/invoices/overview");
		if(invoice != null && invoice.getId() != null) {
			invoice = invoiceService.findById(invoice.getId());
			boolean result = invoiceService.remove(invoice);
			final LocalDateTime created = invoice.getCreatedOn();
			Notification notification = new Notification(result, "Uspesno ste sacuvali podatke o kupcu", null);
			redirectAttributes.addFlashAttribute("notification", notification);
			redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
			Optional.ofNullable(created).ifPresent(c -> uriBuilder.append("/").append(c.getYear()));
			Optional.ofNullable(created).ifPresent(c -> uriBuilder.append("/").append(c.getMonthValue()));
		}
		return new ModelAndView(uriBuilder.toString());
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
		boolean result = invoiceService.save(invoice);
		final LocalDateTime created = invoice.getCreatedOn();	
		Notification notification = new Notification(result, "Uspesno ste sacuvali podatke o kupcu", null);
		redirectAttributes.addFlashAttribute("invoice", invoice);
		redirectAttributes.addFlashAttribute("notification", notification);
		redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
		StringBuilder uriBuilder = new StringBuilder("redirect:/invoices/overview");
		Optional.ofNullable(created).ifPresent(c -> uriBuilder.append("/").append(c.getYear()));
		Optional.ofNullable(created).ifPresent(c -> uriBuilder.append("/").append(c.getMonthValue()));
		return new ModelAndView(uriBuilder.toString());
	}
	
	
	@InitBinder(value = "invoice")
	protected void initBinder(org.springframework.web.bind.WebDataBinder binder) {
		binder.registerCustomEditor(Double.class, "totalAmount", new PropertyEditorSupport() {
			
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if(TextUtils.notEmpty(text)) {
					Double value = null;
					System.out.println("Uneta vrednost: " + text);
					
					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setDecimalSeparator(',');
					symbols.setGroupingSeparator('.');
					DecimalFormat decimalFormat = new DecimalFormat("#,##0.###", symbols);					
					try {
						value = decimalFormat.parse(text).doubleValue();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					setValue(value);
				}
			}
		});
	}
}
