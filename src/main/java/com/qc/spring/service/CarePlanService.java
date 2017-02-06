package com.qc.spring.service;

import java.util.List;

import com.qc.spring.entities.CarePlans;

public interface CarePlanService {

	public CarePlans findByID(Long id);
	
	public List<CarePlans> findByCareGiverId(Long id,Long limit,Long offset);
	
	public boolean save(CarePlans cp); 
	
	public boolean update(CarePlans cp);
	
	public List<CarePlans> findByCareGiverIdUsingSearch(Long id, Long limit,Long offset, String label, String keyword) ;

	public CarePlans findByUserID(Long id);
	
	public int byCountUserId(Long id);
	
	public CarePlans findByUserIdAndPatientId(Long careGiverId,Long patientId);
	
}
