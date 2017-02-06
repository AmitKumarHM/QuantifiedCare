package com.qc.spring.service;

import com.qc.spring.entities.AccessTokens;

public interface AccessTokenService {

	public AccessTokens findByToken(String accessToken);
	
	public boolean save(AccessTokens accessToken); 
	
	public boolean remove(AccessTokens accessToken) ;
	
	public boolean removeByUserID(Long userId);
}
