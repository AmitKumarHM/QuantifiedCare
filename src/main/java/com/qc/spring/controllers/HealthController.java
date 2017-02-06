package com.qc.spring.controllers;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.qc.spring.entities.CarePlans;
import com.qc.spring.entities.Feeds;
import com.qc.spring.entities.Health;
import com.qc.spring.entities.Symptoms;
import com.qc.spring.json.HealthJSON;
import com.qc.spring.service.CarePlanService;
import com.qc.spring.service.FeedService;
import com.qc.spring.service.HealthService;
import com.qc.spring.service.SymptomService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;


@Controller
@RequestMapping(value = RequestMappingURL.HEALTH)
public class HealthController extends BaseController {

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(HealthController.class);
	
	/** The env. */
	@Autowired
	private Configuration env;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private HealthService healthService;
	
	@Autowired
	private CarePlanService carePlanService;
	
	@Autowired
	private SymptomService symptomsService;
	
	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.PATIENT_SUMMARY, method = RequestMethod.POST)
	public String patientHealthSummary(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		    response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Credentials","true");
			response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
			response.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
			response.addHeader("Connection", "keep-alive, close");

		 
		
		LOGGER.info("Entry in healthSummary() method of HealthController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long caregiverId = (Long) jsonObject.get(RequestParameter.CAREGIVER_ID);
				Long patientId = (Long) jsonObject.get(RequestParameter.PATIENT_ID);
				
				if ((accessToken != null) && (caregiverId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
		
							if(at.getUser().getUserId() == caregiverId ){
								
								if(patientId !=null) {
								 
									CarePlans cp=carePlanService.findByUserIdAndPatientId(caregiverId, patientId);
									       if(cp!= null) {
										
												List<Feeds> feeds=feedService.findByUserId(patientId);
												Symptoms symptoms=symptomsService.findByUserId(patientId);
												Health health=healthService.findByUserId(patientId);
												encryptedString = Encrypter.encrypt(HealthJSON.getJson(feeds, health, symptoms),key);
												return encryptedString;
								     }else {
								    	 ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.PATIENT_NOT_ASSIGNED, ResponseMessages.ERROR);
								   }
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);	
								}
							} else{
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
	@RequestMapping(value = RequestMappingURL.HEALTH_SUMMARY, method = RequestMethod.GET)
	public String healthSummary(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in healthSummary() method of HealthController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				
				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
		
							if(at.getUser().getUserId() == userId ){
								List<Feeds> feeds=feedService.findByUserId(userId);
								Symptoms symptoms=symptomsService.findByUserId(userId);
								Health health=healthService.findByUserId(userId);
								encryptedString = Encrypter.encrypt(HealthJSON.getJson(feeds, health, symptoms),key);
								return encryptedString;
							} else{
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
