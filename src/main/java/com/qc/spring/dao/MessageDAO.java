package com.qc.spring.dao;

import java.util.List;

import com.qc.spring.entities.Messages;

public interface MessageDAO {

	public Messages byID(Long id);

	public boolean save(Messages messages); 
	
	public Messages merge(Messages messages);
	
	public boolean update(Messages message);
	
	public List<Messages> getByUserIdAndSearch(Long id, Long limit,Long offset, String label, String keyword,Long flag);
	
	public List<Messages> getByUserId(Long id, Long limit,Long offset, String label,Long flag);
	
	
}
