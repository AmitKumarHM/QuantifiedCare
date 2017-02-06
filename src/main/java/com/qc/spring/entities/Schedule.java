package com.qc.spring.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;

import com.qc.spring.enums.MedicationStatus;

@XmlRootElement	
@Entity
@Table(name = "schedule")
public class Schedule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "schedule_id")
	private Long scheduleId;
	
	@Transient
	private Long medicationId;	
	
	@Column(name = "scheduled_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduleDate;
	
	@Column(name = "time", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Column(name = "reminder_time", columnDefinition = "BIGINT default -1")
	private long reminderTime;
	
	@Column(name = "days", columnDefinition = "smallint default 0")
	private short days;
	
	@Column(name = "med_status")	
	@Enumerated(EnumType.STRING) 
	private MedicationStatus medicationStatus;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="medication_id")
    private Medications medications;
	
	/** The flag. */
	@Column(name = "flag", columnDefinition = "smallint default 0")
	private Short flag;
	
	@Transient
	private String alarmTime;
	
	@Transient
	private String dateAlarm;
	
	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getDateAlarm() {
		return dateAlarm;
	}

	public void setDateAlarm(String dateAlarm) {
		this.dateAlarm = dateAlarm;
	}

	public Short getFlag() {
		return flag;
	}

	public void setFlag(Short flag) {
		this.flag = flag;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Medications getMedications() {
		return medications;
	}

	public void setMedications(Medications medications) {
		this.medications = medications;
	}

	public Long getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(Long medicationId) {
		this.medicationId = medicationId;
	}
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public short getDays() {
		return days;
	}

	public void setDays(short days) {
		this.days = days;
	}

	public MedicationStatus getMedicationStatus() {
		return medicationStatus;
	}

	public void setMedicationStatus(MedicationStatus medicationStatus) {
		this.medicationStatus = medicationStatus;
	}

	public Long getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(Long reminderTime) {
		this.reminderTime = reminderTime;
	}

	@Override
	public String toString() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
		StringBuilder str=new StringBuilder("{")
		.append("\"schedule_id\":").append(scheduleId)
		.append(",\"date\":").append((scheduleDate != null? scheduleDate.getTime(): null))
		.append(",\"time\":").append((time != null? formatter.format(time): null))
		.append(",\"day\":").append(days)
		.append(",\"flag\":").append(flag)
		.append(",\"medication_id\":").append(medications.getMedicationId())
		.append(",\"strength\":").append(medications.getStrength())
		.append(",\"strength_unit\":\"").append(medications.getStrengthUnit().getType())
		.append("\" ,\"quantity\":\"").append(medications.getTotalQuantity())
		.append("\" ,\"quantity_unit\":\"").append(medications.getQuantityUnit().getType())
		.append("\" ,\"dosage\":\"").append(medications.getDosage())
		.append("\" ,\"dosage_unit\":\"").append(medications.getDosageUnit().getType())
		.append("\" ,\"med_name\":\"").append(medications.getName())
		.append("\"}");	
		return str.toString();
			
	}

}
