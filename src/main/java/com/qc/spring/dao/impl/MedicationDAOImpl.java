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
import com.qc.spring.dao.MedicationDAO;
import com.qc.spring.entities.Medications;

@Repository
@Transactional
public class MedicationDAOImpl implements MedicationDAO{

	/** The env. */
	@Autowired
	Configuration env;

	@Autowired
	SessionFactory sessionFactory;
	
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(MedicationDAO.class);
	
	/**
	 * By id.
	 *
	 * @param id the id
	 * @return the users
	 */
	@Transactional
	public Medications byID(Long id) {

		Medications med = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Medications WHERE medicationId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			med = (Medications) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return med;
	}
	
	/**
	 * By id.
	 *
	 * @param id the id
	 * @return the users
	 */
	@Transactional
	public Medications byIDAndUserId(Long id, Long userId) {

		Medications med = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Medications WHERE medicationId=? AND user_id=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			query.setLong(1, userId);
			LOGGER.info("Executing: "+sql);
			med = (Medications) query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return med;
	}

	/**
	 * Save.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	@Transactional
	public Long save(Medications med) {

		Session session=null;
		Transaction transaction=null;
		Long id1 =null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			id1 = (Long)session.save(med);
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

	/**
	 * Update.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	@Transactional
	public boolean update(Medications med) {

		boolean status =  false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(med);
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
	@Transactional
	public boolean remove(Medications med) {
		
		Session session = null;
		boolean status =  false;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.delete(med);
			transaction.commit();
			status = true;
		}catch(Exception e){
			LOGGER.error(e);
			status =  false;
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return status;
	}


	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Medications> bySlotAndDate(String slot, String date) {
		
		List<Medications> lusr = null;
		String sql = null;
		Query query = null;
		Session session = null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Medications WHERE ? between start_date and end_date and slot.slot=? and (slot.frequency=? or slot.frequency=?)";
			query = session.createQuery(sql);
			query.setString(0, date);
			query.setString(1, slot);
			query.setString(2, "Daily");
			query.setString(3, "AsNeeded");
			LOGGER.info("Executing: "+sql);
			lusr = (List<Medications>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return lusr;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Medications> bySlotAndDateWeekly(String slot, String date) {
		List<Medications> lusr = null;
		String sql = null;
		Query query = null;
		Session session = null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Medications WHERE ? between start_date and end_date and slot.slot=? and slot.frequency=?";
			query = session.createQuery(sql);
			query.setString(0, date);
			query.setString(1, slot);
			query.setString(2, "Weekly");
			LOGGER.info("Executing: "+sql);
			lusr = (List<Medications>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return lusr;
	}

	@Override
	@Transactional
	public Medications merge(Medications med) {
		Medications medications=null;
		Session session = null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			medications=(Medications)session.merge(med);
			transaction.commit();
		}catch(Exception e){
			LOGGER.info(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return medications;
	}
	
	@Transactional
	public int removeByMed(Long medId) {
		
		int result=0;
		Query query = null;
		String sql = null;
		Session session = null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "DELETE FROM Medications WHERE medication_id=?";
			query=session.createQuery(sql);
			query.setLong(0, medId);
			LOGGER.info("Executing: "+sql);
			result=query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Medications> byUserId(Long userId, Long limit, Long offset) {
		List<Medications> medList = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Medications WHERE user_id=?";
			query = session.createQuery(sql);
			query.setLong(0, userId);
			if(limit != null && offset != null) {
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());}
			LOGGER.info("Executing: "+sql);
			medList = (List<Medications>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return medList;
	}
}
