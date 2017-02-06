package com.qc.spring.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnTransformer;


/**
 * The Class Organizations.
 */
@XmlRootElement	
@Entity
@Table(name = "organizations")
public class Organizations implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7715277067233988831L;

	/** The org id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_id")
	private Long orgId;
	
	/** The name. */
	@Column(name = "name", nullable = true, length=100)
	@ColumnTransformer(read = "AES_DECRYPT(name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String name;
	
	/** The address. */
	@Column(name = "address", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(address, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String address;
	
	/** The city. */
	@Column(name = "city", nullable = true , length=50)
	@ColumnTransformer(read = "AES_DECRYPT(city, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String city;
	
	/** The country. */
	@Column(name = "country", nullable = true , length=50)
	@ColumnTransformer(read = "AES_DECRYPT(country, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String country;
	
	/** The postal code. */
	@Column(name = "postal_code", nullable = true , length=20)
	private Integer postalCode;
	
	/** The contact number. */
	@Column(name = "contact_number", nullable = true , length=20)
	private String contactNumber;
	
	/** The status. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="status_id")
	private Status status;
	
	/** The states. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="state_id")
    private States states;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The updated date. */
	@Column(name = "updated_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	

	/**
	 * Gets the org id.
	 *
	 * @return the org id
	 */
	public Long getOrgId() {
		return orgId;
	}
	
	/**
	 * Sets the org id.
	 *
	 * @param orgId the new org id
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets the postal code.
	 *
	 * @return the postal code
	 */
	public Integer getPostalCode() {
		return postalCode;
	}
	
	/**
	 * Sets the postal code.
	 *
	 * @param postalCode the new postal code
	 */
	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Gets the states.
	 *
	 * @return the states
	 */
	public States getStates() {
		return states;
	}
	
	/**
	 * Sets the states.
	 *
	 * @param states the new states
	 */
	public void setStates(States states) {
		this.states = states;
	}
	
	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	
	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
		
}
