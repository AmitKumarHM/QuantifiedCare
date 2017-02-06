package com.qc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.MessageDAO;
import com.qc.spring.entities.Messages;
import com.qc.spring.service.MessageService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDAO;
	
	@Override
	public Messages findByID(Long id) {
		
		return messageDAO.byID(id);
	}

	@Override
	@Transactional
	public boolean save(Messages messages) {
		
		return messageDAO.save(messages);
	}

	@Override
	public Messages merge(Messages messages) {
		
		return messageDAO.merge(messages);
	}

	@Override
	public boolean update(Messages message) {
		
		return messageDAO.update(message);
	}

	@Override
	public List<Messages> findByUserIdAndSearch(Long id, Long limit,
			Long offset, String label, String keyword,Long flag) {
		
		return messageDAO.getByUserIdAndSearch(id, limit, offset, label, keyword,flag);
	}

	@Override
	public List<Messages> findByUserId(Long id, Long limit, Long offset,
			String label,Long flag) {
		
		return messageDAO.getByUserId(id, limit, offset, label,flag);
	}

}
