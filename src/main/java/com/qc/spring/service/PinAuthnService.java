package com.qc.spring.service;

import com.qc.spring.entities.PinAuthentication;

public interface PinAuthnService {
	public boolean save(PinAuthentication pinAuthn); 

	public boolean update(PinAuthentication pinAuthn);
	
	public PinAuthentication findByKeyAndUser(String key,Long userId);
}
