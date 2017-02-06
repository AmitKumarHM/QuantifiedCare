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
 * The Class Health.
 */
@XmlRootElement	
@Entity
@Table(name = "health")
public class Health implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The health id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "health_id")
	private Long healthId;
	
	/** The user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="user_id")
    private Users user;
	
	/** The bp. */
	@Column(name = "bp", nullable = true,length=100)
	@ColumnTransformer(read = "AES_DECRYPT(bp, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String bp;
	
	/** The bp Unit. */
	@Column(name = "bp_unit", nullable = true,length=100)
	@ColumnTransformer(read = "AES_DECRYPT(bp_unit, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String bpUnit;
	

	/** The bmi. */
	@Column(name = "bmi", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(bmi, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String bmi; 
	
	/** The bp Unit. */
	@Column(name = "bmi_unit", nullable = true,length=100)
	@ColumnTransformer(read = "AES_DECRYPT(bmi_unit, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String bmiUnit;	
	
	/** The med adherence. */
	@Column(name = "medical_adherence", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(medical_adherence, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String medAdherence;
	
	@Column(name = "med_adherence_unit", nullable = true,length=100)
	@ColumnTransformer(read = "AES_DECRYPT(med_adherence_unit, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String medicalAdherenceUnit;
	
	/** The weight. */
	@Column(name = "weight", nullable = true,length=50)
	@ColumnTransformer(read = "AES_DECRYPT(weight, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String weight;
	
	@Column(name = "weight_unit", nullable = true,length=100)
	@ColumnTransformer(read = "AES_DECRYPT(weight_unit, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String weightUnit;
	
	/** The physical activity. */
	@Column(name = "physical_activity", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(physical_activity, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String physicalActivity;
	
	@Column(name = "physact_unit", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(physact_unit, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String physicalActivityUnit;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="bp_status_id")
	private Status bpStatus;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="bmi_status_id")
	private Status bmiStatus;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="med_status_id")
	private Status medStatus;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="weight_status_id")
	private Status weightStatus;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="physact_status_id")
	private Status physActStatus;
	
	
	@Column(name = "bp_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bpDate;
	
	
	@Column(name = "bmi_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bmiDate;

	
	@Column(name = "med_adherence_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date medAdherenceDate;

	@Column(name = "physact_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date physactDate;

	@Column(name = "weight_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date weightDate;

	
	/**
	 * Gets the health id.
	 *
	 * @return the health id
	 */
	public Long getHealthId() {
		return healthId;
	}

	/**
	 * Sets the health id.
	 *
	 * @param healthId the new health id
	 */
	public void setHealthId(Long healthId) {
		this.healthId = healthId;
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

	/**
	 * Gets the bp.
	 *
	 * @return the bp
	 */
	public String getBp() {
		return bp;
	}

	/**
	 * Sets the bp.
	 *
	 * @param bp the new bp
	 */
	public void setBp(String bp) {
		this.bp = bp;
	}

	/**
	 * Gets the bmi.
	 *
	 * @return the bmi
	 */
	public String getBmi() {
		return bmi;
	}

	/**
	 * Sets the bmi.
	 *
	 * @param bmi the new bmi
	 */
	public void setBmi(String bmi) {
		this.bmi = bmi;
	}

	/**
	 * Gets the med adherence.
	 *
	 * @return the med adherence
	 */
	public String getMedAdherence() {
		return medAdherence;
	}

	/**
	 * Sets the med adherence.
	 *
	 * @param medAdherence the new med adherence
	 */
	public void setMedAdherence(String medAdherence) {
		this.medAdherence = medAdherence;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * Gets the physical activity.
	 *
	 * @return the physical activity
	 */
	public String getPhysicalActivity() {
		return physicalActivity;
	}

	/**
	 * Sets the physical activity.
	 *
	 * @param physicalActivity the new physical activity
	 */
	public void setPhysicalActivity(String physicalActivity) {
		this.physicalActivity = physicalActivity;
	}

	public Date getBpDate() {
		return bpDate;
	}

	public void setBpDate(Date bpDate) {
		this.bpDate = bpDate;
	}

	public Date getBmiDate() {
		return bmiDate;
	}

	public void setBmiDate(Date bmiDate) {
		this.bmiDate = bmiDate;
	}

	public Date getMedAdherenceDate() {
		return medAdherenceDate;
	}

	public void setMedAdherenceDate(Date medAdherenceDate) {
		this.medAdherenceDate = medAdherenceDate;
	}

	public Date getPhysactDate() {
		return physactDate;
	}

	public void setPhysactDate(Date physactDate) {
		this.physactDate = physactDate;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public String getBpUnit() {
		return bpUnit;
	}

	public void setBpUnit(String bpUnit) {
		this.bpUnit = bpUnit;
	}

	public String getBmiUnit() {
		return bmiUnit;
	}

	public void setBmiUnit(String bmiUnit) {
		this.bmiUnit = bmiUnit;
	}

	public String getMedicalAdherenceUnit() {
		return medicalAdherenceUnit;
	}

	public void setMedicalAdherenceUnit(String medicalAdherenceUnit) {
		this.medicalAdherenceUnit = medicalAdherenceUnit;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getPhysicalActivityUnit() {
		return physicalActivityUnit;
	}

	public void setPhysicalActivityUnit(String physicalActivityUnit) {
		this.physicalActivityUnit = physicalActivityUnit;
	}

	public Status getBpStatus() {
		return bpStatus;
	}

	public void setBpStatus(Status bpStatus) {
		this.bpStatus = bpStatus;
	}

	public Status getBmiStatus() {
		return bmiStatus;
	}

	public void setBmiStatus(Status bmiStatus) {
		this.bmiStatus = bmiStatus;
	}

	public Status getMedStatus() {
		return medStatus;
	}

	public void setMedStatus(Status medStatus) {
		this.medStatus = medStatus;
	}

	public Status getWeightStatus() {
		return weightStatus;
	}

	public void setWeightStatus(Status weightStatus) {
		this.weightStatus = weightStatus;
	}

	public Status getPhysActStatus() {
		return physActStatus;
	}

	public void setPhysActStatus(Status physActStatus) {
		this.physActStatus = physActStatus;
	}	
}
