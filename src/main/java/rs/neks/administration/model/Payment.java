/**
 * 
 */
package rs.neks.administration.model;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import rs.neks.administration.util.DateUtils;
import rs.neks.administration.util.TextUtils;

/**
 * @author jelles
 *
 */
@Entity
@Table(name = "payment")
public class Payment implements Idable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "payment_date")
	private LocalDateTime paymentDate;
	
	@NotNull(message = "Molimo unesite iznos uplate")
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name = "invoice_id")
	private Invoice invoice;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public String getPickerPaymentDate() {
		return Optional.ofNullable(paymentDate)
				.map(d -> DateUtils.formatDateTime(d, DateUtils.PICKER_DATE_FORMATTER))
				.orElse(DateUtils.formatDateTime(LocalDateTime.now(), DateUtils.PICKER_DATE_FORMATTER));
	}
	
	public void setPickerPaymentDate(String paymentDate) {
		if(TextUtils.notEmpty(paymentDate)) {
			this.paymentDate = DateUtils.parseDateTime(paymentDate, DateUtils.PICKER_DATE_FORMATTER);
		}			
	}
}
