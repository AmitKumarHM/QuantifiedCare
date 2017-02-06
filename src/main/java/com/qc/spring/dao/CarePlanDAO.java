package com.qc.spring.dao;

import java.util.List;

import com.qc.spring.entities.CarePlans;

public interface CarePlanDAO {

	public CarePlans byID(Long id);
	
	public List<CarePlans> byCareGiverId(Long id,Long limit,Long offset);
	
	public boolean save(CarePlans cp); 
	
	public boolean update(CarePlans cp);
	
	public List<CarePlans> byCareGiverIdUsingSearch(Long id, Long limit,Long offset, String label, String keyword) ;

	public CarePlans byUserID(Long id);
	
	public int byCountUserId(Long id);
	
	public CarePlans byUserIdAndPatientId(Long careGiverId,Long patientId);
	
}
