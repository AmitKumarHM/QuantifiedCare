package com.qc.spring.service;

import java.util.List;

import com.qc.spring.entities.Roles;
import com.qc.spring.entities.States;


public interface CareTeamService {

	public Long save(Object entity);
	
	public States getByStateName(String state);

	public  List<Roles> getAll();

	public <T> List<T> getAllEntity(Class<T> type);

	
}
