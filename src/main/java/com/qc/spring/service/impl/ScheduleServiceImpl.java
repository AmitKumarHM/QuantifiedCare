package com.qc.spring.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qc.spring.dao.ScheduleDAO;
import com.qc.spring.entities.Schedule;
import com.qc.spring.service.ScheduleService;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleDAO scheduleDAO;
	
	@Override
	public boolean save(Schedule schedule) {
		
		return scheduleDAO.save(schedule);
	}

	@Override
	public Schedule merge(Schedule schedule) {
		
		return scheduleDAO.merge(schedule);
	}

	@Override
	public int removeByMedId(Long medId) {

		return scheduleDAO.removeByMedId(medId);
	}

	@Override
	public Schedule byID(Long id) {
	
		return scheduleDAO.byID(id);
	}

	@Override
	public boolean update(Schedule schedule) {
		
		return scheduleDAO.update(schedule);
	}

	@Override
	public List<Schedule> getByMedicationsAndDate(Long medId, String date) {
		
		return scheduleDAO.byMedicationsAndDate(medId, date);
	}

	@Override
	public List<Schedule> getByUserAndDate(Long userId, Long limit, Long offset,String trackDate) {
	
		return scheduleDAO.getByUserAndDate(userId,limit,offset,trackDate);
	}

	@Override
	public List<Schedule> getByUser(Long userId, Long limit, Long offset) {
		
		return scheduleDAO.getByUser(userId, limit, offset);
	}

	@Override
	public List<Schedule> byMedId(Long id) {
		
		return scheduleDAO.byMedId(id);
	}

	@Override
	public int addReminder(Long userId,Schedule schedule) {
	
		return scheduleDAO.addReminder(userId, schedule);
	}

	@Override
	public int resetReminder(Long userId, Schedule schedule) {
		
		return scheduleDAO.resetReminder(userId, schedule);
	}

}
