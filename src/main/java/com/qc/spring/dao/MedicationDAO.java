package com.qc.spring.dao;

import java.util.List;

import com.qc.spring.entities.Medications;

public interface MedicationDAO {

	public Medications byID(Long id);
	
	public Medications byIDAndUserId(Long id, Long userId);

	public Long save(Medications med);

	public boolean update(Medications med);
	
	public boolean remove(Medications med);
	
	public Medications merge(Medications med);
	
	public List<Medications> bySlotAndDate(String slot, String date);

	public List<Medications> bySlotAndDateWeekly(String slot, String date);
	
	public int removeByMed(Long medId);
	
	public List<Medications> byUserId(Long userId, Long limit,Long offset);
}
