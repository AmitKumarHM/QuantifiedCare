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
 * The Class Messages.
 */
@XmlRootElement	
@Entity
@Table(name = "messages")
public class Messages implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -67484760191452657L;

	/** The message id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "message_id")
	private Long messageId;
	
	/** The subject. */
	@Column(name = "subject", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(subject, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String subject;
	
	/** The body. */
	@Column(name = "body", nullable = true)
	@ColumnTransformer(read = "AES_DECRYPT(body, 'kEyLI1Fy648tzWXGuRcxrg==')", write = "AES_ENCRYPT(?, 'kEyLI1Fy648tzWXGuRcxrg==')")
	private String body;
	
	
	/** The deleted. */
	@Column(name = "deleted", nullable = true, columnDefinition = "smallint default 1")
	private Short deleted;
	
	/** The subject. */
	@Column(name = "label", nullable = true, length = 20)
	private String label;
	
	/** The subject. */
	@Column(name = "restore", nullable = true, length = 20)
	private String restore;
		
	/** The from user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="fromUser")
    private Users fromUser;	
	
	/** The to user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="toUser")
    private Users toUser;
	
	/** The updated date. */
	@Column(name = "updated_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The flag. */
	@Column(name = "flag", columnDefinition = "smallint default 0")
	private Short flag;
	
	
	public Short getFlag() {
		return flag;
	}

	public void setFlag(Short flag) {
		this.flag = flag;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public Long getMessageId() {
		return messageId;
	}
	
	/**
	 * Gets the from user.
	 *
	 * @return the from user
	 */
	public Users getFromUser() {
		return fromUser;
	}
	
	/**
	 * Gets the to user.
	 *
	 * @return the to user
	 */
	public Users getToUser() {
		return toUser;
	}
	
	/**
	 * Sets the message id.
	 *
	 * @param messageId the new message id
	 */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
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
	 * Sets the from user.
	 *
	 * @param fromUser the new from user
	 */
	public void setFromUser(Users fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * Sets the to user.
	 *
	 * @param toUser the new to user
	 */
	public void setToUser(Users toUser) {
		this.toUser = toUser;
	}
	
	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Short getDeleted() {
		return deleted;
	}

	public void setDeleted(Short deleted) {
		this.deleted = deleted;
	}

	public String getRestore() {
		return restore;
	}

	public void setRestore(String restore) {
		this.restore = restore;
	}
}
