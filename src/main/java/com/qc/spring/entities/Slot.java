package com.qc.spring.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.qc.spring.enums.Slots;


@XmlRootElement	
@Entity
@Table(name = "slot")
public class Slot implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "slot_id")
	private Integer slotId;
	
	@Column(name = "slot")	
	@Enumerated(EnumType.STRING) 
	private Slots slot;
	
	@Column(name = "frequency")	
	@Enumerated(EnumType.STRING) 
	private Slots frequency;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The updated date. */
	@Column(name = "updated_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Slots getSlot() {
		return slot;
	}

	public void setSlot(Slots slot) {
		this.slot = slot;
	}

	public Slots getFrequency() {
		return frequency;
	}

	public void setFrequency(Slots frequency) {
		this.frequency = frequency;
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
	
}
