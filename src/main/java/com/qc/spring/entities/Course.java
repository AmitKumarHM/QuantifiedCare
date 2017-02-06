package com.qc.spring.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.IndexColumn;

@XmlRootElement	
@Entity
@Table(name = "course")
public class Course implements Serializable {

	private static final long serialVersionUID = -1326194389264975336L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "course_id")
	private long id;
	
	@Column(name = "name", nullable = true)
	private String name;
	
	@Column(name = "fee", nullable = true)
	private int fee;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@IndexColumn(name = "topicIndex", base = 1)
	private List<Topics> topicList;
	
	
	
	/*@ElementCollection(targetClass = String.class)
	@JoinTable(name = "tropics",joinColumns = @JoinColumn(name = "course_id"))
	@IndexColumn(name = "POSITION", base = 1)
	@Column(name = "tropic", nullable = false)
	private List<String> topics;
*/
	
	public List<Topics> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<Topics> topicList) {
		this.topicList = topicList;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", fee=" + fee
				 + "]";
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	/*public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
*/

}
