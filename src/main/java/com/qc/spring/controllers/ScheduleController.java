package com.qc.spring.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.spring.base.BaseController;
import com.qc.spring.configs.Configuration;
import com.qc.spring.constants.RequestMappingURL;
import com.qc.spring.constants.RequestParameter;
import com.qc.spring.constants.ResponseMessages;
import com.qc.spring.constants.StatusCode;
import com.qc.spring.entities.AccessTokens;
import com.qc.spring.entities.Medications;
import com.qc.spring.entities.Schedule;
import com.qc.spring.json.ScheduleJSON;
import com.qc.spring.service.MedicationService;
import com.qc.spring.service.ScheduleService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;

@Controller
@RequestMapping(value = RequestMappingURL.SCHEDULE)
public class ScheduleController extends BaseController{

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(ScheduleController.class);

	/** The env. */
	@Autowired
	private Configuration env;
	
	@Autowired
	private MedicationService medicationService;
	
	@Autowired
	private ScheduleService scheduleService;

	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.EDIT, method = RequestMethod.POST)
	public String editSchedule(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in addSchedule() method of ScheduleController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);
				Long userId = (Long) jsonObject.get(RequestParameter.USER_ID);
                JSONArray jsonArray=(JSONArray)jsonObject.get(RequestParameter.SCHEDULES);
                
                
				List<Schedule> scheduleList=null;
				if(jsonArray != null) {
					scheduleList=new ArrayList<Schedule>();
					Schedule schedule=null;
				for (int i = 0; i < jsonArray.size(); i++) {
					 JSONObject objJSON=(JSONObject)jsonArray.get(i);
					    Long reminderType=(Long) objJSON.get(RequestParameter.REMINDER_TYPE);
		                Long scheduleId=(Long) objJSON.get(RequestParameter.SCHEDULE_ID);
                        String date = (String) objJSON.get(RequestParameter.DATE);
                        Date formatDate=null;
                        if(date != null) {
                        formatDate = formatter.parse(date);}
						schedule=new Schedule();
						schedule.setScheduleDate(formatDate);
						schedule.setFlag((reminderType != null)?reminderType.shortValue():null);
						schedule.setScheduleId(scheduleId);
						scheduleList.add(schedule);
				     }
				}
				
				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						         if(at != null){
							                 if(at.getUser().getUserId() == userId){
													
							                	 Iterator<Schedule> itrSchList=scheduleList.iterator();
													
							                	 while(itrSchList.hasNext()) {
														Schedule sch=itrSchList.next();
														Schedule schedule=scheduleService.byID(sch.getScheduleId());
														if(sch.getScheduleDate() != null) {
															schedule.setScheduleDate(sch.getScheduleDate());
															}
														schedule.setFlag(sch.getFlag());
														scheduleService.merge(schedule);
												}
							            ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.SCHEDULES_UPDATED, ResponseMessages.SUCCESS);								
							  }else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
							}
						  } else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESS_TOKEN_NOT_EMPTY, ResponseMessages.ERROR);
					}
				} else{
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
				}
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}	
	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.ADD, method = RequestMethod.POST)
	public String addSchedule(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {


		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		LOGGER.info("Entry in addSchedule() method of ScheduleController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				Calendar cal = Calendar.getInstance();
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);
				Long userId = (Long) jsonObject.get(RequestParameter.USER_ID);
                Long reminderType=(Long) jsonObject.get(RequestParameter.REMINDER_TYPE);
                Long medicationId=(Long) jsonObject.get(RequestParameter.MEDICATION_ID);
                String frequency=((String)jsonObject.get(RequestParameter.FREQUENCY));
              
               
                
                JSONArray timeArray=(JSONArray)jsonObject.get(RequestParameter.TIME);
                JSONArray daysArray=(JSONArray)jsonObject.get(RequestParameter.DAYS);
                
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> weekdates=new ArrayList<Date>();
                Date today=new Date();
              
                List<Date> dates=new ArrayList<Date>();;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(timeArray != null && frequency.equals("Daily")){
					for (int j = 0; j <7; j++) {
						for (int i = 0; i < timeArray.size(); i++) {
							String todayDate=formatter.format(today);
							todayDate=todayDate+" "+(String)timeArray.get(i);
							Date d=formatter2.parse(todayDate);
							cal.setTime(d);
							cal.add(Calendar.DATE, j);
							d=cal.getTime();
							dates.add(d);
						    }
					}
				}
				else if(daysArray != null && timeArray != null && frequency.equals("Weekly")){
				
					for (int i = 0; i < daysArray.size(); i++) {
							for (int j = 0; j < timeArray.size(); j++) {
								String weekDate=(String)daysArray.get(i);
								String week=weekDate+" "+(String)timeArray.get(j);
								weekdates.add(formatter2.parse(week));
							}
					 }
				}
				else {
					frequency="AsNeeded";
				}
            	
 				
				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						if(at != null){
							
							if(at.getUser().getUserId() == userId){
								  if(medicationId != null && reminderType != null) {
												Medications med=medicationService.findByID(medicationId);
												List<Schedule> scheduleList=new ArrayList<Schedule>();
												if(med != null) {
													 Schedule sch=null;
					                                    if(frequency.equals("Daily")) {
					                                    	for (int i = 0; i < dates.size(); i++) {
					                                    	sch=new Schedule();
					                                    	sch.setFlag(reminderType.shortValue());
					                                    	sch.setScheduleDate(dates.get(i));
					                                    	sch.setMedications(med);
					                                    	Schedule addSchedule=scheduleService.merge(sch);
					                            			scheduleList.add(addSchedule);}
					                                    	
					                                    }else if(frequency.equals("Weekly")){
					                                    	for (int i = 0; i < weekdates.size(); i++) {
						                                    	sch=new Schedule();
						                                    	sch.setFlag(reminderType.shortValue());
						                                    	sch.setScheduleDate(weekdates.get(i));
						                            			sch.setMedications(med);
						                            			Schedule addSchedule=scheduleService.merge(sch);
						                            			scheduleList.add(addSchedule);	
					                                    	}
					                                    }
												
													encryptedString = Encrypter.encrypt(ScheduleJSON.getJson(scheduleList), key);
													return encryptedString;
									    }else {
											ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_ID, ResponseMessages.ERROR);
										}								
								   }else {
									ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
								}
							  }else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
							}
						  } else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESS_TOKEN_NOT_EMPTY, ResponseMessages.ERROR);
					}
				} else{
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
				}
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}		
	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.REMINDER, method = RequestMethod.POST)
	public String addRemainder(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in addRemainder() method of ScheduleController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);
		
		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);
				Long userId = (Long) jsonObject.get(RequestParameter.USER_ID);
                
                JSONArray jsonReminders=(JSONArray)jsonObject.get(RequestParameter.REMINDERS);
                List<Schedule> scheduleList=null;
				if(jsonReminders != null) {
					scheduleList=new ArrayList<Schedule>();
					Schedule schedule=null;
				for (int i = 0; i < jsonReminders.size(); i++) {
					 JSONObject objJSON=(JSONObject)jsonReminders.get(i);
					    
					    Long enable = (Long) objJSON.get(RequestParameter.ENABLE);
						Long reminderTime = (Long) objJSON.get(RequestParameter.REMINDER_TIME);
						String scheduleTime = (String) objJSON.get(RequestParameter.TIME);
						String date = (String) objJSON.get(RequestParameter.DATE);
				        
						schedule=new Schedule();	
						schedule.setReminderTime(reminderTime);
						schedule.setAlarmTime(scheduleTime);
						schedule.setDateAlarm(date);
						schedule.setFlag(enable.shortValue());
						scheduleList.add(schedule);
				     }
				}
           	
				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						         if(at != null){
							                 if(at.getUser().getUserId() == userId){
													
							                	if(jsonReminders != null && jsonReminders.size() !=0 && scheduleList != null && scheduleList.size() != 0) {
							                		Iterator<Schedule> itrSchedule=scheduleList.iterator();
							                		int status=0;
							                		while(itrSchedule.hasNext()) {
							                			Schedule schedule=itrSchedule.next();
							                		    status=scheduleService.addReminder(userId, schedule);
							                		    if(schedule.getDateAlarm() != null) {
							                		    scheduleService.resetReminder(userId, schedule);}
							                		}
							                	if(status !=0) {
							                		ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.REMINDER_ADD, ResponseMessages.SUCCESS);		
							                	}else {
							                ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.NO_REMINDER_ADD, ResponseMessages.SUCCESS);		
							            }
							     }else {
							          ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
							        }  								
							  }else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
							}
						  } else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESS_TOKEN_NOT_EMPTY, ResponseMessages.ERROR);
					}
				} else{
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
				}
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}	
}
