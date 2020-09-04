/**
 * 
 */
package rs.neks.administration.controller;

import java.beans.PropertyEditorSupport;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import rs.neks.administration.util.TextUtils;

/**
 * @author jelles
 *
 */
@Controller
@RequestMapping("/billings")
public class BillingController {

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String defaultOverview(Model model) {
		final LocalDateTime from = DateUtils.makeOrDefault(0, 1, 1);
		final LocalDateTime to = from.plusYears(1);
		List<Invoice> invoices = invoiceService.findAllSortedByCustomer(from, to);
		List<Customer> customers = customerService.findAll(true);
		model.addAttribute("invoices", invoices);
		model.addAttribute("customers", customers);
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
		return "fragment/billing :: remove_confirm";
	}
	
	
	@RequestMapping(path = "/payments/{paymentId}/remove", method = RequestMethod.DELETE)
	public String removePayment(@PathVariable Integer paymentId, RedirectAttributes redirectAttributes) {
		boolean removed = invoiceService.removePaymentById(paymentId);
		Notification notification = new Notification(removed, "Uspesno ste obrisali uplatu", null);
		redirectAttributes.addFlashAttribute("notification", notification);
		redirectAttributes.addFlashAttribute("httpStatus", HttpStatus.OK);
		return "redirect:/billings/overview";
	}
	
	
}
