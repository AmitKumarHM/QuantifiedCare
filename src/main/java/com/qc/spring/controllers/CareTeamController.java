package com.qc.spring.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.qc.spring.entities.CareTeam;
import com.qc.spring.entities.Course;
import com.qc.spring.entities.Roles;
import com.qc.spring.entities.States;
import com.qc.spring.entities.Topics;
import com.qc.spring.json.CareMemberJSON;
import com.qc.spring.service.CareTeamService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;
import com.qc.spring.validator.UserValidator;


@Controller
@RequestMapping(value = RequestMappingURL.CARETEAM)
public class CareTeamController extends BaseController{


	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(CareTeamController.class);
	
	/** The env. */
	@Autowired
	private Configuration env;
		
	@Autowired
	private CareTeamService careTeamService;
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public String addCareTeam(HttpServletRequest request,@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

		
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		LOGGER.info("Entry in addCareTeam() method of CareTeamController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);
		CustomException ce = CustomException.getInstance();
		
		
		
		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
			}

			try {
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);
				Long userId = (Long) jsonObject.get(RequestParameter.USER_ID);
				JSONArray careMemberJSONArray=(JSONArray)jsonObject.get(RequestParameter.CARE_MEMEBERS);
				JSONArray pharmacyJSONArray=(JSONArray)jsonObject.get(RequestParameter.PHARMACY);
				JSONArray doctorsJSONArray=(JSONArray)jsonObject.get(RequestParameter.DOCTORS);
				
				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
		
							if(at.getUser().getUserId() == userId ){
							   
								if(at.getUser().getRole().getRoleId() == 3L) {
								
								
								
								UserValidator userValidator=new UserValidator(); 
								List<Roles> roles=careTeamService.getAll();
								Iterator<Roles> itrRoles=roles.iterator();
								Roles doctorRole=null;
								Roles careMemberRole=null;
								Roles pharmacyRole=null;
								while(itrRoles.hasNext()) {
									Roles role=itrRoles.next();
									if((role.getRole().equalsIgnoreCase("Doctor"))) {
										doctorRole=role;	
									}
									if((role.getRole().equalsIgnoreCase("CareMember"))) {
										careMemberRole=role;
									}
									if((role.getRole().equalsIgnoreCase("Pharmacy"))) {
										pharmacyRole=role;
									}
									
								}
								
								List<CareTeam> careTeam=new ArrayList<CareTeam>();
								
								
								
								if(careMemberJSONArray != null) {
								for (int i = 0; i < careMemberJSONArray.size(); i++) {
									JSONObject careMemberJSON =(JSONObject)careMemberJSONArray.get(i);
									CareTeam careMember=new CareTeam();
									careMember.setFirstName((String)careMemberJSON.get(RequestParameter.FIRST_NAME));
									careMember.setLastName((String)careMemberJSON.get(RequestParameter.LAST_NAME));
									careMember.setEmailId(((String)careMemberJSON.get(RequestParameter.EMAIL)));
									careMember.setAddress((String)careMemberJSON.get(RequestParameter.ADDRESS));
									careMember.setMobileNumber((String)careMemberJSON.get(RequestParameter.MOBILE_NUMBER));
									careMember.setPostalCode(((Long)careMemberJSON.get(RequestParameter.POSTALCODE)).intValue());
									careMember.setCity((String)careMemberJSON.get(RequestParameter.CITY));
									careMember.setStateName((String)careMemberJSON.get(RequestParameter.STATE));
									careMember.setCountry((String)careMemberJSON.get(RequestParameter.COUNTRY));
									careMember.setRole(careMemberRole);
									
									if(careMember.getFirstName() == null || careMember.getLastName() == null || careMember.getAddress() == null || careMember.getMobileNumber() == null || careMember.getPostalCode() == null || careMember.getCity() == null || careMember.getStateName() == null || careMember.getCountry() == null || careMember.getEmailId() == null) {
										throw new Exception(ResponseMessages.REQUIRED_FIELDS);
									}
									else if(careMember.getFirstName().trim().length() == 0 || careMember.getLastName().trim().length() == 0 || careMember.getAddress().trim().length() == 0 || careMember.getMobileNumber().trim().length() == 0 ||  careMember.getCity().trim().length() == 0 || careMember.getStateName().trim().length() == 0 || careMember.getCountry().trim().length() == 0 || careMember.getEmailId().trim().length() == 0 ) {
										throw new Exception(ResponseMessages.REQUIRED_FIELDS_BLANK);
									}
									if(userValidator.validatorEmail(careMember.getEmailId().trim())==false) {
										throw new Exception(ResponseMessages.INVALID_EMAIL_FORMAT);
									}
									userValidator.validateCity(careMember.getCity(), ce);
									
									userValidator.validatePostalCode(careMember.getPostalCode(), ce);
									
									userValidator.validateMobileNumber(careMember.getMobileNumber(), ce);
									
									userValidator.validateCountry(careMember.getCountry(), ce);
									
									if(ce.getStatusCode() != null) {
										encryptedString = Encrypter.encrypt(ce.toString(), key);
										response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
										return encryptedString;
									}
									
									careTeam.add(careMember);
								  }
								}
								
								
								
								
								if(pharmacyJSONArray!= null) {
									for (int i = 0; i < pharmacyJSONArray.size(); i++) {
										JSONObject careMemberJSON =(JSONObject)pharmacyJSONArray.get(i);
										CareTeam careMember=new CareTeam();
										careMember.setPharmacyName((String)careMemberJSON.get(RequestParameter.NAME));
										careMember.setEmailId(((String)careMemberJSON.get(RequestParameter.EMAIL)));
										careMember.setAddress((String)careMemberJSON.get(RequestParameter.ADDRESS));
										careMember.setMobileNumber((String)careMemberJSON.get(RequestParameter.MOBILE_NUMBER));
										careMember.setPostalCode(((Long)careMemberJSON.get(RequestParameter.POSTALCODE)).intValue());
										careMember.setCity((String)careMemberJSON.get(RequestParameter.CITY));
										careMember.setStateName((String)careMemberJSON.get(RequestParameter.STATE));
										careMember.setCountry((String)careMemberJSON.get(RequestParameter.COUNTRY));
										careMember.setRole(pharmacyRole);
										
										if(careMember.getPharmacyName() == null || careMember.getAddress() == null || careMember.getMobileNumber() == null || careMember.getPostalCode() == null || careMember.getCity() == null || careMember.getStateName() == null || careMember.getCountry() == null ) {
											throw new Exception(ResponseMessages.REQUIRED_FIELDS);
										}
										else if(careMember.getPharmacyName().trim().length() == 0 || careMember.getAddress().trim().length() == 0 || careMember.getMobileNumber().trim().length() == 0 ||  careMember.getCity().trim().length() == 0 || careMember.getStateName().trim().length() == 0 || careMember.getCountry().trim().length() == 0 ) {
											throw new Exception(ResponseMessages.REQUIRED_FIELDS_BLANK);
										}
									 
										userValidator.validateCity(careMember.getCity(), ce);
										
										userValidator.validatePostalCode(careMember.getPostalCode(), ce);
										
										userValidator.validateMobileNumber(careMember.getMobileNumber(), ce);
										
										userValidator.validateCountry(careMember.getCountry(), ce);
										
										if(ce.getStatusCode() != null) {
											encryptedString = Encrypter.encrypt(ce.toString(), key);
											response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
											return encryptedString;
										}
										careTeam.add(careMember);
									  }
									}
								
								
								
								
								if(doctorsJSONArray != null) {
									for (int i = 0; i < doctorsJSONArray.size(); i++) {
										JSONObject careMemberJSON =(JSONObject)doctorsJSONArray.get(i);
										CareTeam careMember=new CareTeam();
										careMember.setFirstName((String)careMemberJSON.get(RequestParameter.FIRST_NAME));
										careMember.setLastName((String)careMemberJSON.get(RequestParameter.LAST_NAME));
										careMember.setEmailId(((String)careMemberJSON.get(RequestParameter.EMAIL)));
										careMember.setAddress((String)careMemberJSON.get(RequestParameter.ADDRESS));
										careMember.setMobileNumber((String)careMemberJSON.get(RequestParameter.MOBILE_NUMBER));
										careMember.setPostalCode(((Long)careMemberJSON.get(RequestParameter.POSTALCODE)).intValue());
										careMember.setCity((String)careMemberJSON.get(RequestParameter.CITY));
										careMember.setStateName((String)careMemberJSON.get(RequestParameter.STATE));
										careMember.setCountry((String)careMemberJSON.get(RequestParameter.COUNTRY));
										careMember.setRole(doctorRole);
										
										if(careMember.getFirstName() == null || careMember.getLastName() == null || careMember.getAddress() == null || careMember.getMobileNumber() == null || careMember.getPostalCode() == null || careMember.getCity() == null || careMember.getStateName() == null || careMember.getCountry() == null ) {
											throw new Exception(ResponseMessages.REQUIRED_FIELDS);
										}
										
										else if(careMember.getFirstName().trim().length() == 0 || careMember.getLastName().trim().length() == 0 || careMember.getAddress().trim().length() == 0 || careMember.getMobileNumber().trim().length() == 0 ||  careMember.getCity().trim().length() == 0 || careMember.getStateName().trim().length() == 0 || careMember.getCountry().trim().length() == 0 ) {
											throw new Exception(ResponseMessages.REQUIRED_FIELDS_BLANK);
										}
										
										userValidator.validateCity(careMember.getCity(), ce);
										
										userValidator.validatePostalCode(careMember.getPostalCode(), ce);
										
										userValidator.validateMobileNumber(careMember.getMobileNumber(), ce);
										
										userValidator.validateCountry(careMember.getCountry(), ce);
										
										if(ce.getStatusCode() != null) {
											encryptedString = Encrypter.encrypt(ce.toString(), key);
											response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
											return encryptedString;
										}
										
										careTeam.add(careMember);
									   }
								
									}
																
								
								if(careTeam.size()!=0) {
								
									List<Long> careMemberId=new ArrayList<Long>();
									Iterator<CareTeam> itrCareTeam=careTeam.iterator();
									while(itrCareTeam.hasNext()) {
										CareTeam careTeamRef=itrCareTeam.next();
		
									    States state=careTeamService.getByStateName(careTeamRef.getStateName());
									    
									    if(state != null) {
									    
												 careTeamRef.setState(state);
												 Long id=careTeamService.save(careTeamRef);
												 careMemberId.add(id);
									    
										    }else {
										    	
										    	ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_VALUE, ResponseMessages.ERROR);
										    	encryptedString = Encrypter.encrypt(ce.toString(), key);
										    	response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
										    	return encryptedString;
												
										    }
								       }
									     encryptedString = Encrypter.encrypt(CareMemberJSON.getJson(careMemberId), key);
									    return encryptedString;
								   }else {
								      ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.CARETEAM_EMPTY, ResponseMessages.ERROR);
								      response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
							    }
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
								response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
							}
						}else {
							 ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.USER_NOT_AUTHORIZED, ResponseMessages.ERROR);
							 response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());   
						}
						}else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
							response.sendError(HttpServletResponse.SC_FOUND, ce.getMessage());
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESS_TOKEN_NOT_EMPTY, ResponseMessages.ERROR);
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
					}
				} else{
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
				}
				
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, e.getMessage(), ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
		}
		
		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}
	
	
	@ResponseBody
	@RequestMapping(value ="techmentro",method = RequestMethod.GET)
	public String techmentro() throws Exception {

		
	Course course=new Course();
	course.setFee(200000);
	course.setName("Spring Extensions");
	
	List<Topics> topics=new ArrayList<Topics>();
	Topics topic=new Topics();
	topic.setName("Exceptions Handling");
	topic.setWeightage(85L);
	
	Topics topic2=new Topics();
	topic2.setName("Container");
	topic2.setWeightage(95L);
	
	topics.add(topic);
	topics.add(topic2);
	
	course.setTopicList(topics);
   
	careTeamService.save(course);
	
	System.out.println(careTeamService.getAllEntity(Course.class));
	
	return "Everything is ok";
	}
	
	
	
}
