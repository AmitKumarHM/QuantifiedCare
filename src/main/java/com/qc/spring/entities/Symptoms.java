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
 * The Class Symptoms.
 */
@XmlRootElement	
@Entity
@Table(name = "symptoms")
public class Symptoms implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7140683501943352561L;

	/** The symptoms id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "symptom_id")
	private Long symptomsId;
	
	/** The symptom. */
	@Column(name = "symptom", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(symptom, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String symptom;
	
	/** The type of symptom. */
	@Column(name = "type_of_symptom", nullable = true, length = 50)
	@ColumnTransformer(read = "AES_DECRYPT(type_of_symptom, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String typeOfSymptom;
	
	/** The user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="user_id")
    private Users user;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="status_id")
	private Status status;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The updated date. */
	@Column(name = "updated_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	/**
	 * Gets the symptoms id.
	 *
	 * @return the symptoms id
	 */
	public Long getSymptomsId() {
		return symptomsId;
	}
	
	/**
	 * Sets the symptoms id.
	 *
	 * @param symptomsId the new symptoms id
	 */
	public void setSymptomsId(Long symptomsId) {
		this.symptomsId = symptomsId;
	}
	
	/**
	 * Gets the type of symptom.
	 *
	 * @return the type of symptom
	 */
	public String getTypeOfSymptom() {
		return typeOfSymptom;
	}
	
	/**
	 * Sets the type of symptom.
	 *
	 * @param typeOfSymptom the new type of symptom
	 */
	public void setTypeOfSymptom(String typeOfSymptom) {
		this.typeOfSymptom = typeOfSymptom;
	}
	
	/**
	 * Gets the symptom.
	 *
	 * @return the symptom
	 */
	public String getSymptom() {
		return symptom;
	}
	
	/**
	 * Sets the symptom.
	 *
	 * @param symptom the new symptom
	 */
	public void setSymptom(String symptom) {
		this.symptom = symptom;
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
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public Users getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(Users user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
