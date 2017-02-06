package com.qc.spring.dao;

import java.util.List;

import com.qc.spring.entities.Schedule;

public interface ScheduleDAO {

	public boolean save(Schedule schedule);

	public Schedule merge(Schedule schedule);

	public int removeByMedId(Long medId) ;
	
	public Schedule byID(Long id);
	
	public List<Schedule> byMedId(Long id);
	
	public boolean update(Schedule schedule);
	
	public int addReminder(Long userId,Schedule schedule);
	
	public int resetReminder(Long userId,Schedule schedule);
	
	public List<Schedule> byMedicationsAndDate(Long medId, String date);
	
	public List<Schedule> getByUser(Long userId, Long limit, Long offset);
	
	public List<Schedule> getByUserAndDate(Long userId, Long limit, Long offset, String trackDate);
}
