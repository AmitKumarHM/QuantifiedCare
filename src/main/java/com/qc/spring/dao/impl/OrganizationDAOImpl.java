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
import com.qc.spring.dao.OrganizationDAO;
import com.qc.spring.entities.Organizations;

@Repository
@Transactional
public class OrganizationDAOImpl implements OrganizationDAO{

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;

	/** The _logger. */
	private static Log LOGGER = LogFactory.getLog(OrganizationDAOImpl.class);
	
	/**
	 * By id.
	 *
	 * @param id the id
	 * @return the users
	 */
	@Transactional
	public Organizations byID(Long id) {

		Organizations org = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Organizations WHERE orgId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			org = (Organizations) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return org;
	}

	/**
	 * Save.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	@Transactional
	public boolean save(Organizations org) {

		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(org);
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
	@Transactional
	public boolean update(Organizations org) {

	
		boolean status =  false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(org);
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
}
