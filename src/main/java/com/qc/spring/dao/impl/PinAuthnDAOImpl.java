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
import com.qc.spring.dao.PinAuthnDAO;
import com.qc.spring.entities.PinAuthentication;


@Repository
@Transactional
public class PinAuthnDAOImpl implements PinAuthnDAO {

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;

	/** The _logger. */
	private static Log LOGGER = LogFactory.getLog(PinAuthnDAOImpl.class);
	
	@Override
	@Transactional
	public boolean save(PinAuthentication pinAuthn) {
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(pinAuthn);
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

	@Override
	public boolean update(PinAuthentication pinAuthn) {
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(pinAuthn);
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

	@Override
	public PinAuthentication byKeyAndUser(String key, Long userId) {
		PinAuthentication pinAuthn = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM PinAuthentication WHERE key=? and user.userId=?";
			query = session.createQuery(sql);
			query.setString(0, key);
			query.setLong(1, userId);
			LOGGER.info("Executing: "+sql);
			pinAuthn = (PinAuthentication) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return pinAuthn;
	}

}
