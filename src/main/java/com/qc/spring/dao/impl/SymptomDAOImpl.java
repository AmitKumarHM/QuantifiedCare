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
import com.qc.spring.dao.SymptomDAO;
import com.qc.spring.entities.Symptoms;


/**
 * The Class SymptomDAO.
 */
@Repository
@Transactional
public class SymptomDAOImpl implements SymptomDAO{

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(SymptomDAOImpl.class);
	
	@Transactional
	public Symptoms byId(Long id) {
		
		String sql = null;
		Query query = null;
		Session session=null;
		Symptoms symptoms = null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Symptoms WHERE symptomsId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			symptoms = (Symptoms) query.uniqueResult();
			transaction.commit();
		} catch (Exception e) {
			LOGGER.error(e);
		}
		finally {
			if(session != null){
			session.close();	
			}
		}
		
		return symptoms;
	}
	
	@Transactional
	public Symptoms byUserId(Long id) {
		
		String sql = null;
		Query query = null;
		Session session=null;
		Symptoms symptoms = null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Symptoms WHERE user.userId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			symptoms = (Symptoms) query.uniqueResult();
			transaction.commit();
		} catch (Exception e) {
			LOGGER.error(e);
		}	finally {
			if(session != null){
			session.close();	
			}
		}
		
		return symptoms;
	}
	
	
	
	@Transactional
	public boolean save(Symptoms symptoms) {
	
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(symptoms);
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
	
	@Transactional
	public boolean update(Symptoms symptoms) {
		
		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(symptoms);
			transaction.commit();
			status = true;
		
		}catch(Exception e){
			LOGGER.error(e);
		}
		finally {
			if(session != null){
			session.close();	
			}
		}
		return status;
	}
	
	
	@Transactional
	public int removeByPatientId(Long userId) {
		
		int result=0;
		Query query = null;
		String sql = null;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "DELETE FROM Symptoms WHERE user_id=?";
			query=session.createQuery(sql);
			query.setLong(0, userId);
			LOGGER.info("Executing: "+sql);
			result=query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}	finally {
			if(session != null){
			session.close();	
			}
		}
		
		return result;
	}
		
}
