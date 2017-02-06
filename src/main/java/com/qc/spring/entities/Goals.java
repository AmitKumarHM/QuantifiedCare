package com.qc.spring.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * The Class Goals.
 */
@XmlRootElement	
@Entity
@Table(name = "goals")
public class Goals implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5530405810682300038L;
	
	/** The goal id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "goal_id")
	private Long goalId;
	
	/** The care plans. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="care_plan_id")
    private CarePlans carePlans;
	
	/** The name. */
	@Column(name = "name", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String name;
	
	/** The description. */
	@Column(name = "description", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(description, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String description;
	
	/** The status. */
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
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
	 * Gets the care plans.
	 *
	 * @return the care plans
	 */
	public CarePlans getCarePlans() {
		return carePlans;
	}

	/**
	 * Sets the care plans.
	 *
	 * @param carePlans the new care plans
	 */
	public void setCarePlans(CarePlans carePlans) {
		this.carePlans = carePlans;
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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
