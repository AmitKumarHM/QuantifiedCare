package com.qc.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.UserDAO;
import com.qc.spring.entities.Users;
import com.qc.spring.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public Users findByID(Long id) {
		return userDAO.byID(id);
	}

	@Override
	public Users findByEmailIdAndPassword(String emailId, String password) {
		
		return userDAO.byEmailIdAndPassword(emailId, password);
	}

	@Override
	public Users findByEmailId(String emailId) {
		
		return userDAO.byEmailId(emailId);
	}

	@Override
	@Transactional
	public boolean save(Users user) {
		
		return userDAO.save(user);
	}

	@Override
	@Transactional
	public boolean update(Users user) {
		
		return userDAO.update(user);
	}

	@Override
	public Users findByForgetToken(String forgetToken) {
		
		return userDAO.byForgetToken(forgetToken);
	}

	@Override
	public Users findByMemberIdAndDOB(String memberId, String dob) {
		
		return userDAO.findByMemberIdAndDOB(memberId, dob);
	}

}
