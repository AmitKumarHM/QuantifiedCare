package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.SlotDAO;
import com.qc.spring.entities.Slot;
import com.qc.spring.service.SlotService;

@Service
@Transactional
public class SlotServiceImpl implements SlotService {

	@Autowired
	private SlotDAO slotDAO;
	
	
	@Override
	public Slot findBySlotAndFrequency(String slot, String frequency) {
		
		return slotDAO.bySlotAndFrequency(slot, frequency);
	}

}
