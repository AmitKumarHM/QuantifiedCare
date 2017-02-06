package com.qc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.GenericDAO;
import com.qc.spring.entities.Roles;
import com.qc.spring.entities.States;
import com.qc.spring.service.CareTeamService;

@Service
@Transactional
public class CareTeamServiceImpl implements CareTeamService {

	@Autowired
	private GenericDAO genericDAO;
	
	@Override
	public Long save(Object entity) {
		return (Long)genericDAO.save(entity);
	}

	@Override
	public States getByStateName(String state) {
		
		return genericDAO.findByStateName(state);
	}

	@Override
	public List<Roles> getAll() {
		
		return genericDAO.findAll(Roles.class);
	}

	@Override
	public <T> List<T> getAllEntity(Class<T> type){
		return genericDAO.findAll(type);}
	

}
