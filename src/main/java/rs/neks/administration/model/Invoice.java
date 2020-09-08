/**
 * 
 */
package rs.neks.administration.model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import rs.neks.administration.util.AmountDeserializer;
import rs.neks.administration.util.DateUtils;
import rs.neks.administration.util.TextUtils;

/**
 * @author nemanja
 *
 */
@Entity
@Table(name = "invoice")
public class Invoice implements Idable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message = "Molimo unesite broj fakture")
	@Size(min = 1, max = 9, message = "Sifra kupca mora sadrzati izmedju 1 i 9 karaktera")
	@Column(name = "invoice_no")
	private String invoiceNo;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "due_for_payment")
	private LocalDateTime dueForPayment;
	
	@Column(name = "total_amount")
	@NotNull(message = "Molimo unesite iznas na koji fakturisete")
	@JsonDeserialize(using = AmountDeserializer.class)
	private Double totalAmount;
	
	private String description;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="invoice")
	private List<Payment> payments;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getPickerCreatedOn() {
		return Optional.ofNullable(createdOn)
				.map(d -> DateUtils.formatDateTime(d, DateUtils.PICKER_DATE_FORMATTER))
				.orElse(DateUtils.formatDateTime(LocalDateTime.now(), DateUtils.PICKER_DATE_FORMATTER));
	}
	
	public void setPickerCreatedOn(String createdOn) {
		if(TextUtils.notEmpty(createdOn)) {
			this.createdOn = DateUtils.parseDateTime(createdOn, DateUtils.PICKER_DATE_FORMATTER);
		}			
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getDueForPayment() {
		return dueForPayment;
	}

	public void setDueForPayment(LocalDateTime dueForPayment) {
		this.dueForPayment = dueForPayment;
	}

	public String getPickerDueForPayment() {
		return Optional.ofNullable(dueForPayment)
				.map(d -> DateUtils.formatDateTime(d, DateUtils.PICKER_DATE_FORMATTER))
				.orElse(DateUtils.formatDateTime(LocalDateTime.now(), DateUtils.PICKER_DATE_FORMATTER));
	}
	
	public void setPickerDueForPayment(String dueForPayment) {
		if(TextUtils.notEmpty(createdOn)) {
			this.dueForPayment = DateUtils.parseDateTime(dueForPayment, DateUtils.PICKER_DATE_FORMATTER);
		}			
	}
	
	public Double getTotalAmount() {		
		return Optional.ofNullable(this.totalAmount).orElse(0d);
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Payment> getPayments() {
		return payments;
	}
		
	public Double getPaidAmount() {
		if(this.payments != null && payments.size() > 0) {
			return payments.stream().mapToDouble(Payment::getAmount).sum();
		}
		return 0d;
	}
	
	public LocalDateTime getLastPaymentDate() {
		if(this.payments != null && payments.size() > 0) {
			return payments.stream().filter(p -> p.getPaymentDate() != null)
					.map(Payment::getPaymentDate)
					.sorted(Comparator.nullsLast(
				     (pd1, pd2) -> pd2.compareTo(pd1)))					
					.findFirst().orElse(null);
		}
		return null;
	} 
}
