package com.qc.spring.dao;

import com.qc.spring.entities.Symptoms;

public interface SymptomDAO {

	public Symptoms byId(Long id);
	
	public Symptoms byUserId(Long id);
	
	public boolean save(Symptoms symptoms);
		
	public boolean update(Symptoms symptoms) ;
	
	public int removeByPatientId(Long userId); 
		
}
