/**
 * 
 */
package rs.neks.administration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import rs.neks.administration.model.Payment;
import rs.neks.administration.service.CustomerService;
import rs.neks.administration.service.InvoiceService;
import rs.neks.administration.util.DateUtils;
import rs.neks.administration.util.Notification;

/**
 * @author jelles
 *
 */
@Controller
@RequestMapping("/billings")
public class BillingController extends YearAware {

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(path = {"", "/customer/{customerId}"}, method = RequestMethod.GET)
	public String billingForCustomer(@PathVariable Optional<Integer> customerId, Model model) {
		int year = Optional.ofNullable( model.getAttribute("year") ).map(y -> (int) y).orElse(0);
		final LocalDateTime from = DateUtils.makeOrDefault(year, 1, 1);
		final LocalDateTime to = from.plusYears(1);
		final Customer customer = customerId
				.map(id -> customerService.findById(id))
				.orElse(invoiceService.findLast().getCustomer());
		/* Get Invoices */
		List<Invoice> invoices = Optional.ofNullable(customer)
				.map(c -> invoiceService.findAll(from, to, c, true))
				.orElse(invoiceService.findAllSortedByCustomer(from, to));
		List<Customer> customers = customerService.findAllInvoicesOwners(from, to);
		model.addAttribute("invoices", invoices);
		model.addAttribute("customers", customers);
		model.addAttribute("customer", customer);
		model.addAttribute("month", from.getMonthValue());
		model.addAttribute("year", from.getYear());
		return "billings";
	}
	
	
	@RequestMapping(path = {"/overview", "/overview/{year}/customer/{customerId}"}, method = RequestMethod.GET)
	public String prepareBillingsOverview(
			@PathVariable Optional<Integer> year,
			@PathVariable Optional<Integer> customerId,
			Model model) {
		final LocalDateTime from = DateUtils.makeOrDefault(year.orElse(0), 1, 1);
		final LocalDateTime to = from.plusYears(1);
		final Customer customer = customerId.map(id -> customerService.findById(id)).orElse(null);
		List<Invoice> invoices = invoiceService.findAll(from, to, customer, true);
		model.addAttribute("customer", customer);
		model.addAttribute("invoices", invoices);
		return "fragment/billing :: overview";
	}
	
	
	@RequestMapping(path = "/payments/invoice/{invoiceId}", method = RequestMethod.GET)
	public String invoicePayments(@PathVariable Integer invoiceId, Model model) {
		if(invoiceId != null) {
			Invoice invoice = invoiceService.findFullyById(invoiceId);
			List<Payment> payments = Optional.ofNullable(invoice).map(Invoice::getPayments).orElse(new ArrayList<>(0));
			model.addAttribute("invoice", invoice);
			model.addAttribute("payments", payments);
		} else {
			model.addAttribute("payment", new Payment());
		}
		return "fragment/billing :: payments";
	}
	
	
	@RequestMapping(path = {"/invoices/{invoiceId}/payments/add", "/invoices/{invoiceId}/payments/{paymentId}/edit"}, method = RequestMethod.GET)
	public String addOrEditPayment(
			@PathVariable Integer invoiceId, 
			@PathVariable Optional<Integer> paymentId,
			Model model) {
		if(invoiceId != null) {
			Invoice invoice = invoiceService.findFullyById(invoiceId);
			Payment payment = paymentId.map(id -> {
					return invoice.getPayments().stream().filter(p -> p.getId() == id).findFirst().orElse(new Payment());
				}).orElse(new Payment());
//			Optional.ofNullable(payment).filter(predicate)
			model.addAttribute("invoice", invoice);
			model.addAttribute("payment", payment);
		}
		return "fragment/billing :: payment";
	}
	
	
	@RequestMapping(path = "/payments/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String savePayment(@Valid @RequestBody Payment payment, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors())  {
			System.out.println("Ima gresaka!");
		} else {
			boolean saved = invoiceService.savePayment(payment);
			Notification notification = new Notification(saved, "Uspesno ste uneli uplatu", null);
			Invoice paidInvoice = invoiceService.findById(payment.getInvoice().getId());
			LocalDateTime invoiceDate = paidInvoice.getCreatedOn();
			redirectAttributes.addFlashAttribute("invoice", paidInvoice);
			redirectAttributes.addFlashAttribute("notification", notification);
			return new StringBuilder("redirect:/billings/overview/")
					.append(invoiceDate.getYear()).append("/customer/")
					.append(paidInvoice.getCustomer().getId())
					.toString();
		}			
		return "redirect:/billings/overview";
	}
	
	@RequestMapping(path = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView saveInvoice(@Valid @RequestBody Invoice invoice, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes, ModelMap modelMap) {
		if (bindingResult.hasErrors()) {
			modelMap.addAttribute("invoice", invoice);
			modelMap.addAttribute("httpStatus", HttpStatus.BAD_REQUEST);
			modelMap.addAllAttributes(bindingResult.getModel());
//			return prepareInvoiceEdit(Optional.empty(), modelMap);
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
	
	
	
	@RequestMapping(path = "/payments/{paymentId}/remove", method = RequestMethod.GET)
	public String removePaymentPrepare(@PathVariable Integer paymentId, Model model) {
		Payment payment = invoiceService.findPaymentById(paymentId);
		if(payment == null) {
			return "redirect:/billings";
		}
		model.addAttribute("payment", payment);
		return "fragment/billing :: remove";
	}
	
		
	@RequestMapping(path = "/payments/remove", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView removePayment(@RequestBody Payment payment, RedirectAttributes redirectAttributes, ModelMap modelMap) {
		StringBuilder uriBuilder = new StringBuilder("redirect:/billings/overview");
		if(payment != null && payment.getId() != null) {
			payment = invoiceService.findPaymentById(payment.getId());
			final Invoice invoice = payment.getInvoice();
			boolean result = invoiceService.removePaymentById(payment.getId());			
			Notification notification = new Notification(result, "Uspesno obrisana uplata", null);
			redirectAttributes.addFlashAttribute("invoice", invoice);
			redirectAttributes.addFlashAttribute("notification", notification);
			redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
			Optional.ofNullable(invoice).ifPresent(in -> uriBuilder.append("/").append(in.getLastPaymentDate().getYear()));
			Optional.ofNullable(invoice).ifPresent(in -> uriBuilder.append("/customer/").append(in.getCustomer().getId()));
		}
		return new ModelAndView(uriBuilder.toString());
	}
	
}
