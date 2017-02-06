package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.AccessTokenDAO;
import com.qc.spring.entities.AccessTokens;
import com.qc.spring.service.AccessTokenService;

@Service
@Transactional
public class AccessTokenServiceImpl implements AccessTokenService {

	@Autowired
	private AccessTokenDAO accessTokenDAO;
	
	@Override
	public AccessTokens findByToken(String accessToken) {
		
		return accessTokenDAO.byToken(accessToken);
	}

	@Override
	@Transactional
	public boolean save(AccessTokens accessToken) {
		
		return accessTokenDAO.save(accessToken);
	}

	@Override
	@Transactional
	public boolean remove(AccessTokens accessToken) {
		
		return accessTokenDAO.remove(accessToken);
	}

	@Override
	@Transactional
	public boolean removeByUserID(Long userId) {

		return accessTokenDAO.removeByUserID(userId);
	}

}
