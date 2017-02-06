package com.qc.spring.controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qc.spring.base.BaseController;
import com.qc.spring.configs.Configuration;
import com.qc.spring.constants.RequestMappingURL;
import com.qc.spring.constants.RequestParameter;
import com.qc.spring.constants.ResponseMessages;
import com.qc.spring.constants.StatusCode;
import com.qc.spring.entities.AccessTokens;
import com.qc.spring.entities.CarePlans;
import com.qc.spring.entities.PinAuthentication;
import com.qc.spring.entities.States;
import com.qc.spring.entities.Users;
import com.qc.spring.json.PatientJSON;
import com.qc.spring.json.UserJSON;
import com.qc.spring.service.AccessTokenService;
import com.qc.spring.service.CarePlanService;
import com.qc.spring.service.CareTeamService;
import com.qc.spring.service.PinAuthnService;
import com.qc.spring.service.UserService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;
import com.qc.spring.utils.S3Save;
import com.qc.spring.validator.UserValidator;

/**
 * The Class UserController.
 */
@Controller
@RequestMapping(value = RequestMappingURL.BASE_USERS)
public class UserController extends BaseController {

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(UserController.class);

	/** The env. */
	@Autowired
	private Configuration env;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccessTokenService accessTokenService;
	
	@Autowired
	private CarePlanService carePlanService;
	
	@Autowired
	private PinAuthnService pinAuthnService;
	
	@Autowired
	private CareTeamService careTeamService;
	

	@ResponseBody
	@RequestMapping(value = RequestMappingURL.AUTHN, method = RequestMethod.POST)
	public String authn(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

		
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		CustomException ce = CustomException.getInstance();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Credentials","true");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
		response.addHeader("Connection", "keep-alive, close");
		LOGGER.info("Entry in authn() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.info(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String email = (String) jsonObject.get(RequestParameter.EMAIL);
				String password = (String) jsonObject.get(RequestParameter.PASSWORD);
                String authnKey=(String)jsonObject.get(RequestParameter.KEY);
                Long pin=(Long)jsonObject.get(RequestParameter.PIN);
                Long userId=(Long)jsonObject.get(RequestParameter.USER_ID);
                
                if(authnKey != null && authnKey.length() != 0 && pin != null && userId != null){
                	PinAuthentication pinAuthn = pinAuthnService.findByKeyAndUser(authnKey, userId.longValue());
                	if(pinAuthn != null){
                	     if(pinAuthn.getPin() == null) {
		                		pinAuthn.setPin(String.valueOf(pin));
		                		pinAuthnService.update(pinAuthn);
		                		ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.PIN_REGISTER, ResponseMessages.SUCCESS);
								encryptedString = Encrypter.encrypt(ce.toString(), key);
								return encryptedString;
						}else {
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.ALREDAY_EXIST_PIN, ResponseMessages.ERROR);
						}
                	}else{
                		
                		ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_KEY, ResponseMessages.ERROR);
                	}
                	
                }else if ((email != null) && (password != null)) {
					if(email.length() != 0 && password.length() != 0) {
						Users usr = userService.findByEmailIdAndPassword(email, password);
						if(usr != null){
							if(usr.getStatus().getStatusId() == 1l){
								PinAuthentication pinAuthn=new PinAuthentication();
								createKey(usr,pinAuthn);
								pinAuthnService.save(pinAuthn);
								encryptedString = Encrypter.encrypt(UserJSON.getAuthenicateJSON(pinAuthn), key);
								return encryptedString;
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCOUNT_INACTIVE, ResponseMessages.ERROR);
							}
						} else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_USER, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.EMAIL_AND_PASSWORD_NOT_EMPTY, ResponseMessages.ERROR);
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

		encryptedString =Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}


	@ResponseBody
	@RequestMapping(value = RequestMappingURL.MEMBER, method = RequestMethod.POST)
	public String member(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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
		LOGGER.info("Entry in authn() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.info(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
			}

			try {
				String memberId = (String) jsonObject.get(RequestParameter.MEMBER_ID);
				String dateOfBirth = (String) jsonObject.get(RequestParameter.DATE_OF_BIRTH);
              
            if ((memberId != null) && (dateOfBirth != null)) {
					if(memberId.length() != 0 && dateOfBirth.length() != 0) {
						Users usr = userService.findByMemberIdAndDOB(memberId, dateOfBirth);
						if(usr != null){
							if(usr.getStatus().getStatusId() == 1l){
								
								if(usr.getEmailId() != null && usr.getPassword() != null) {
									encryptedString =Encrypter.encrypt(UserJSON.getJSON(usr, 1), key);
								}else {
									encryptedString =Encrypter.encrypt(UserJSON.getJSON(usr, 0), key);
								}
								return encryptedString;
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCOUNT_INACTIVE, ResponseMessages.ERROR);
								response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
							}
						} else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MEMBERID_AND_DOB, ResponseMessages.ERROR);
							response.sendError(HttpServletResponse.SC_NOT_FOUND, ce.getMessage());
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
					}
				} else{
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
				}
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
		}

		encryptedString =Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}

		
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.PINLOGIN, method = RequestMethod.POST)
	public String keyauthn(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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
		LOGGER.info("Entry in login() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.info(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String authnKey = (String) jsonObject.get(RequestParameter.KEY);
				Long pin = (Long) jsonObject.get(RequestParameter.PIN);
				 Long userId=(Long)jsonObject.get(RequestParameter.USER_ID);
				 
				if ((authnKey != null) && (pin != null)) {
					if(authnKey.length() != 0) {
						PinAuthentication pinAuthn = pinAuthnService.findByKeyAndUser(authnKey, userId.longValue());
						
						if(pinAuthn != null){
							if(pinAuthn.getUser().getStatus().getStatusId() == 1l){
								AccessTokens at = createAccsessToken(pinAuthn.getUser());
								accessTokenService.save(at);
								CarePlans cp=carePlanService.findByUserID(pinAuthn.getUser().getUserId());
								encryptedString = Encrypter.encrypt(pinAuthn.getUser().getLoginJson(at,null,cp), key);
								return encryptedString;
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCOUNT_INACTIVE, ResponseMessages.ERROR);
							}
						} else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_KEY_AND_PIN, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);
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

		encryptedString =Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}


	
	/**
	 * Login user.
	 *
	 * @param EncryptedValue the encrypted value
	 * @return the string
	 * @throws Exception the exception
	 */
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.LOGIN, method = RequestMethod.POST)
	public String login(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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
		LOGGER.info("Entry in login() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);
      
		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.info(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String email = (String) jsonObject.get(RequestParameter.EMAIL);
				String password = (String) jsonObject.get(RequestParameter.PASSWORD);

				if ((email != null) && (password != null)) {
					if(email.length() != 0 && password.length() != 0) {
						Users usr = userService.findByEmailIdAndPassword(email, password);
						if(usr != null){
							if(usr.getStatus().getStatusId() == 1l){
								AccessTokens at = createAccsessToken(usr);
								accessTokenService.save(at);
								CarePlans cp=carePlanService.findByUserID(usr.getUserId());
								encryptedString = Encrypter.encrypt(usr.getLoginJson(at,null,cp), key);
								return encryptedString;
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCOUNT_INACTIVE, ResponseMessages.ERROR);
							}
						} else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_USER, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.EMAIL_AND_PASSWORD_NOT_EMPTY, ResponseMessages.ERROR);
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

		encryptedString =Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}


	/**
	 * Logout user.
	 *
	 * @param EncryptedValue the encrypted value
	 * @return the string
	 * @throws Exception the exception
	 */
	@ResponseBody
	@RequestMapping(value =RequestMappingURL.LOGOUT, method = RequestMethod.POST)
	public String logout(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

	
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

		LOGGER.info("Entry in logout() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, new Date().toString(), ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);

				if(accessToken != null){
					if(validateAccessToken(accessToken)){

						accessTokenService.remove(getAccessTokenObjectById(accessToken));

						ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.LOGOUT_MESSAGE, ResponseMessages.SUCCESS);
						encryptedString = Encrypter.encrypt(ce.toString(), key);
						return encryptedString;
					} else{
						ce.setFields(StatusCode.UNAUTHORIZED, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
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


	/**
	 * Change password.
	 *
	 * @param EncryptedValue the encrypted value
	 * @return the string
	 * @throws Exception the exception
	 */
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.CHANGE_PASSWORD, method = RequestMethod.POST)
	public String changePassword(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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

		LOGGER.info("Entry in changePassword() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, new Date().toString(), ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);
				Long userId = (Long) jsonObject.get(RequestParameter.USER_ID);
				String oldPassword = (String) jsonObject.get(RequestParameter.OLD_PASSWORD);
				String newPassword = (String) jsonObject.get(RequestParameter.NEW_PASSWORD);
				String confirmPassword = (String) jsonObject.get(RequestParameter.CONFIRM_PASSWORD);

				if(accessToken != null){
					if(validateAccessToken(accessToken)){
						if(userId != null && oldPassword != null && newPassword != null && confirmPassword != null){
							if(oldPassword.length() != 0 && newPassword.length() !=0 && confirmPassword.length() !=0) {
								Users usr = getUserObjectByID(userId);
								if(usr != null){
									if(usr.getPassword().equals(oldPassword)){
										if(!oldPassword.equals(newPassword)){
											if(newPassword.equals(confirmPassword)){
												if(newPassword.length()>=6 && newPassword.length()<=15) {
													usr.setPassword(newPassword);
													boolean status=getUpdate(usr, 1);
													if(status) {
														ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.PWD_CHANGED, ResponseMessages.SUCCESS);
														encryptedString = Encrypter.encrypt(ce.toString(), key);
														return encryptedString;}
													else {
														ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);}
												}else {
													ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.PASSWORD_LIMIT, ResponseMessages.ERROR);
												}
											} else{
												ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.PWD_MISMATCH, ResponseMessages.ERROR);
											}
										} else{
											ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.OLD_AND_NEW_PASSWORD, ResponseMessages.ERROR);
										}
									} else{
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_OLD_PASSWORD, ResponseMessages.ERROR);
									}
								} else{
									ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.USER_NOT_FOUND, ResponseMessages.ERROR);
								}
							}else{
								ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);
							}
						} else{
							ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.UNAUTHORIZED, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
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

	/**
	 * Forgot pwd.
	 *
	 * @param EncryptedValue the encrypted value
	 * @return the string
	 * @throws Exception the exception
	 */
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.FORGET_PASSWORD, method = RequestMethod.GET)
	public String forgotPwd(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

		
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

		LOGGER.info("Entry in forgotPwd() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, new Date().toString(), ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
			}

			try {
				String email = (String) jsonObject.get(RequestParameter.EMAIL);

				if (email != null) {
					if(email.length() != 0) {
						Users usr = getUserObjectByEmail(email.trim());
						if(usr != null){
							if(usr.getStatus().getStatusId() == 1l){
								sendForgotPasswordEmail(usr, env);

								ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.EMAIL_SEND, ResponseMessages.SUCCESS);
								encryptedString = Encrypter.encrypt(ce.toString(), key);
								return encryptedString;
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCOUNT_INACTIVE, ResponseMessages.ERROR);
							}
						} else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_EMAIL, ResponseMessages.ERROR);
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.EMAIL_NOT_EMPTY, ResponseMessages.ERROR);
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

	/**
	 * Reset pwd.
	 *
	 * @param request the request
	 * @param accessToken the access token
	 * @param id the id
	 * @param model the model
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = RequestMappingURL.WEB_FORGET_TOKEN, method = RequestMethod.GET)
	public String validateForogotPasswordLink(HttpServletRequest request, @PathVariable(RequestParameter.TOKEN) String token, @PathVariable(RequestParameter.USER_ID) Long id, Model model,HttpServletResponse response) throws Exception {

	
		LOGGER.info("Entry in validateForogotPasswordLink() method of UserController class");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Credentials","true");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
		response.addHeader("Connection", "keep-alive, close");

		if(token != null && id != null ) {
			Users usr = getUserObjectByID(id);
			if(usr != null){
				if(usr.getForgetToken() != null) {	
					if(usr.getForgetToken().equals(token)){	
						model.addAttribute(RequestParameter.USER_ID, usr.getUserId());
						return RequestMappingURL.FORGET_PWD;
					}
					else {
						model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
					}
				} else{
					model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
				}
			} else{
				model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
			}
		} else {
			model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
		}
		return RequestMappingURL.ERROR;
	}

	/**
	 * Reset pwd.
	 *
	 * @param request the request
	 * @param newPassword the new password
	 * @param confirmPassword the confirm password
	 * @param token the token
	 * @param model the model
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = RequestMappingURL.WEB_RESET_PASSWORD, method = RequestMethod.POST)
	public String resetPwd(HttpServletRequest request,
			@RequestParam(value = RequestParameter.NEW_PASSWORD) String newPassword,@RequestParam(value = RequestParameter.CONFIRM_PASSWORD) String confirmPassword,
			@RequestParam(RequestParameter.USER_ID) Long userid, Model model,HttpServletResponse response) throws Exception {

	
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Credentials","true");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
		response.addHeader("Connection", "keep-alive, close");

		LOGGER.info("Entry in resetPwd() method of UserController class");

		if(newPassword != null && confirmPassword != null){
			if(newPassword.equals(confirmPassword)){
				Users usr = getUserObjectByID(userid);
				if(usr != null){
					if(!usr.getPassword().equals(newPassword)) {
						usr.setForgetToken(null);
						usr.setPassword(newPassword);
						boolean status=getUpdate(usr, 1);
						if(status) {model.addAttribute(RequestParameter.MSG, ResponseMessages.PWD_CHANGED);}
						else {model.addAttribute(RequestParameter.MSG, ResponseMessages.ERR_TRY_AGAIN);}

					}else {
						model.addAttribute(RequestParameter.MSG, ResponseMessages.OLD_AND_NEW_PASSWORD);
					}	
				} else{
					model.addAttribute(RequestParameter.MSG, ResponseMessages.USER_EXPIRE);
				}
			} else{
				model.addAttribute(RequestParameter.MSG, ResponseMessages.PWD_MISMATCH);
				return RequestMappingURL.FORGET_PWD;
			}
		} else{
			model.addAttribute(RequestParameter.MSG, ResponseMessages.REQUIRED_FIELDS_BLANK);
			return RequestMappingURL.FORGET_PWD;
		}
		return RequestMappingURL.ERROR; 	
	}	


	@ResponseBody
	@RequestMapping(value = RequestMappingURL.GET_PROFILE, method = RequestMethod.GET)
	public String getUserProfile(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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

		LOGGER.info("Entry in getUserProfile() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
								encryptedString = Encrypter.encrypt(UserJSON.getJson(at),key);
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


	@ResponseBody
	@RequestMapping(value = RequestMappingURL.EDIT_PROFILE, method = RequestMethod.POST)
	public String editUserProfile(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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

		LOGGER.info("Entry in editUserProfile() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				JSONObject userJsonObject = (JSONObject) jsonObject.get(RequestParameter.USER);
				String firstName = (String)userJsonObject.get(RequestParameter.FIRST_NAME);
				String lastName = (String)userJsonObject.get(RequestParameter.LAST_NAME);
				String gender = (String)userJsonObject.get(RequestParameter.GENDER);
				String email = ((String)userJsonObject.get(RequestParameter.EMAIL)).trim();
				String height = (String)userJsonObject.get(RequestParameter.HEIGHT);
				String weight = (String)jsonObject.get(RequestParameter.WEIGHT);
				String address = (String)userJsonObject.get(RequestParameter.ADDRESS);
				String mobileNumber = (String)userJsonObject.get(RequestParameter.MOBILE_NUMBER);
				String profileImage = (String)userJsonObject.get(RequestParameter.PROFILE_IMAGE);
				String dob = (String)userJsonObject.get(RequestParameter.DATE_OF_BIRTH);
				String bloodGroup = (String)jsonObject.get(RequestParameter.BLOOD_GROUP);
				String medicalCondition = (String)jsonObject.get(RequestParameter.MEDICAL_CONDITION);
				String compliance = (String)jsonObject.get(RequestParameter.COMPILANCE);
				String city=(String)jsonObject.get(RequestParameter.CITY);
				String state=(String)jsonObject.get(RequestParameter.STATE);
				String country=(String)jsonObject.get(RequestParameter.COUNTRY);
				String postal=(String)jsonObject.get(RequestParameter.POSTALCODE);
				
				UserValidator validator=new UserValidator();
				Users userBean=new Users();
				userBean.setFirstName(firstName);
				userBean.setLastName(lastName);
				userBean.setGender(gender);
				userBean.setEmailId(email);
				userBean.setHeight(height);
				userBean.setAddress(address);
				userBean.setMobileNumber(mobileNumber);
				userBean.setProfile_image(profileImage);
				
				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId ){
								if(firstName != null && lastName != null && email !=null && dob !=null) {
									if(firstName.length() !=0 && lastName.length() !=0 && email.length() !=0 && dob.length() !=0) {
										
										String result=validator.validator(userBean, dob,ce);
										
										
										if(result != null) {
											encryptedString = Encrypter.encrypt(result, key);
											return encryptedString;
										}
										
										
										if(postal != null) {
											userBean.setPostalCode(Integer.parseInt(postal));
											validator.validatePostalCode(userBean.getPostalCode(), ce);
										}else if(weight != null) {
											validator.validateWeight(weight, ce);
										}else if(city != null) {
											validator.validateCity(city, ce);
										}else if(country != null) {
											validator.validateCountry(country, ce);
										}
										
										
										if(ce.getStatusCode() !=null) {
											encryptedString = Encrypter.encrypt(ce.toString(), key);
											return encryptedString;
										}
										
										
										States states=null; 
										if(state != null) {
										 states=careTeamService.getByStateName(state);
										 }
										
										
										Date date=new SimpleDateFormat("yyyy-MM-dd").parse(dob);
										Users user=at.getUser();
										user.setFirstName(firstName);
										user.setLastName(lastName);
										user.setDateOfBirth(date);
										user.setAddress(((address == null)?user.getAddress():address));
										user.setGender(((gender == null)?user.getGender():gender));
										user.setHeight(((height == null)?user.getHeight():height));
										user.setMobileNumber(((mobileNumber == null)?user.getMobileNumber():mobileNumber));
										user.setProfile_image(((profileImage == null)?user.getProfile_image():profileImage));
										user.setBloodGroup(((bloodGroup == null)?user.getBloodGroup():bloodGroup));
										user.setCompilance((compliance == null)?user.getCompilance():compliance);
										user.setWeight(((weight == null)?user.getWeight():weight));
										user.setMedicalCondition((medicalCondition == null)?user.getMedicalCondition():medicalCondition);
										user.setCity(((city == null)?user.getCity():city));
										user.setCountry((country == null)?user.getCountry():country);
										user.setState((states == null)?user.getState():states);
										user.setPostalCode((postal==null)?user.getPostalCode():userBean.getPostalCode());
										
										if(at.getUser().getEmailId().equalsIgnoreCase(email)) {
											boolean status=getUpdate(user, 1);
											if(status) {
												ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.USER_PROFILE_UPDATED, ResponseMessages.SUCCESS);
												encryptedString = Encrypter.encrypt(ce.toString(), key);
												return encryptedString;	
											}else {
												ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);}

										}else {
											Users usr=getUserObjectByEmail(email);
											if(usr == null) {
												user.setNewEmail(email);
												String uid = UUID.randomUUID().toString();
												user.setEmailToken(uid);
												boolean status=getUpdate(user, 1);
												if(status) {
													sendEmailForChangeEmail(user, env);
													ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.VERIFICATION_EMAIL, ResponseMessages.SUCCESS);
													encryptedString = Encrypter.encrypt(ce.toString(), key);
													return encryptedString;

												}else {
													ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
												}
											}else {
												ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.EMAIL_ALREADY_EXIST, ResponseMessages.ERROR);
											}
										}}else {
											ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);
										}
								}else {
									ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);
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



	@RequestMapping(value = RequestMappingURL.USER_PROFILE_EMAIL_VERIFY, method = RequestMethod.GET)
	public String emailVerificationLink(HttpServletRequest request, @PathVariable(RequestParameter.TOKEN) String token, @PathVariable(RequestParameter.USER_ID) Long id, Model model,HttpServletResponse response) throws Exception {

		
		LOGGER.info("Entry in emailVerificationLink() method of UserController class");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Credentials","true");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
		response.addHeader("Connection", "keep-alive, close");

		if(token != null && id != null ) {
			Users usr = getUserObjectByID(id);
			if(usr != null){
				if(usr.getEmailToken()!= null) {	
					if(usr.getEmailToken().equals(token)){
						usr.setEmailId(usr.getNewEmail());
						usr.setNewEmail(null);
						usr.setEmailToken(null);
						boolean status=getUpdate(usr, 1);
						if(status) {
							model.addAttribute(RequestParameter.MSG, ResponseMessages.EMAIL_CHANGED);
							return RequestMappingURL.ERROR;}
						else {
							model.addAttribute(RequestParameter.MSG, ResponseMessages.ERR_TRY_AGAIN);
						}
					}else {
						model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
					}
				} else{
					model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
				}
			} else{
				model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
			}
		} else {
			model.addAttribute(RequestParameter.MSG, ResponseMessages.LINK_EXPIRE);
		}
		return RequestMappingURL.ERROR;
	}


	@ResponseBody
	@RequestMapping(value = RequestMappingURL.PATIENTS, method = RequestMethod.GET)
	public String getPatientList(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

		 CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		 response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Credentials","true");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
		

		LOGGER.info("Entry in getPatientList() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long userId = (Long) jsonObject.get(RequestParameter.CAREGIVER_ID);
				Long limit = (Long) jsonObject.get(RequestParameter.LIMIT);
				Long offset = (Long) jsonObject.get(RequestParameter.OFFSET);
				String label = ((String) jsonObject.get(RequestParameter.LABEL));
				String keyword = ((String) jsonObject.get(RequestParameter.KEYWORD));

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId ){
								limit=(limit==null)?1000:limit;
								offset=(offset == null)?0:offset;
								if(label != null && keyword !=null && label.length() !=0 && keyword.length() !=0) {
									List<CarePlans> patients=carePlanService.findByCareGiverIdUsingSearch(userId, limit, offset, label, keyword);			
									int size=carePlanService.byCountUserId(userId);
									encryptedString = Encrypter.encrypt(PatientJSON.getJson(patients,size),key);
									return encryptedString;
								}else {
									List<CarePlans> patients=carePlanService.findByCareGiverId(userId,limit,offset);
									int size=carePlanService.byCountUserId(userId);
									encryptedString = Encrypter.encrypt(PatientJSON.getJson(patients,size),key);
									return encryptedString;}
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
	@RequestMapping(value = RequestMappingURL.PATIENTS, method = RequestMethod.POST)
	public String addPatient(HttpServletRequest request,@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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

		LOGGER.info("Entry in editUserProfile() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
			}

			try {
				
				Long userId = (Long) jsonObject.get(RequestParameter.USER_ID);
				String accessToken = (String) jsonObject.get(RequestParameter.ACCESS_TOKEN);
				String firstName = (String)jsonObject.get(RequestParameter.FIRST_NAME);
				String lastName = (String)jsonObject.get(RequestParameter.LAST_NAME);
				String gender = (String)jsonObject.get(RequestParameter.GENDER);
				String height = (String)jsonObject.get(RequestParameter.HEIGHT);
				String weight = (String)jsonObject.get(RequestParameter.WEIGHT);
				String address = (String)jsonObject.get(RequestParameter.ADDRESS);
				String mobileNumber = (String)jsonObject.get(RequestParameter.MOBILE_NUMBER);
				String bloodGroup = (String)jsonObject.get(RequestParameter.BLOOD_GROUP);
				String medicalCondition = (String)jsonObject.get(RequestParameter.MEDICAL_CONDITION);
				String compliance = (String)jsonObject.get(RequestParameter.COMPILANCE);
				String profileImage = (String)jsonObject.get(RequestParameter.PROFILE_IMAGE);
				String dob = (String)jsonObject.get(RequestParameter.DATE_OF_BIRTH);
				String city=(String)jsonObject.get(RequestParameter.CITY);
				String state=(String)jsonObject.get(RequestParameter.STATE);
				String country=(String)jsonObject.get(RequestParameter.COUNTRY);
				String postal=(String)jsonObject.get(RequestParameter.POSTALCODE);
				
				
				UserValidator validator=new UserValidator();
				
				Users userBean=new Users();
				userBean.setFirstName(firstName);
				userBean.setLastName(lastName);
				userBean.setGender(gender);
				userBean.setHeight(height);
				userBean.setAddress(address);
				userBean.setMobileNumber(mobileNumber);
				userBean.setProfile_image(profileImage);
				userBean.setBloodGroup(bloodGroup);
				userBean.setCompilance(compliance);
				userBean.setMedicalCondition(medicalCondition);
				userBean.setWeight(weight);
				userBean.setCity(city);
				userBean.setCountry(country);
				
				
				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
					
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						if(at != null){
							
							Users user=at.getUser();
							
							if(at.getUser().getUserId() == userId ){				
												
										
										if(dob !=null) {
											   validator.validateDOB(dob, ce);
										}else if(height != null) {
											validator.validateHeight(height, ce);
										}else if(weight != null) {
											validator.validateWeight(weight, ce);
										}else if(postal != null) {
											userBean.setPostalCode(Integer.parseInt(postal));
											validator.validatePostalCode(userBean.getPostalCode(), ce);
										}else if(gender != null) {
											validator.validatorGender(gender, ce);
										}else if(mobileNumber != null) {
											validator.validateMobileNumber(mobileNumber, ce);
										}else if(city != null) {
											validator.validateCity(city, ce);
										}else if(country != null) {
											validator.validateCountry(country, ce);
										}
										
										if(ce.getStatusCode() !=null) {
											encryptedString = Encrypter.encrypt(ce.toString(), key);
											response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
											return encryptedString;
										}
										
										
										States states=null; 
										if(state != null) {
										 states=careTeamService.getByStateName(state);
										 }
										
										
										
										Date date=null;
										if(dob != null) {
											date=new SimpleDateFormat("yyyy-MM-dd").parse(dob);
										}
										
										
												user.setFirstName(((firstName== null)?user.getFirstName():firstName));
												user.setLastName(((lastName == null)?user.getLastName():lastName));
												user.setDateOfBirth((date==null)?user.getDateOfBirth():date);
												user.setAddress(((address == null)?user.getAddress():address));
												user.setGender(((gender == null)?user.getGender():gender));
												user.setHeight(((height == null)?user.getHeight():height));
												user.setMobileNumber(((mobileNumber == null)?user.getMobileNumber():mobileNumber));
												user.setProfile_image(((profileImage == null)?user.getProfile_image():profileImage));
												user.setWeight(((weight == null)?user.getWeight():weight));
												user.setBloodGroup(((bloodGroup == null)?user.getBloodGroup():bloodGroup));
												user.setCompilance((compliance == null)?user.getCompilance():compliance);
												user.setMedicalCondition((medicalCondition == null)?user.getMedicalCondition():medicalCondition);
												user.setCity(((city == null)?user.getCity():city));
												user.setCountry((country == null)?user.getCountry():country);
												user.setState((states == null)?user.getState():states);
												user.setPostalCode((postal==null)?user.getPostalCode():userBean.getPostalCode());
												
												boolean status=userService.update(user);
											
												if(status) {
													ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.USER_PROFILE_UPDATED, ResponseMessages.SUCCESS);
													encryptedString = Encrypter.encrypt(ce.toString(), key);
													return encryptedString;	
												 }else {
													ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
													response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
												 }
												
						
								} else{
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
									response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
								}
							} else{
								ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_ACCESS_TOKEN, ResponseMessages.ERROR);
								response.sendError(HttpServletResponse.SC_NOT_FOUND, ce.getMessage());
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
					ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
				}
			} else {
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
			}

			encryptedString = Encrypter.encrypt(ce.toString(), key);
			return encryptedString;
		
	}

	@ResponseBody
	@RequestMapping(value = RequestMappingURL.UPLOAD, method = RequestMethod.POST)
	public String getUserProfilePicture(HttpServletRequest request,@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response,@RequestPart(RequestParameter.PROFILE_IMAGE) MultipartFile file) throws Exception {

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

		LOGGER.info("Entry in getUserProfilePicture() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

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

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId ){
								
									String file_name = null;
									if (!file.isEmpty()) {
										file_name = file.getOriginalFilename();
										String ext = file_name.substring(file_name.lastIndexOf("."), file_name.length());
										file_name = at.getUser().getUserId() + "_" + String.valueOf(System.currentTimeMillis()) + ext;
	
										String filepath = request.getSession().getServletContext().getRealPath("resources")
												+ File.separator + "temp" + File.separator + file_name;
	
										File newFile = new File(filepath);
								
										if(file.getContentType().equals("image/gif") || file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/x-png") || file.getContentType().equals("image/pjpeg")) {
										
											newFile.setWritable(true, false);
											file.transferTo(newFile);
											newFile.createNewFile();
											S3Save s3 = new S3Save();
											s3.Save(env.get(RequestParameter.BUCKET), env.get(RequestParameter.PROFILE_PIC_URL) + file_name, newFile);
											newFile.deleteOnExit();
										    at.getUser().setProfile_image(env.get(RequestParameter.HOST_URL)+env.get(RequestParameter.PROFILE_PIC_FOLDER)+file_name);
									        userService.update(at.getUser());
									
									
									        
										} else{
											ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_IMAGE_FORMAT, ResponseMessages.ERROR);
											encryptedString = Encrypter.encrypt(ce.toString(), key);
											response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
											return encryptedString;
										}
									        
										
										} else {
										at.getUser().setProfile_image(null);
									}
								
										
									
								encryptedString = Encrypter.encrypt(UserJSON.getJsonProfileImage(at),key);
								return encryptedString;
								}else {
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCESSTOKEN_AND_USERID_MISMATCH, ResponseMessages.ERROR);
								response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
							}
						} else{
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
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.MEMBER_ACCOUNT, method = RequestMethod.POST)
	public String memberAuthentication(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue,HttpServletResponse response) throws Exception {

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
		LOGGER.info("Entry in authn() method of UserController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
		String key = env.get(RequestParameter.AES_KEY);		
		decryptedString = Encrypter.decrypt(encryptedValue, key);
		LOGGER.info(ResponseMessages.DECRYPTED_MESSAGE + decryptedString);

		if (decryptedString != null) {
			try {
				jsonObject = (JSONObject) parser.parse(decryptedString);
			} catch (Exception e) {
				LOGGER.info(e);
				ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_JSON_FORMAT, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
			}

			try {
				String email = (String) jsonObject.get(RequestParameter.EMAIL);
				String password = (String) jsonObject.get(RequestParameter.PASSWORD);
				String memberId = (String) jsonObject.get(RequestParameter.MEMBER_ID);
				String dob = (String) jsonObject.get(RequestParameter.DATE_OF_BIRTH);
             
                if ((email != null) && (password != null) && (memberId != null) && (dob != null)) {
					if(email.length() != 0 && password.length() != 0 && memberId.length() != 0 && dob.length() != 0) {
						Users usr = userService.findByMemberIdAndDOB(memberId, dob);
						if(usr != null){
							if(usr.getStatus().getStatusId() == 1l){
								if(usr.getMemberId().equals(memberId)) {
									
									
									if(usr.getEmailId() == null && usr.getPassword() == null) {
									
									
											UserValidator validator=new UserValidator();
											boolean result=validator.validatorEmail(email);
												if(!result) {
													ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_EMAIL_FORMAT, ResponseMessages.ERROR);
													encryptedString = Encrypter.encrypt(ce.toString(), key);
													response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
													return encryptedString;
												}
																	
												PinAuthentication pinAuthn=new PinAuthentication();
												createKey(usr,pinAuthn);
												usr.setEmailId(email);
												usr.setPassword(password);
												userService.update(usr);
												pinAuthnService.save(pinAuthn);
												encryptedString = Encrypter.encrypt(UserJSON.getAuthenicateJSON(pinAuthn), key);
												return encryptedString;
									}else {
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.EMAIL_ID_AND_PASSWORD_EXIST, ResponseMessages.ERROR);
										response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
									}
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MEMBER_ID, ResponseMessages.ERROR);
									response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
								}
							} else{
								ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.ACCOUNT_INACTIVE, ResponseMessages.ERROR);
								response.sendError(HttpServletResponse.SC_FORBIDDEN, ce.getMessage());
							}
						} else{
							ce.setFields(StatusCode.NOT_FOUND, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MEMBERID_AND_DOB, ResponseMessages.ERROR);
							response.sendError(HttpServletResponse.SC_FOUND, ce.getMessage());
						}
					} else{
						ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
					}
				} else{
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS, ResponseMessages.ERROR);
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, ce.getMessage());
				}
			} catch (Exception e) {
				LOGGER.error(e);
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ce.getMessage());
		}

		encryptedString =Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}
	
}


