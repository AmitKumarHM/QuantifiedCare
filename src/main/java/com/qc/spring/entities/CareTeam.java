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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnTransformer;

@XmlRootElement	
@Entity
@Table(name = "care_team")
public class CareTeam implements Serializable {


	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "careteam_id")
	private Long careTeamId;


	/** The first name. */
	@Column(name = "pharmacy_name", nullable = true, length = 50)
	@ColumnTransformer(read = "AES_DECRYPT(pharmacy_name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String pharmacyName;


	/** The first name. */
	@Column(name = "first_name", nullable = true, length = 50)
	@ColumnTransformer(read = "AES_DECRYPT(first_name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String firstName;

	/** The last name. */
	@Column(name = "last_name", nullable = true, length = 100)
	@ColumnTransformer(read = "AES_DECRYPT(last_name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String lastName;

	/** The address. */
	@Column(name = "address", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(address, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String address;

	/** The postal code. */
	@Column(name = "postal_code", nullable = true, length = 20)
	private Integer postalCode;

	/** The mobile number. */
	@Column(name = "mobile_number", nullable = false , length = 50)
	@ColumnTransformer(read = "AES_DECRYPT(mobile_number, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String mobileNumber;

	/** The country. */
	@Column(name = "country", nullable = true, length = 50)
	@ColumnTransformer(read = "AES_DECRYPT(country, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String country;
	
	/** The email id. */
	@Column(name = "email_id", nullable = true, length = 100)
	@ColumnTransformer(read = "AES_DECRYPT(email_id, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
    private String emailId;
		
	/** The city. */
	@Column(name = "city", nullable = true, length = 50)
	@ColumnTransformer(read = "AES_DECRYPT(city, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String city;
	
	@Transient
	private String stateName;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	/** The updated date. */
	@Column(name = "updated_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	/** The status. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="status_id")
	private Status status;

	/** The state. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="state_id")
	private States state;


	/** The role. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="role_id")
    private Roles role;
	
	
	/** The user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="user_id")
	private Users user;


	public Long getCareTeamId() {
		return careTeamId;
	}


	public void setCareTeamId(Long careTeamId) {
		this.careTeamId = careTeamId;
	}


	public String getPharmacyName() {
		return pharmacyName;
	}


	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getPostalCode() {
		return postalCode;
	}


	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Date getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public States getState() {
		return state;
	}


	public void setState(States state) {
		this.state = state;
	}


	public Users getUser() {
		return user;
	}


	public void setUser(Users user) {
		this.user = user;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getStateName() {
		return stateName;
	}


	public void setStateName(String stateName) {
		this.stateName = stateName;
	}


	public Roles getRole() {
		return role;
	}


	public void setRole(Roles role) {
		this.role = role;
	}
	
}
