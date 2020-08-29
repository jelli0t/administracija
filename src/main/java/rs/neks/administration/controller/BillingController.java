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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rs.neks.administration.model.Invoice;
import rs.neks.administration.model.Payment;
import rs.neks.administration.service.CustomerService;
import rs.neks.administration.service.InvoiceService;
import rs.neks.administration.util.DateUtils;
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
	public String savePayment(@Valid @ModelAttribute("payment") Payment payment, 
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors())  {
			System.out.println("Ima gresaka!");
		} else {
			boolean saved = invoiceService.savePayment(payment);
			Invoice paidInvoice = payment.getInvoice();
//			redirectAttributes.addAttribute("invoice", paidInvoice);
		}			
		return "redirect:/billings";
	}
	
	
	@RequestMapping(path = "/payments/remove/{paymentId}", method = RequestMethod.GET)
	public String removePaymentPrepare(@PathVariable Integer paymentId) {
		
		return "";
	}
	
	
	@InitBinder(value = "payment")
	protected void initBinder(org.springframework.web.bind.WebDataBinder binder) {
		binder.registerCustomEditor(Double.class, "amount", new PropertyEditorSupport() {
			
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
