package com.qc.spring.json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.qc.spring.entities.Medications;
import com.qc.spring.entities.Schedule;

public class MedicationsJSON {

	@SuppressWarnings("unchecked")
	public static String getJson(List<Medications> medications, String trackDate) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Medications list retrieve successfully.");
		js.put("medications", medicationsJson(medications,trackDate));
	
		return js.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray medicationsJson(List<Medications> medications, String trackDate) {
		
        JSONArray medicationsArray=new JSONArray();
        Iterator<Medications> medicationsItr=medications.iterator();
        JSONObject medicationsJson=null;
		while(medicationsItr.hasNext()) {
			Medications medication=medicationsItr.next();
			medicationsJson = new JSONObject();
			medicationsJson.put("color", medication.getColor().toString());
			medicationsJson.put("dosage", medication.getDosage());
			medicationsJson.put("dosage_unit", medication.getDosageUnit().getType());
			medicationsJson.put("form", medication.getForm().toString());
			medicationsJson.put("name",medication.getName());
			medicationsJson.put("quantity_unit", medication.getQuantityUnit().getType());
			medicationsJson.put("shape", medication.getShape().getType());
			medicationsJson.put("strength", medication.getStrength());
			medicationsJson.put("strength_unit",medication.getStrengthUnit().getType());
			medicationsJson.put("total_quantity", medication.getTotalQuantity());
			medicationsJson.put("slot_name",medication.getSlot().getSlot().getType());
			medicationsJson.put("frequency", medication.getSlot().getFrequency().getType());
			medicationsJson.put("medication_id", medication.getMedicationId());
			
			if(trackDate != null) {
			
				if(medication.getSlot().getFrequency().getType().equals("Daily")) {
					medicationsJson.put("schedules",shedulesJsonDaily(medication.getSchedules(),trackDate));
				
				}else if(medication.getSlot().getFrequency().getType().equals("Weekly")) {
					medicationsJson.put("schedules",shedulesJson(medication.getSchedules()));	
				
				}else{
					medicationsJson.put("schedules",shedulesJson(medication.getSchedules()));
				   }
				
			}else {
				  medicationsJson.put("schedules",shedulesJson(medication.getSchedules()));
			}
			medicationsJson.put("medication_id", medication.getMedicationId());
			medicationsArray.add(medicationsJson);
	
		}
		
		return medicationsArray;
	}
	
	@SuppressWarnings({"unchecked"})
	private static JSONArray shedulesJsonDaily(List<Schedule> schedules, String trackDate) {
		
		JSONArray schedulesArray=new JSONArray();
		Iterator<Schedule> itrSchedules=schedules.iterator();
		JSONObject js;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		while(itrSchedules.hasNext()) {
			
			js = new JSONObject();
			Schedule schedule=itrSchedules.next();
			String dbDate=formatter.format(schedule.getScheduleDate());
			if(dbDate.equals(trackDate)) {
			js.put("schedule_id",schedule.getScheduleId());
			js.put("schedule_date",schedule.getScheduleDate().getTime());
			js.put("enable",schedule.getFlag());
			js.put("daily",schedule.getDays());
			js.put("reminder_time",schedule.getReminderTime());
			js.put("status",(schedule.getMedicationStatus() != null?schedule.getMedicationStatus().toString():null));
			schedulesArray.add(js);
			}
		}
		return schedulesArray;
		}
	
	
	
	
	@SuppressWarnings({"unchecked"})
	private static JSONArray shedulesJson(List<Schedule> schedules) {
		
		JSONArray schedulesArray=new JSONArray();
		Iterator<Schedule> itrSchedules=schedules.iterator();
		JSONObject js;
		while(itrSchedules.hasNext()) {
			js = new JSONObject();
			Schedule schedule=itrSchedules.next();
			js.put("schedule_id",schedule.getScheduleId());
			js.put("schedule_date",schedule.getScheduleDate().getTime());
			js.put("enable",schedule.getFlag());
			js.put("reminder_time",schedule.getReminderTime());
			js.put("daily",schedule.getDays());
			js.put("status",(schedule.getMedicationStatus() != null?schedule.getMedicationStatus().toString():null));
			schedulesArray.add(js);
			}
		return schedulesArray;
		}

	@SuppressWarnings("unchecked")
	public static String getAddJson(Long med) {

		JSONObject js = new JSONObject();
		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Medications added successfully.");
		js.put("medication_id", med);
	
		return js.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String getMedTracking(List<Schedule> scheduleList, int totalSchedule,int totalMissed,int totalTaken,int totalLoggedIn) {

		JSONObject js = new JSONObject();
		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().getTime());
		js.put("total_schedule", totalSchedule);
		js.put("total_missed", totalMissed);
		js.put("total_taken", totalTaken);
		js.put("total_notloggedin", totalLoggedIn);
		js.put("medications", shedulesJSON(scheduleList));
		js.put("message","Medication Tracking Retrieve Successfully." );
		return js.toJSONString();
	}
	
	
	@SuppressWarnings({"unchecked"})
	private static JSONArray shedulesJSON(List<Schedule> schedules) {
		
		JSONArray schedulesArray=new JSONArray();
		Iterator<Schedule> itrSchedules=schedules.iterator();
		JSONObject js;
		while(itrSchedules.hasNext()) {
			js = new JSONObject();
			JSONObject schjs = new JSONObject();
			Schedule schedule=itrSchedules.next();
			Medications medicationsRef=schedule.getMedications();
			schjs.put("schedule_id",schedule.getScheduleId());
			schjs.put("schedule_date",schedule.getScheduleDate().getTime());
			schjs.put("reminder_time",schedule.getReminderTime());
			schjs.put("status",(schedule.getMedicationStatus() != null?schedule.getMedicationStatus().toString():null));
			schjs.put("enable",schedule.getFlag());
			schjs.put("daily",schedule.getDays());
			js.put("schedule",schjs);
			js.put("color", medicationsRef.getColor().toString());
			js.put("dosage", medicationsRef.getDosage());
			js.put("dosage_unit", medicationsRef.getDosageUnit().getType());
			js.put("form", medicationsRef.getForm().toString());
			js.put("name", medicationsRef.getName());
			js.put("quantity_unit", medicationsRef.getQuantityUnit().getType());
			js.put("shape", medicationsRef.getShape().getType());
			js.put("strength", medicationsRef.getStrength());
			js.put("strength_unit", medicationsRef.getStrengthUnit().getType());
			js.put("total_quantity", medicationsRef.getTotalQuantity());
			js.put("slot_name", medicationsRef.getSlot().getSlot().getType());
			js.put("frequency", medicationsRef.getSlot().getFrequency().getType());
			js.put("medication_id", medicationsRef.getMedicationId());
			schedulesArray.add(js);
			}
		return schedulesArray;
		}

	@SuppressWarnings("unchecked")
	public static String getMedTracking(int totalSchedule,int totalMissed,int totalTaken,int totalLoggedIn) {

		JSONObject js = new JSONObject();
		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().getTime());
		js.put("total_schedule", totalSchedule);
		js.put("total_missed", totalMissed);
		js.put("total_taken", totalTaken);
		js.put("total_notloggedin", totalLoggedIn);
		js.put("message","Med Tracking Status is added succesfully." );
		return js.toJSONString();
	}
	
	
}
