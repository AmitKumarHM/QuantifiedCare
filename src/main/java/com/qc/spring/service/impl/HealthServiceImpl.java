package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.HealthDAO;
import com.qc.spring.entities.Health;
import com.qc.spring.service.HealthService;

@Service
@Transactional
public class HealthServiceImpl implements HealthService {

	@Autowired
	private HealthDAO healthDAO;
	
	@Override
	public Health findByID(Long id) {
		
		return healthDAO.byID(id);
	}

	@Override
	public Health findByUserId(Long id) {
		
		return healthDAO.byUserId(id);
	}

}
