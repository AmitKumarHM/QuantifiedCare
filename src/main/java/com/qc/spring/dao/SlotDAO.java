package com.qc.spring.dao;

import com.qc.spring.entities.Slot;

public interface SlotDAO {

	public Slot bySlotAndFrequency(String slot, String frequency);
	
}
