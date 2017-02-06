package com.qc.spring.dao;

import com.qc.spring.entities.Users;

public interface UserDAO {
	
	public Users byID(Long id) ;

	public Users byEmailIdAndPassword(String emailId, String password);

	public Users byEmailId(String emailId);
	
	public boolean save(Users user);
	
	public boolean update(Users user); 

	public Users byForgetToken(String forgetToken) ;
	
	public Users findByMemberIdAndDOB(String memberId,String dob) ;
}
