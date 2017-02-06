package com.qc.spring.service;

import java.util.List;

import com.qc.spring.entities.Medications;

public interface MedicationService {

	public Medications findByID(Long id);
	
	public Medications findByIDAndUserId(Long id, Long userId);

	public Long save(Medications med);

	public boolean update(Medications med);
	
	public boolean remove(Medications med);

	public Medications merge(Medications med);
	
	public List<Medications> findBySlotAndDate(String slot, String date);
	
	public List<Medications> findBySlotAndDateWeekly(String slot, String date);
	
	public List<Medications> findByUserId(Long userId, Long limit,Long offset);

	public int removeByMed(Long medId);
}
