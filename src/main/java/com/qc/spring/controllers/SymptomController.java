package com.qc.spring.controllers;

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
import com.qc.spring.service.SymptomService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;

@Controller
@RequestMapping(value = RequestMappingURL.SYMPTOMS)
public class SymptomController extends BaseController{

	@Autowired
	private SymptomService symptomsService;
	
	@Autowired
	private Configuration env;
		
	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(SymptomController.class);
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.REMOVE, method = RequestMethod.GET)
	public String removeSymptom(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {
		
		LOGGER.info("Entry in removeSymptom() method of SymptomController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);	
		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		
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
				Long pateintId = (Long) jsonObject.get(RequestParameter.USER_ID);
				
				
				if ((accessToken != null) && (pateintId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == pateintId ){					
								        int status=symptomsService.removeByPatientId(pateintId);
								        if(status>0) {
								        	ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.SYMPTOM_REMOVED, ResponseMessages.SUCCESS);
								        	encryptedString = Encrypter.encrypt(ce.toString(),key);
											return encryptedString;
								         }else{
								        	 ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
								    }
							  }else{
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
								}
						}else{
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
