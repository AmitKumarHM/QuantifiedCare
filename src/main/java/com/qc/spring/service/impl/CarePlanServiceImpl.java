package com.qc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.CarePlanDAO;
import com.qc.spring.entities.CarePlans;
import com.qc.spring.service.CarePlanService;
@Service
@Transactional
public class CarePlanServiceImpl implements CarePlanService {

	@Autowired
	private CarePlanDAO carePlanDAO;
	
	@Override
	public CarePlans findByID(Long id) {
		
		return carePlanDAO.byID(id);
	}

	@Override
	public List<CarePlans> findByCareGiverId(Long id, Long limit, Long offset) {
		
		return carePlanDAO.byCareGiverId(id, limit, offset);
	}

	@Override
	public boolean save(CarePlans cp) {
		
		return carePlanDAO.save(cp);
	}

	@Override
	public boolean update(CarePlans cp) {
		
		return carePlanDAO.update(cp);
	}

	@Override
	public List<CarePlans> findByCareGiverIdUsingSearch(Long id, Long limit,
			Long offset, String label, String keyword) {
		
		return carePlanDAO.byCareGiverIdUsingSearch(id, limit, offset, label, keyword);
	}

	@Override
	public CarePlans findByUserID(Long id) {
		
		return carePlanDAO.byUserID(id);
	}

	@Override
	public int byCountUserId(Long id) {
		
		return carePlanDAO.byCountUserId(id);
	}

	@Override
	public CarePlans findByUserIdAndPatientId(Long careGiverId, Long patientId) {
		
		return carePlanDAO.byUserIdAndPatientId(careGiverId, patientId);
	}
	
}
