package com.qc.spring.service;

import com.qc.spring.entities.Slot;

public interface SlotService {

	public Slot findBySlotAndFrequency(String slot, String frequency);
}
