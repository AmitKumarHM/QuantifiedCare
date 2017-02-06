package com.qc.spring.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Statuses.
 */
@XmlRootElement	
@Entity
@Table(name = "statuses")
public class Status implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3961857101976120835L;

	/** The status id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "status_id")
	private Integer statusId;
	
	/** The status. */
	@Column(name = "status", nullable = true, length=100)
	private String status;
	
	/**
	 * Gets the status id.
	 *
	 * @return the status id
	 */
	public Integer getStatusId() {
		return statusId;
	}
	
	/**
	 * Sets the status id.
	 *
	 * @param statusId the new status id
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
