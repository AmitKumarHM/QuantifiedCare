package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.OrganizationDAO;
import com.qc.spring.entities.Organizations;
import com.qc.spring.service.OrganizationService;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationDAO organizationDAO;
	
	@Override
	public Organizations findByID(Long id) {
		
		return organizationDAO.byID(id);
	}

	@Override
	@Transactional
	public boolean save(Organizations org) {
		
		return organizationDAO.save(org);
	}

	@Override
	@Transactional
	public boolean update(Organizations org) {
		
		return organizationDAO.update(org);
	}

}
