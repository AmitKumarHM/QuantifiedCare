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
import com.qc.spring.dao.SlotDAO;
import com.qc.spring.entities.Slot;

@Repository
@Transactional
public class SlotDAOImpl implements SlotDAO {

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(SlotDAOImpl.class);
	
	@Override
	@Transactional
	public Slot bySlotAndFrequency(String slot, String frequency) {
		
		String sql = null;
		Query query = null;
		Slot slotRef = null;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Slot WHERE slot=? and frequency=?";
			query = session.createQuery(sql);
			query.setString(0, slot.trim());
			query.setString(1, frequency.trim());
			LOGGER.info("Executing: "+sql);
			slotRef = (Slot) query.uniqueResult();
			transaction.commit();
		} catch (Exception e) {
			LOGGER.error(e);
		}	
		finally {
			if(session != null){
			session.close();	
			}
		}
		return slotRef;
	}

}
