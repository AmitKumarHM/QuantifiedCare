package com.qc.spring.dao;

import com.qc.spring.entities.Organizations;

public interface OrganizationDAO {
	
	public Organizations byID(Long id);
	
	public boolean save(Organizations org);
	
	public boolean update(Organizations org);
}
