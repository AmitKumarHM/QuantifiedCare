package com.qc.spring.service;

import java.util.List;

import com.qc.spring.entities.Messages;

public interface MessageService {

	public Messages findByID(Long id);

	public boolean save(Messages messages); 
	
	public Messages merge(Messages messages);
	
	public boolean update(Messages message);
	
	public List<Messages> findByUserIdAndSearch(Long id, Long limit,Long offset, String label, String keyword,Long flag);
	
	public List<Messages> findByUserId(Long id, Long limit,Long offset, String label,Long flag);
	
	
}
