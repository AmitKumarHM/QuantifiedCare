package com.qc.spring.service;

import com.qc.spring.entities.Organizations;

public interface OrganizationService {

	public Organizations findByID(Long id);
	
	public boolean save(Organizations org);
	
	public boolean update(Organizations org);
}
