package com.qc.spring.dao.impl;

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
import com.qc.spring.dao.AccessTokenDAO;
import com.qc.spring.entities.AccessTokens;


/**
 * The Class AccessTokenDAOImpl.
 */
@Repository
@Transactional
public class AccessTokenDAOImpl implements AccessTokenDAO{
	
	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;

	/** The _logger. */
	private static Log LOGGER = LogFactory.getLog(AccessTokenDAOImpl.class);
	
	/**
	 * By token.
	 *
	 * @param accessToken the access token
	 * @return the access tokens
	 */
	@Transactional
	public AccessTokens byToken(String accessToken) {

		String sql = null;
		Query query = null;
		AccessTokens at = null;
		Session session=null;
		Transaction transaction=null;
		
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM AccessTokens WHERE accessToken=?";
			query = session.createQuery(sql);
			query.setString(0, accessToken);
			LOGGER.info("Executing: "+sql);
			at = (AccessTokens) query.uniqueResult();
			transaction.commit();
			LOGGER.info("Access Token retrieve successfully "+at);
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return at;
	}
	
	/**
	 * Save.
	 *
	 * @param accessToken the access token
	 * @return the long
	 */
	@Transactional
	public boolean save(AccessTokens accessToken) {
		
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(accessToken);
			status = true;
			transaction.commit();
			LOGGER.info("Access Token save successfully "+accessToken);
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
	 * Removes the.
	 *
	 * @param accessToken the access token
	 * @return true, if successful
	 */
	@Transactional
	public boolean remove(AccessTokens accessToken) {
		
		String sql = null;
		Query query = null;
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "DELETE AccessTokens WHERE accessToken=?";
			query = session.createQuery(sql);
			query.setParameter(0, accessToken.getAccessToken());
			LOGGER.info("Executing: "+sql);
			query.executeUpdate();
			transaction.commit();
			status = true;
			LOGGER.info("Access Token remove successfully "+ accessToken);
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
			return status;
	}
	
	@Transactional
	public boolean removeByUserID(Long userId) {
		
		String sql = null;
		Query query = null;
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "DELETE AccessTokens WHERE user_id=?";
			query = session.createQuery(sql);
			query.setParameter(0, userId);
			LOGGER.info("Executing: "+sql);
			query.executeUpdate();
			transaction.commit();
			status = true;
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return status;
	}
}