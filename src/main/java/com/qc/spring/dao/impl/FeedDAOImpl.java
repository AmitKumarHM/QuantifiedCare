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
import com.qc.spring.dao.FeedDAO;
import com.qc.spring.entities.Feeds;

@Repository
@Transactional
public class FeedDAOImpl implements FeedDAO {

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(FeedDAOImpl.class);
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Feeds> byUserId(Long id) {

		List<Feeds> lf = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Feeds WHERE patient_id=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			lf = (List<Feeds>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return lf;
	}
}
