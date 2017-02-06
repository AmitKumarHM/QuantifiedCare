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
 * The Class Roles.
 */
@XmlRootElement	
@Entity
@Table(name = "roles")
public class Roles implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1358406869128693767L;

	/** The role id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Integer roleId;
	
	/** The role. */
	@Column(name = "role", nullable = true, length=100)
	private String role;
	
	/**
	 * Gets the role id.
	 *
	 * @return the role id
	 */
	public Integer getRoleId() {
		return roleId;
	}
	
	/**
	 * Sets the role id.
	 *
	 * @param roleId the new role id
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
