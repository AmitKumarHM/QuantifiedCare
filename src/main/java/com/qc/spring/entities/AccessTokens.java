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


/**
 * The Class AccessTokens.
 */
@XmlRootElement	
@Entity
@Table(name = "access_tokens")
public class AccessTokens implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 238827125093911190L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "access_token_id")
    private Long id;
    
    /** The user. */
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinColumn(name="user_id")
	private Users user;
	
	/** The access token. */
    @Column(name = "access_token", nullable = true)
	private String accessToken;
	
	/** The created date. */
	@Column(name = "created_date", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	
	public String getAccessToken() {
		return accessToken;
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
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the user.
	 *
	 * @param users the new user
	 */
	public void setUser(Users users) {
		this.user = users;
	}

	/**
	 * Sets the access token.
	 *
	 * @param accessToken the new access token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
