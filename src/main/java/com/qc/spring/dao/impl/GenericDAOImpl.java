package com.qc.spring.dao.impl;

import java.io.Serializable;
import java.util.List;

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
import com.qc.spring.dao.GenericDAO;
import com.qc.spring.entities.States;


@Repository
@Transactional
public class GenericDAOImpl implements GenericDAO {

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(GenericDAOImpl.class);
	
	
	@Override
	public <T> T find(Class<T> type, Serializable id) {
		
		return null;
	}

	@Override
	public <T> T[] find(Class<T> type, Serializable... ids) {
		
		return null;
	}

	@Override
	public <T> T getReference(Class<T> type, Serializable id) {
		
		return null;
	}

	@Override
	public <T> T[] getReferences(Class<T> type, Serializable... ids) {
		
		return null;
	}

	@Override
	@Transactional
	public Serializable save(Object entity) {
		
		Session session=null;
		Transaction transaction=null;
		Long id1 =null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			id1 = (Long)session.save(entity);
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return id1;
	}

	@Override
	public Serializable[] save(Object... entities) {
		
		return null;
	}

	@Override
	public boolean remove(Object entity) {
		
		return false;
	}

	@Override
	public void remove(Object... entities) {
		
		
	}

	@Override
	public boolean removeById(Class<?> type, Serializable id) {
		
		return false;
	}

	@Override
	public void removeByIds(Class<?> type, Serializable... ids) {
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(Class<T> type) {
		
		List<T> listValues=null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			listValues=(List<T>)session.createCriteria(type).list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return listValues;
	}

	@Override
	public States findByStateName(String state) {
	
		States states = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM States WHERE state=?";
			query = session.createQuery(sql);
			query.setString(0, state);
			LOGGER.info("Executing: "+sql);
			states = (States) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return states;
	}

}
