package com.qc.spring.service;

import com.qc.spring.entities.Symptoms;

public interface SymptomService {

	public Symptoms findById(Long id);
	
	public Symptoms findByUserId(Long id);
	
	public boolean save(Symptoms symptoms);
		
	public boolean update(Symptoms symptoms) ;
	
	public int removeByPatientId(Long userId); 
		
}
