package com.qc.spring.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnTransformer;

import com.qc.spring.enums.Color;
import com.qc.spring.enums.Dosage;
import com.qc.spring.enums.Form;
import com.qc.spring.enums.Quantity;
import com.qc.spring.enums.Shape;
import com.qc.spring.enums.Strength;


/**
 * The Class Medications.
 */
@XmlRootElement	
@Entity
@Table(name = "medications")
public class Medications implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7495764372164467583L;

	/** The medication id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "medication_id")
	private Long medicationId;
	
	@Column(name = "name", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(name, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String name;
	
	@Column(name = "form")	
	@Enumerated(EnumType.STRING) 	
	private Form form;
	
	@Column(name = "strength_unit")	
	@Enumerated(EnumType.STRING) 
	private Strength strengthUnit;
	
	@Column(name = "shape")	
	@Enumerated(EnumType.STRING) 
	private Shape shape;
	
	@Column(name = "color")	
	@Enumerated(EnumType.STRING) 
	private Color color;
	
	@Column(name = "total_quantity")	 
	private Integer totalQuantity ;
	
	@Column(name = "quantity_unit")	
	@Enumerated(EnumType.STRING) 
	private Quantity quantityUnit;
	
	@Column(name = "dosage")	 
	private Integer dosage ;
	
	@Column(name = "dosage_unit")	
	@Enumerated(EnumType.STRING) 
	private Dosage dosageUnit;
	
	@Column(name = "duration")	 
	private Integer duration ;
	
	@Column(name = "strength")	 
	private Integer strength ;
		
	@Column(name = "start_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name = "end_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The updated date. */
	@Column(name = "updated_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;	
	
	/** The user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="user_id")
    private Users user;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="slot_id")
    private Slot slot;
	
	@Transient
	private List<Schedule> schedules;
	
	
	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Quantity getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(Quantity quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public Integer getDosage() {
		return dosage;
	}

	public void setDosage(Integer dosage) {
		this.dosage = dosage;
	}

	public Dosage getDosageUnit() {
		return dosageUnit;
	}

	public void setDosageUnit(Dosage dosageUnit) {
		this.dosageUnit = dosageUnit;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	/**
	 * Gets the medication id.
	 *
	 * @return the medication id
	 */
	public Long getMedicationId() {
		return medicationId;
	}
	
	/**
	 * Sets the medication id.
	 *
	 * @param medicationId the new medication id
	 */
	public void setMedicationId(Long medicationId) {
		this.medicationId = medicationId;
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

	public Strength getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(Strength strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	@Override
	public String toString() {
		StringBuilder str=new StringBuilder("{");
		str.append("\"medication_id\":").append(medicationId)
		.append(",\"strength\":").append(getStrength())
		.append(",\"quantity\":").append(getTotalQuantity())
		.append(",\"strength_unit\":\"").append(getStrengthUnit().getType())
		.append("\" ,\"quantity_unit\":\"").append(getQuantityUnit().getType())
		.append("\" ,\"dosage\":").append(getDosage())
		.append(",\"dosage_unit\":\"").append(getDosageUnit().getType())
		.append("\" ,\"med_name\":\"").append(getName())
		.append("}");	
		return str.toString();
			
	}	
}
