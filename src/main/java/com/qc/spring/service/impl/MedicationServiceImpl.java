package com.qc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.MedicationDAO;
import com.qc.spring.entities.Medications;
import com.qc.spring.service.MedicationService;

@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

	@Autowired
	private MedicationDAO medicationDAO;
	
	@Override
	public Medications findByID(Long id) {
		
		return medicationDAO.byID(id);
	}

	@Override
	public Long save(Medications med) {
		
		return medicationDAO.save(med);
	}

	@Override
	public boolean update(Medications med) {
		
		return medicationDAO.update(med);
	}
	
	@Override
	public boolean remove(Medications med) {
		
		return medicationDAO.remove(med);
	}

	@Override
	public List<Medications> findBySlotAndDate(String slot, String date) {
		
		return medicationDAO.bySlotAndDate(slot, date);
	}

	@Override
	public List<Medications> findBySlotAndDateWeekly(String slot, String date) {
		
		return medicationDAO.bySlotAndDate(slot, date);
	}

	@Override
	public Medications merge(Medications med) {
		
		return medicationDAO.merge(med);
	}

	@Override
	public Medications findByIDAndUserId(Long id, Long userId) {
		
		return medicationDAO.byIDAndUserId(id, userId);
	}

	@Override
	public int removeByMed(Long medId) {
		
		return medicationDAO.removeByMed(medId);
	}

	@Override
	public List<Medications> findByUserId(Long userId, Long limit, Long offset) {
		
		return medicationDAO.byUserId(userId, limit, offset);
	}
}
