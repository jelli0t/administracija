/**
 * 
 */
package rs.neks.administration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.neks.administration.model.Invoice;
import rs.neks.administration.model.Payment;
import rs.neks.administration.service.CustomerService;
import rs.neks.administration.service.InvoiceService;
import rs.neks.administration.util.DateUtils;

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
		model.addAttribute("invoices", invoices);
		model.addAttribute("month", from.getMonthValue());
		model.addAttribute("year", from.getYear());
		return "billings";
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
			model.addAttribute("invoice", invoice);
			model.addAttribute("payment", payment);
		}
		return "fragment/billing :: payment";
	}
	
	
	@RequestMapping(path = "/payments/save", method = RequestMethod.POST)
	public String savePayment(@ModelAttribute("payment") Payment payment) {
		boolean saved = invoiceService.savePayment(payment);
		return "redirect:/billings";
	}
}
