package com.qc.spring.dao;

import com.qc.spring.entities.AccessTokens;

public interface AccessTokenDAO {

	public AccessTokens byToken(String accessToken);
	
	public boolean save(AccessTokens accessToken); 
	
	public boolean remove(AccessTokens accessToken) ;
	
	public boolean removeByUserID(Long userId);
}
