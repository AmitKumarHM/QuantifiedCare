package com.qc.spring.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.configs.Configuration;
import com.qc.spring.dao.UserDAO;
import com.qc.spring.entities.Users;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO{

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(UserDAOImpl.class);
	
	/**
	 * By id.
	 *
	 * @param id the id
	 * @return the users
	 */
	@Transactional
	public Users byID(Long id) {

		Users usr = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Users WHERE userId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			usr = (Users) query.uniqueResult();
            transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}
		finally {
			if(session != null){
			session.close();	
			}
		}
		
		return usr;
	}

	/**
	 * By email id and password.
	 *
	 * @param emailId the email id
	 * @param password the password
	 * @return the users
	 */
	@Transactional
	public Users byEmailIdAndPassword(String emailId, String password) {

		Users usr = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Users WHERE LOWER(emailId)=LOWER(?) AND password=?";
			query = session.createQuery(sql);
			query.setString(0, emailId);
			query.setString(1, password);
			LOGGER.info("Executing: "+sql);
			usr = (Users) query.uniqueResult();
			transaction.commit();	
		} catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return usr;
	}

	/**
	 * By email id.
	 *
	 * @param emailId the email id
	 * @return the users
	 */
	@Transactional
	public Users byEmailId(String emailId) {

		Users usr = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Users WHERE LOWER(emailId)=LOWER(?)";
			query = session.createQuery(sql);
			query.setString(0, emailId.trim());
			LOGGER.info("Executing: "+sql);
			usr = (Users) query.uniqueResult();
			transaction.commit();

		} catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return usr;
	}

	/**
	 * Save.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	@Transactional
	public boolean save(Users user) {

		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(user);
			transaction.commit();
			status = true;
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return status;
	}

	/**
	 * Update.
	 *
	 * @param user the user
	 * @return true, if successful
	 */

	public boolean update(Users user) {

		boolean status =  false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(user);
			transaction.commit();
			status = true;
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return status;
	}


	/**
	 * By forget token.
	 *
	 * @param forgetToken the forget token
	 * @return the users
	 */
	@Transactional
	public Users byForgetToken(String forgetToken) {

		Query query = null;
		String sql = null;
		Users user = null;
		Session session=null;
		Transaction transaction=null;
		try {	
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Users WHERE forgetToken=?";
			query = session.createQuery(sql);
			query.setString(0, forgetToken);
			LOGGER.info("Executing: "+sql);
			user = (Users) query.uniqueResult();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return user;
	}

	@Override
	public Users findByMemberIdAndDOB(String memberId, String dob) {
		
		Query query = null;
		String sql = null;
		Users user = null;
		Session session=null;
		Transaction transaction=null;
		DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		try {	
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Users WHERE memberId=? and DATE(dateOfBirth)=?";
			query = session.createQuery(sql);
			Date d=formatter.parse(dob);
			dob=formatter.format(d);
			query.setString(0, memberId);
			query.setString(1, dob);
			LOGGER.info("Executing: "+sql);
			user = (Users) query.uniqueResult();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return user;
	}

}
