package com.qc.spring.dao;

import com.qc.spring.entities.PinAuthentication;

public interface PinAuthnDAO {

public boolean save(PinAuthentication pinAuthn); 

public boolean update(PinAuthentication pinAuthn);

public PinAuthentication byKeyAndUser(String key,Long userId);
	
}
