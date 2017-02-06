package com.qc.spring.service;

import com.qc.spring.entities.Users;

public interface UserService {
	
	public Users findByID(Long id) ;

	public Users findByEmailIdAndPassword(String emailId, String password);
	
	public Users findByEmailId(String emailId);
	
	public boolean save(Users user);
	
	public boolean update(Users user); 

	public Users findByForgetToken(String forgetToken) ;
	
	public Users findByMemberIdAndDOB(String memberId,String dob) ;

}
