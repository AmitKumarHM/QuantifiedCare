package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.PinAuthnDAO;
import com.qc.spring.entities.PinAuthentication;
import com.qc.spring.service.PinAuthnService;

@Service
@Transactional
public class PinAuthnServiceImpl implements PinAuthnService {

	@Autowired
	private PinAuthnDAO pinAuthnDAO;
	
	@Override
	public boolean save(PinAuthentication pinAuthn) {
		
		return pinAuthnDAO.save(pinAuthn);
	}

	@Override
	public boolean update(PinAuthentication pinAuthn) {
	
		return pinAuthnDAO.update(pinAuthn);
	}

	@Override
	public PinAuthentication findByKeyAndUser(String key, Long userId) {
		
		return pinAuthnDAO.byKeyAndUser(key, userId);
	}

}
