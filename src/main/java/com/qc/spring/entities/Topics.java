package com.qc.spring.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement	
@Entity
@Table(name = "topic")
public class Topics implements Serializable {

   
	private static final long serialVersionUID = 3692592956349118909L;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "weightage")
	private Long weightage;
	
	@ManyToOne
	@JoinColumn(name="course_id")
	private Course course;
	
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWeightage() {
		return weightage;
	}

	public void setWeightage(Long weightage) {
		this.weightage = weightage;
	}
	
	
	
	
}
