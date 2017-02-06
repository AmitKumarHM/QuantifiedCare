package com.qc.spring.service;

import java.util.List;

import com.qc.spring.entities.Schedule;

public interface ScheduleService {

	public boolean save(Schedule schedule);
	
	public Schedule merge(Schedule schedule);
	
	public boolean update(Schedule schedule);
	
	public int removeByMedId(Long medId) ;
	
	public Schedule byID(Long id);
	
	public int addReminder(Long userId,Schedule schedule);
	
	public int resetReminder(Long userId,Schedule schedule);
	
	public List<Schedule> byMedId(Long id);
	
	public List<Schedule> getByMedicationsAndDate(Long medId,String date);
	
	public List<Schedule> getByUser(Long userId, Long limit, Long offset);
	
	public List<Schedule> getByUserAndDate(Long userId, Long limit, Long offset,String tracDate);
}
