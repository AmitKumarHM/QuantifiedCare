package com.qc.spring.dao.impl;

import java.util.Iterator;
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
import com.qc.spring.dao.ScheduleDAO;
import com.qc.spring.entities.Schedule;

@Repository
@Transactional
public class ScheduleDAOImpl implements ScheduleDAO {

	/** The env. */
	@Autowired
	Configuration env;

	@Autowired
	SessionFactory sessionFactory;

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(ScheduleDAOImpl.class);


	@Override
	public boolean save(Schedule schedule) {

		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.save(schedule);
			transaction.commit();
			status=true;
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
	public Schedule merge(Schedule schedule) {

		Session session=null;
		Schedule sch=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sch=(Schedule) session.merge(schedule);
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return sch;
	}

	@Override
	public int removeByMedId(Long medId) {

		int result=0;
		Query query = null;
		String sql = null;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "DELETE FROM Schedule WHERE medication_id=?";
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
	public Schedule byID(Long id) {
		Schedule cp = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Schedule WHERE scheduleId=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			cp = (Schedule) query.uniqueResult();
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
	public List<Schedule> byMedId(Long id) {
		List<Schedule> listSch = null;
		String sql = null;
		Query query = null;
		Session session=null;
		Transaction transaction=null;
		try{
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Schedule WHERE medication_id=?";
			query = session.createQuery(sql);
			query.setLong(0, id);
			LOGGER.info("Executing: "+sql);
			listSch = (List<Schedule>) query.list();
			transaction.commit();
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return listSch;
	}
	
	

	@Override
	public boolean update(Schedule schedule) {

		boolean status = false;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			session.merge(schedule);
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
	@Override
	@Transactional
	public List<Schedule> byMedicationsAndDate(Long medId, String date) {
		String sql = null;
		Query query = null;
		List<Schedule> schedulesList = null;
		Session session=null;
		Transaction transaction=null;

		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Schedule WHERE medication_id=? and DATE(scheduled_date)=? order by TIME(scheduled_date) asc";
			query = session.createQuery(sql);
			query.setLong(0, medId);
			query.setString(1, date);
			LOGGER.info("Executing: "+sql);
			schedulesList = (List<Schedule>) query.list();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		
		return schedulesList;
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public List<Schedule> getByUserAndDate(Long userId, Long limit, Long offset,String trackDate) {

		String sql = null;
		Query query = null;
		List<Schedule> schedulesList = null;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Schedule WHERE medications.user.userId=? and DATE(scheduled_date)<=? order by DATE(scheduled_date) desc,Time(scheduled_date) asc";
			query = session.createQuery(sql);
			query.setLong(0, userId);
			query.setString(1, trackDate);
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());
			LOGGER.info("Executing: "+sql);
			schedulesList=(List<Schedule>)query.list();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return schedulesList;

	}
	
	@SuppressWarnings({"unchecked"})
	public int addReminder(Long userId,Schedule schedule) {

		int noOfRowsUpdated = 0;
		Query query = null;
	    String sql=null;
		Session session=null;
		List<Schedule> scheduleList=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			if(schedule.getDateAlarm() != null) {
				sql = "FROM Schedule WHERE medications.user.userId=? and TIME(scheduled_date)=? and DATE(scheduled_date)=?";
			}
			else {
				sql = "FROM Schedule WHERE medications.user.userId=? and TIME(scheduled_date)=?";
			}
			query = session.createQuery(sql);
			query.setLong(0, userId);
			query.setString(1, schedule.getAlarmTime());
			if(schedule.getDateAlarm() != null){
				query.setString(2, schedule.getDateAlarm());
	             }
			LOGGER.info("Executing: "+sql);
			scheduleList=(List<Schedule>)query.list();
			Iterator<Schedule> itrSchedule=scheduleList.iterator();
			while(itrSchedule.hasNext()){
				Schedule sch=itrSchedule.next();
				sch.setFlag(schedule.getFlag());
				sch.setReminderTime(schedule.getReminderTime());
				if(schedule.getDateAlarm() == null){
				             sch.setDays((short)1);
				             }
			}
			noOfRowsUpdated=scheduleList.size();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return noOfRowsUpdated;

	}
	
	
	
	@SuppressWarnings({"unchecked"})
	public List<Schedule> getByUser(Long userId, Long limit, Long offset) {

		String sql = null;
		Query query = null;
		List<Schedule> schedulesList = null;
		Session session=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
			sql = "FROM Schedule WHERE medications.user.userId=?";
			query = session.createQuery(sql);
			query.setLong(0, userId);
			query.setMaxResults(limit.intValue());
			query.setFirstResult(offset.intValue());
			LOGGER.info("Executing: "+sql);
			schedulesList=(List<Schedule>)query.list();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return schedulesList;

	}

	@Override
	@SuppressWarnings({"unchecked"})
	public int resetReminder(Long userId, Schedule schedule) {
		
		int noOfRowsUpdated = 0;
		Query query = null;
	    String sql=null;
		Session session=null;
		List<Schedule> scheduleList=null;
		Transaction transaction=null;
		try {
			session=sessionFactory.openSession();
			transaction=session.beginTransaction();
		
				sql = "FROM Schedule WHERE medications.user.userId=? and TIME(scheduled_date)=? and DATE(scheduled_date)>?";
		
			query = session.createQuery(sql);
			query.setLong(0, userId);
			query.setString(1, schedule.getAlarmTime());
		
				query.setString(2, schedule.getDateAlarm());
			LOGGER.info("Executing: "+sql);
			scheduleList=(List<Schedule>)query.list();
			Iterator<Schedule> itrSchedule=scheduleList.iterator();
			
			while(itrSchedule.hasNext()){
				Schedule sch=itrSchedule.next();
				    sch.setReminderTime((long)-1);
					sch.setFlag((short)0);
					sch.setDays((short)0);
		
			}
			noOfRowsUpdated=scheduleList.size();
			transaction.commit();
		}catch(Exception e) {
			LOGGER.error(e);
		}finally {
			if(session != null){
			session.close();	
			}
		}
		return noOfRowsUpdated;
	}
	
	
	
}
