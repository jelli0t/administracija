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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import rs.neks.administration.util.TextUtils;

/**
 * @author nemanja
 *
 */
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Molimo unesite sifru kupca")
	@Size(min = 3, max = 9, message = "Sifra kupca mora sadrzati izmedju 3 i 9 karaktera")
	@Column(name = "customer_code")
	private String code;

	@NotNull
	@Size(min = 3, max = 30, message = "Naziv kupca mora sadrzati izmedju 3 i 30 karaktera")
	private String name;

	@Column(name = "alt_name")
	private String altName;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "active", columnDefinition = "TINYINT")
	private boolean active;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	private String phone;

	@Column(name = "cell_phone")
	private String cellPhone;

	private String email;

	private String pib;

	private String place;

	@Column(name = "zip_code")
	private String zipCode;

	private String address;

	private String description;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAltName() {
		return altName;
	}

	public void setAltName(String altName) {
		this.altName = altName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPib() {
		return pib;
	}

	public void setPib(String pib) {
		this.pib = pib;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = TextUtils.isEmpty(zipCode) ? "00000" : zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder customer = new StringBuilder("Customer: {");
		Optional.ofNullable(this.id).ifPresent(x -> customer.append("id: ").append(x).append(", "));
		Optional.ofNullable(this.code).ifPresent(x -> customer.append("code: ").append(x).append(", "));
		Optional.ofNullable(this.name).ifPresent(x -> customer.append("name: ").append(x));
		return customer.append("}").toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
}
