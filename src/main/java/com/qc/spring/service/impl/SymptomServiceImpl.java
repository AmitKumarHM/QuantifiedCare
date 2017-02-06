package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.SymptomDAO;
import com.qc.spring.entities.Symptoms;
import com.qc.spring.service.SymptomService;

@Service
@Transactional
public class SymptomServiceImpl implements SymptomService {

	@Autowired
	private SymptomDAO symptomDAO;
	
	@Override
	public Symptoms findById(Long id) {
		
		return symptomDAO.byId(id);
	}

	@Override
	public Symptoms findByUserId(Long id) {
		
		return symptomDAO.byUserId(id);
	}

	@Override
	public boolean save(Symptoms symptoms) {
		
		return symptomDAO.save(symptoms);
	}

	@Override
	public boolean update(Symptoms symptoms) {
		
		return symptomDAO.update(symptoms);
	}

	@Override
	public int removeByPatientId(Long userId) {
		
		return symptomDAO.removeByPatientId(userId);
	}

}
