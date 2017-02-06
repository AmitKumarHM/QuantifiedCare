package com.qc.spring.dao.impl;

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
import com.qc.spring.dao.CarePlanDAO;
import com.qc.spring.entities.CarePlans;

@Repository
@Transactional
public class CarePlanDAOImpl implements CarePlanDAO{

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	/** The _logger. */
	private static Log LOGGER = LogFactory.getLog(CarePlanDAOImpl.class);
	
	/**
	 * By id.
	 *
	 * @param id the id
	 * @return the users
	 */
	@Transactional
	public CarePlans byID(Long id) {

		CarePlans cp = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM CarePlans WHERE carePlanId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			cp = (CarePlans) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return cp;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<CarePlans> byCareGiverId(Long id,Long limit,Long offset) {

		List<CarePlans> lcp = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM CarePlans WHERE caregiver_id=?";
			query = session.createQuery(sql);
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			lcp = (List<CarePlans>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return lcp;
	}

	
	/**
	 * Save.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	@Transactional
	public boolean save(CarePlans cp) {

		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(cp);
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
	public boolean update(CarePlans cp) {

		boolean status =  false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(cp);
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
	@SuppressWarnings("unchecked")	
	public List<CarePlans> byCareGiverIdUsingSearch(Long id, Long limit,
			Long offset, String label, String keyword) {
		List<CarePlans> lusr = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM CarePlans WHERE user.userId=? and LOWER(patient."+label +") like '%"+keyword+"%' ORDER BY created_date DESC";
			query = session.createQuery(sql);
			query.setLong(0, id);
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());
			LOGGER.info("Executing: "+sql);
			lusr = (List<CarePlans>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.info(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return lusr;
	}
	
	@Override
	@Transactional
	public CarePlans byUserID(Long id) {
		CarePlans cp = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM CarePlans WHERE patient_id=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			cp = (CarePlans) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return cp;
	}

	@Override
	public int byCountUserId(Long id) {
		
		Long countPatients=null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "SELECT count(*) FROM CarePlans WHERE caregiver_id=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			countPatients= (Long)query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return countPatients.intValue();
	}

	@Override
	public CarePlans byUserIdAndPatientId(Long careGiverId, Long patientId) {
		CarePlans cp = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM CarePlans WHERE patient_id=? and caregiver_id=?";
			query = session.createQuery(sql);
			query.setLong(0, patientId);
			query.setLong(1, careGiverId);
			LOGGER.info("Executing: "+sql);
			cp = (CarePlans) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return cp;
	}
	
}
