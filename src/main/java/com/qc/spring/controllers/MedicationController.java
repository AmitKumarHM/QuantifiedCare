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
import com.qc.spring.entities.Slot;
import com.qc.spring.enums.Color;
import com.qc.spring.enums.Dosage;
import com.qc.spring.enums.Form;
import com.qc.spring.enums.MedicationStatus;
import com.qc.spring.enums.Quantity;
import com.qc.spring.enums.Slots;
import com.qc.spring.enums.Strength;
import com.qc.spring.json.MedicationsJSON;
import com.qc.spring.service.MedicationService;
import com.qc.spring.service.ScheduleService;
import com.qc.spring.service.SlotService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;
import com.qc.spring.validator.MedicationValidator;

@Controller
@RequestMapping(value = RequestMappingURL.MEDICATIONS)
public class MedicationController extends BaseController {
	
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(MedicationController.class);

	/** The env. */
	@Autowired
	private Configuration env;

	@Autowired
	private MedicationService medicationService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	private MedicationValidator medicationValidator=new MedicationValidator();
	
	@Autowired
	private SlotService slotService;

	private static final String TIME="09:15:00 am";
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.ADD, method = RequestMethod.POST)
	public String addMedication(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance(); 
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in addMedication() method of MedicationController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
                String color=(String)jsonObject.get(RequestParameter.COLOR);
                String form=(String)jsonObject.get(RequestParameter.FORM);
                String shape=(String)jsonObject.get(RequestParameter.SHAPE);
                Long strength=(Long)jsonObject.get(RequestParameter.STRENGTH);
                String strengthUnit=(String)jsonObject.get(RequestParameter.STRENGTH_UNIT);
                String frequency=((String)jsonObject.get(RequestParameter.FREQUENCY));
                Long quantity=(Long)jsonObject.get(RequestParameter.QUANTITY);
                String quantityUnit=(String)jsonObject.get(RequestParameter.QUANTITY_UNIT);
                String dosageUnit=(String)jsonObject.get(RequestParameter.DOSAGE_UNIT);
                String name=(String)jsonObject.get(RequestParameter.NAME);
                Long dosage=(Long)jsonObject.get(RequestParameter.DOSAGE);
                JSONArray jsonArray=(JSONArray)jsonObject.get(RequestParameter.TIME);
                JSONArray daysArray=(JSONArray)jsonObject.get(RequestParameter.DAYS);
                
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> weekdates=new ArrayList<Date>();
                Date today=new Date();
              
                List<Date> dates=new ArrayList<Date>();;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(jsonArray != null && frequency.equals("Daily")){
					for (int j = 0; j <7; j++) {
						for (int i = 0; i < jsonArray.size(); i++) {
							String todayDate=formatter.format(today);
							todayDate=todayDate+" "+(String)jsonArray.get(i);
							Date d=formatter2.parse(todayDate);
							cal.setTime(d);
							cal.add(Calendar.DATE, j);
							d=cal.getTime();
							dates.add(d);
						    }
					}
				}
				else if(daysArray != null && jsonArray != null && frequency.equals("Weekly")){
				
					for (int i = 0; i < daysArray.size(); i++) {
							for (int j = 0; j < jsonArray.size(); j++) {
								String weekDate=(String)daysArray.get(i);
								String week=weekDate+" "+(String)jsonArray.get(j);
								weekdates.add(formatter2.parse(week));
							}
					 }
				}
				else {
					frequency="AsNeeded";
				}
	
                Medications medications=new Medications();
                medications.setColor((color == null)?null:Color.valueOf(color));
                medications.setForm((form == null)?null:Form.valueOf(form));
                medications.setShape((shape == null)?null:getEnum(shape));
                medications.setStrengthUnit((strengthUnit == null)?null:Strength.valueOf(strengthUnit));
                medications.setStrength((strength == null)?null:strength.intValue());
                medications.setQuantityUnit((quantityUnit == null)?null:Quantity.valueOf(quantityUnit));
                medications.setTotalQuantity((quantity == null)?null:quantity.intValue());
                medications.setName((name == null)?null:name.trim());
                medications.setDosageUnit((dosageUnit == null)?null:Dosage.valueOf(dosageUnit));
                medications.setDosage((dosage == null)?null:dosage.intValue());
                if(frequency != null){
                	Slots.valueOf(frequency);}
                Date currentDate=new Date();
                medications.setStartDate(currentDate);
                
                cal.setTime(currentDate);
                cal.add(Calendar.MONTH, 1);
                medications.setEndDate(cal.getTime());
                
                if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						if(at != null){
							
							if(at.getUser().getUserId() == userId){
							      
								String statusMessage=medicationValidator.validateMedication(medications);
								
								if(statusMessage == null) {
									
								     if(frequency != null && frequency.length() !=0 && TIME !=null && TIME.length() !=0 && (frequency.equals("Daily") || frequency.equals("Weekly") || frequency.equals("AsNeeded"))) {
								    	
			                            String timeSlot=getTimeSlot(TIME);
			                            Slot slot=slotService.findBySlotAndFrequency(timeSlot, frequency);
			                            if(slot != null) {
			                            	medications.setSlot(slot);
			                            	medications.setUser(at.getUser());
			                            	Long id=medicationService.save(medications);
			                            	if(id != null) {
			                                    String messages =MedicationsJSON.getAddJson(id);
			                                    Medications med=medicationService.findByID(id);           
			                                    Schedule sch=null;
			                                    if(frequency.equals("Daily")) {
			                                    	for (int i = 0; i < dates.size(); i++) {
			                                    	sch=new Schedule();
			                                    	sch.setFlag((short)0);
			                            			sch.setScheduleDate(dates.get(i));
			                            			sch.setMedications(med);
			                            			scheduleService.save(sch);}
			                                    	
			                                    }else if(frequency.equals("Weekly")){
			                                    	for (int i = 0; i < weekdates.size(); i++) {
				                                    	sch=new Schedule();
				                                    	sch.setFlag((short)0);
				                            			sch.setScheduleDate(weekdates.get(i));
				                            			sch.setMedications(med);
				                            			scheduleService.save(sch);}
			                                    }
			                                            encryptedString = Encrypter.encrypt(messages, key);
			                            		        return encryptedString;
				                               } else {
				                        	   ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
				                           }
			                            }else {
			                            	 ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);	
			                            }
								     }else { 
								    	 ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
								     }
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, statusMessage, ResponseMessages.ERROR);
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
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_VALUE, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}	
	
	
	

	@ResponseBody
	@RequestMapping(value = RequestMappingURL.RETRIEVE_ALL, method = RequestMethod.GET)
	public String fetchAllMedications(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		LOGGER.info("Entry in medications() method of MedicationController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				String trackDate = (String) jsonObject.get(RequestParameter.DATE);
				Long limit = (Long) jsonObject.get(RequestParameter.LIMIT);
				Long offset = (Long) jsonObject.get(RequestParameter.OFFSET);
				
				
				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						if(at != null){
							
							if(at.getUser().getUserId() == userId){
								limit=(limit==null)?1000:limit;
								offset=(offset == null)?0:offset;
								List<Medications> medList=medicationService.findByUserId(userId, limit, offset);
								if(medList != null){								
									Iterator<Medications> itr=medList.iterator();
									while(itr.hasNext()) {
										Medications medications=itr.next();
										List<Schedule> listSchedule=scheduleService.byMedId(medications.getMedicationId());
										medications.setSchedules(listSchedule);
									}
									encryptedString = Encrypter.encrypt(MedicationsJSON.getJson(medList,trackDate),key);
								    return encryptedString;
								
								}else{
									ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
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
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_VALUE, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.EDIT, method = RequestMethod.POST)
	public String editMedication(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		
		LOGGER.info("Entry in editMedication() method of MedicationController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long medicationId = (Long) jsonObject.get(RequestParameter.MEDICATION_ID);
                String color=(String)jsonObject.get(RequestParameter.COLOR);
                String form=(String)jsonObject.get(RequestParameter.FORM);
                String shape=(String)jsonObject.get(RequestParameter.SHAPE);
                Long strength=(Long)jsonObject.get(RequestParameter.STRENGTH);
                String strengthUnit=(String)jsonObject.get(RequestParameter.STRENGTH_UNIT);
                String frequency=((String)jsonObject.get(RequestParameter.FREQUENCY));
                Long quantity=(Long)jsonObject.get(RequestParameter.QUANTITY);
                String quantityUnit=(String)jsonObject.get(RequestParameter.QUANTITY_UNIT);
                String dosageUnit=(String)jsonObject.get(RequestParameter.DOSAGE_UNIT);
                String name=(String)jsonObject.get(RequestParameter.NAME);
                Long dosage=(Long)jsonObject.get(RequestParameter.DOSAGE);
             
                JSONArray jsonArray=(JSONArray)jsonObject.get(RequestParameter.TIME);
                JSONArray daysArray=(JSONArray)jsonObject.get(RequestParameter.DAYS);
                
                
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> weekdates=new ArrayList<Date>();
                Date today=new Date();
              
                List<Date> dates=new ArrayList<Date>();;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(jsonArray != null && frequency.equals("Daily")){
					for (int j = 0; j <7; j++) {
						for (int i = 0; i < jsonArray.size(); i++) {
							String todayDate=formatter.format(today);
							todayDate=todayDate+" "+(String)jsonArray.get(i);
							Date d=formatter2.parse(todayDate);
							cal.setTime(d);
							cal.add(Calendar.DATE, j);
							d=cal.getTime();
							dates.add(d);
						    }
					}
				}
				else if(daysArray != null && jsonArray != null && frequency.equals("Weekly")){
				
					for (int i = 0; i < daysArray.size(); i++) {
							for (int j = 0; j < jsonArray.size(); j++) {
								String weekDate=(String)daysArray.get(i);
								String week=weekDate+" "+(String)jsonArray.get(j);
								weekdates.add(formatter2.parse(week));
							}
					 }
				}
				else {
					frequency="AsNeeded";
				}
				
				if(frequency != null){
                	Slots.valueOf(frequency);}
				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						if(at != null){
							
							if(at.getUser().getUserId() == userId){
									
								     if(medicationId!= null && frequency != null && frequency.length() !=0 && TIME !=null && TIME.length() !=0 && (frequency.equals("Daily") || frequency.equals("Weekly") || frequency.equals("AsNeeded"))) {
								    	
			                           Medications medication=medicationService.findByID(medicationId);
			                            
			                           if(medication!=null && medication.getName().equals(name)){
								    	
			                        	   medication.setColor((color == null)?medication.getColor():Color.valueOf(color));
				                           medication.setForm((form == null)?medication.getForm():Form.valueOf(form));
				                           medication.setShape((shape == null)?medication.getShape():getEnum(shape));
				                           medication.setStrengthUnit((strengthUnit == null)?null:Strength.valueOf(strengthUnit));
				                           medication.setStrength((strength == null)?null:strength.intValue());
				                           medication.setQuantityUnit((quantityUnit == null)?medication.getQuantityUnit():Quantity.valueOf(quantityUnit));
				                           medication.setTotalQuantity((quantity == null)?medication.getTotalQuantity():quantity.intValue());
				                           medication.setName((name == null)?medication.getName():name.trim());
				                           medication.setDosageUnit((dosageUnit == null)?medication.getDosageUnit():Dosage.valueOf(dosageUnit));
				                           medication.setDosage((dosage == null)?medication.getDosage():dosage.intValue());
			                        	   
				                           String statusMessage=medicationValidator.validateMedication(medication);
								             
				                           if(statusMessage == null) {
				                           
				                           String timeSlot=getTimeSlot(TIME);
					                            Slot slot=slotService.findBySlotAndFrequency(timeSlot, frequency);
					                            if(slot != null) {
					                            	medication.setSlot(slot);
					                            	 
					                            	
					                            	if(medicationService.merge(medication) != null){
					                            	
					                            		if((dates != null && dates.size()!=0) || (weekdates != null && weekdates.size()!=0)) {
					                            		       scheduleService.removeByMedId(medication.getMedicationId());
					                            		}
					                            		
					                            		 Schedule sch=null;
					                
						                                    if(frequency.equals("Daily")) {
						                                    	for (int i = 0; i < dates.size(); i++) {
						                                    	sch=new Schedule();
						                                    	sch.setFlag((short)0);
						                            			sch.setScheduleDate(dates.get(i));
						                            			sch.setMedications(medication);
						                            			scheduleService.save(sch);}
						                                    	
						                                    }else if(frequency.equals("Weekly")){
						                                    	for (int i = 0; i < weekdates.size(); i++) {
							                                    	sch=new Schedule();
							                                    	sch.setFlag((short)0);
							                            			sch.setScheduleDate(weekdates.get(i));
							                            			sch.setMedications(medication);
							                            			scheduleService.save(sch);}
						                                    }
				                                           
					                            		ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_EDIT, ResponseMessages.SUCCESS);
					                            	}else {

					                            		ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_EDIT, ResponseMessages.SUCCESS);	
					                            	}
					                            }else {
					                            	 ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);	
					                            }
			                                 }else{
			                            	  ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);  
				                           }   
			                           }else{  
			                        	   ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_NAME, ResponseMessages.ERROR);  
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
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_VALUE, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}	
	
	
	/**
	 * @param encryptedValue
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.REMOVE, method = RequestMethod.POST)
	public String removeMedication(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in removeMedication() method of MedicationController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long medicationId = (Long) jsonObject.get(RequestParameter.MEDICATION_ID);
				if ((accessToken != null) && (userId != null) && (medicationId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								Medications med = medicationService.findByIDAndUserId(medicationId, userId);
								if(med != null){
									
									int statusW=scheduleService.removeByMedId(med.getMedicationId());
									if(statusW>=0) {
										medicationService.removeByMed(med.getMedicationId());
										ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_REMOVE, ResponseMessages.SUCCESS);	
									}
								}else{
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
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
	@RequestMapping(value = RequestMappingURL.TRACK, method = RequestMethod.POST)
	public String medTracking(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		
		
		LOGGER.info("Entry in medTracking() method of MedicationController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				String trackDate = (String) jsonObject.get(RequestParameter.DATE);
				DateFormat formatter2= new SimpleDateFormat("yyyy-MM-dd");
				JSONArray jsonArray=(JSONArray)jsonObject.get(RequestParameter.MEDICATIONS);
				List<Schedule> scheduleList=null;
				if(jsonArray != null) {
					scheduleList=new ArrayList<Schedule>();
					Schedule schedule=null;
				for (int i = 0; i < jsonArray.size(); i++) {
					 JSONObject objJSON=(JSONObject)jsonArray.get(i);
					    String status = (String) objJSON.get(RequestParameter.STATUS);
						Long scheduleId = (Long) objJSON.get(RequestParameter.SCHEDULE_ID);
						Long medicationId = (Long) objJSON.get(RequestParameter.MEDICATION_ID);
						
						schedule=new Schedule();	
						schedule.setScheduleId(scheduleId);
						schedule.setMedicationId(medicationId);
						schedule.setMedicationStatus(MedicationStatus.valueOf(status));
						scheduleList.add(schedule);
				     }
				}
				
				    int totalSchedule=0;
				    int totalMissed=0;
				    int totalTaken=0;
				    int totalLoggedIn=0; 
				    
				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
				
								if(scheduleList != null && scheduleList.size()!=0){
									Iterator<Schedule> itrSchedule=scheduleList.iterator();
									while(itrSchedule.hasNext()) {
										Schedule schedule=itrSchedule.next();
											 Medications med = medicationService.findByIDAndUserId(schedule.getMedicationId(), userId);
											 Schedule sch=scheduleService.byID(schedule.getScheduleId());
											if(med != null && sch.getMedications().getMedicationId().equals(med.getMedicationId())) {
												sch.setMedicationStatus(schedule.getMedicationStatus());
												scheduleService.merge(sch);
											}else {
												ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_ID, ResponseMessages.ERROR);
												encryptedString = Encrypter.encrypt(ce.toString(), key);
												return encryptedString;
											}
									}	
									
									
									if(trackDate != null) {	 
									      
									List<Medications> medList=medicationService.findByUserId(userId, null, null);;
									formatter2.parse(trackDate);
									Iterator<Medications> itr=medList.iterator();
									while(itr.hasNext()) {
										Medications medicine=itr.next();
										List<Schedule> scheduleListTrack=scheduleService.getByMedicationsAndDate(medicine.getMedicationId(),trackDate);
		                                totalSchedule=totalSchedule+scheduleListTrack.size();
		                                Iterator<Schedule> itrScheduleTrack=scheduleListTrack.iterator();
										while(itrScheduleTrack.hasNext()) {
											Schedule schedule=itrScheduleTrack.next();
											if(schedule.getMedicationStatus() != null && schedule.getMedicationStatus().toString().equals("Taken")) {
												totalTaken=totalTaken+1;
											}else if(schedule.getMedicationStatus() != null && schedule.getMedicationStatus().toString().equals("Missed")) {
												totalMissed=totalMissed+1;
											}else {
												totalLoggedIn=totalLoggedIn+1;
											}
										}
									}
								}
									
									encryptedString = Encrypter.encrypt(MedicationsJSON.getMedTracking(totalSchedule, totalMissed, totalTaken, totalLoggedIn), key);
									return encryptedString;
								}  else{
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MEDICATION_EMPTY, ResponseMessages.ERROR);
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
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_VALUE, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}
	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.STATUS, method = RequestMethod.POST)
	public String getStatus(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		
		LOGGER.info("Entry in getStatus() method of MedicationController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				String trackDate = (String) jsonObject.get(RequestParameter.DATE);
				Long limit = (Long) jsonObject.get(RequestParameter.LIMIT);
				Long offset = (Long) jsonObject.get(RequestParameter.OFFSET);
				
				DateFormat formatter2= new SimpleDateFormat("yyyy-MM-dd");	
				
				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								    int totalSchedule=0;
								    int totalMissed=0;
								    int totalTaken=0;
								    int totalLoggedIn=0;    
								    List<Medications> medList=medicationService.findByUserId(userId, null, null);;
								         
								    if(trackDate != null) {	 
								            	   formatter2.parse(trackDate);
													Iterator<Medications> itr=medList.iterator();
													while(itr.hasNext()) {
														Medications medicine=itr.next();
														List<Schedule> scheduleList=scheduleService.getByMedicationsAndDate(medicine.getMedicationId(),trackDate);
						                                totalSchedule=totalSchedule+scheduleList.size();
						                                Iterator<Schedule> itrSchedule=scheduleList.iterator();
														while(itrSchedule.hasNext()) {
															Schedule schedule=(Schedule)itrSchedule.next();
															if(schedule.getMedicationStatus() != null && schedule.getMedicationStatus().toString().equals("Taken")) {
																totalTaken=totalTaken+1;
															}else if(schedule.getMedicationStatus() != null && schedule.getMedicationStatus().toString().equals("Missed")) {
																totalMissed=totalMissed+1;
															}else {
																totalLoggedIn=totalLoggedIn+1;
															}
														}
													}
												}
								            limit=(limit==null)?10:limit;
									        offset=(offset == null)?0:offset;
								            List<Schedule> scheduleList=scheduleService.getByUserAndDate(userId,limit,offset,trackDate);				
										encryptedString = Encrypter.encrypt(MedicationsJSON.getMedTracking(scheduleList,totalSchedule, totalMissed, totalTaken, totalLoggedIn), key);
										return encryptedString;	
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
