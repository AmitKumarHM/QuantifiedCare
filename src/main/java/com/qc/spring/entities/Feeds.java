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

@XmlRootElement	
@Entity
@Table(name = "feeds")
public class Feeds implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 238827125093911190L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "feed_id")
	private Long id;
    
    /** The user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="patient_id")
    private Users patient;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="caregiver_id")
    private Users caregiver;
	
	
	/** The Feed Name. */
	@Column(name = "name", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String name;
	
	@Column(name = "description",columnDefinition = "TEXT", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(description, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String description;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The created date. */
	@Column(name = "updated_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Users getPatient() {
		return patient;
	}

	public void setPatient(Users patient) {
		this.patient = patient;
	}

	public Users getCaregiver() {
		return caregiver;
	}

	public void setCaregiver(Users caregiver) {
		this.caregiver = caregiver;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
