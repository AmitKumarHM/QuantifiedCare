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
import com.qc.spring.dao.MessageDAO;
import com.qc.spring.entities.Messages;

@Repository
@Transactional
public class MessageDAOImpl implements MessageDAO{

	/** The env. */
	@Autowired
	Configuration env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	/** The _logger. */
	private static Log LOGGER = LogFactory.getLog(MessageDAOImpl.class);
	
	@Transactional
	public Messages byID(Long id) {

		Messages med = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Messages WHERE messageId=? and deleted=1";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			med = (Messages) query.uniqueResult();
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
	public boolean save(Messages messages) {

		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(messages);
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
	public Messages merge(Messages messages) {

		Messages mergeMessages=null;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			mergeMessages=(Messages)session.merge(messages);
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return mergeMessages;
	}
	
	/**
	 * Update.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	@Transactional
	public boolean update(Messages message) {

		
		boolean status =  false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(message);
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
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Messages> getByUserIdAndSearch(Long id, Long limit,Long offset, String label, String keyword,Long flag) {
		List<Messages> lusr = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			
			if(flag != null && (flag.shortValue() == 0 || flag.shortValue() == 1)) {
				if(label.equals("inbox")) {	
				    sql = "FROM Messages WHERE (label=? and toUser.userId=? and flag=? and deleted=1)"+" and (body LIKE '%"+keyword+"%'"+" or subject LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%') ORDER BY created_date DESC";}
				
				else if(label.equals("sent")){
					sql = "FROM Messages WHERE (label=? and fromUser.userId=? and flag=? and deleted=1)"+" and (body LIKE '%"+keyword+"%'"+" or subject LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%') ORDER BY created_date DESC";	
				
				}else if(label.equals("archive")){
					sql = "FROM Messages WHERE (label=? and fromUser.userId=? and flag=? and deleted=1)"+" and (body LIKE '%"+keyword+"%'"+" or subject LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%') ORDER BY created_date DESC";
				
				}
			}else {
				if(label.equals("inbox")) {	
				    sql = "FROM Messages WHERE (label=? and toUser.userId=? and deleted=1)"+" and (body LIKE '%"+keyword+"%'"+" or subject LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%') ORDER BY created_date DESC";}
				
				else if(label.equals("sent")){
					sql = "FROM Messages WHERE (label=? and fromUser.userId=? and deleted=1)"+" and (body LIKE '%"+keyword+"%'"+" or subject LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%') ORDER BY created_date DESC";	
				
				}else if(label.equals("archive")){
					sql = "FROM Messages WHERE (label=? and fromUser.userId=? and deleted=1)"+" and (body LIKE '%"+keyword+"%'"+" or subject LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%' or toUser.emailId LIKE '%"+keyword+"%') ORDER BY created_date DESC";
				}
			}
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			query = session.createQuery(sql);
			query.setString(0, label.toLowerCase());
			query.setLong(1, id);
			if(flag != null && (flag.shortValue() == 0 || flag.shortValue() == 1)) {
				query.setShort(2,flag.shortValue());
			}
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());
			LOGGER.info("Executing: "+sql);
			lusr = (List<Messages>) query.list();
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
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Messages> getByUserId(Long id, Long limit,Long offset, String label,Long flag) {
		List<Messages> lusr = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			if(flag != null && (flag.shortValue() == 0 || flag.shortValue() == 1)) {
				if(label.equals("inbox")) {	
				    sql = "FROM Messages WHERE label=? and toUser.userId=? and flag=? and deleted=1 ORDER BY created_date DESC";}
				else if(label.equals("sent")){
					sql = "FROM Messages WHERE label=? and fromUser.userId=? and flag=? and deleted=1 ORDER BY created_date DESC";	
				}else if(label.equals("archive")){
					sql = "FROM Messages WHERE label=? and fromUser.userId=? and flag=? and deleted=1 ORDER BY created_date DESC";
				}
			}else {
				if(label.equals("inbox")) {	
				    sql = "FROM Messages WHERE label=? and toUser.userId=? and deleted=1 ORDER BY created_date DESC";}
				else if(label.equals("sent")){
					sql = "FROM Messages WHERE label=? and fromUser.userId=? and deleted=1 ORDER BY created_date DESC";	
				}else if(label.equals("archive")){
					sql = "FROM Messages WHERE label=? and fromUser.userId=? and deleted=1 ORDER BY created_date DESC";
				}
			}
			transaction=session.beginTransaction();
			query = session.createQuery(sql);
			query.setString(0, label.toLowerCase());
			query.setLong(1, id);
			if(flag != null && (flag.shortValue() == 0 || flag.shortValue() == 1)) {
				query.setShort(2,flag.shortValue());
			}
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());
			LOGGER.info("Executing: "+sql);
			lusr = (List<Messages>) query.list();
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
	
}
