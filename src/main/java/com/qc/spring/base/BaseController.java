package com.qc.spring.base;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.qc.spring.configs.Configuration;
import com.qc.spring.constants.RequestMappingURL;
import com.qc.spring.constants.ResponseMessages;
import com.qc.spring.entities.AccessTokens;
import com.qc.spring.entities.PinAuthentication;
import com.qc.spring.entities.Users;
import com.qc.spring.enums.Shape;
import com.qc.spring.service.AccessTokenService;
import com.qc.spring.service.CarePlanService;
import com.qc.spring.service.UserService;
import com.qc.spring.utils.Mail;

/**
 * The Class BaseController.
 */
public abstract class BaseController {	
	
	
	@Autowired
	UserService userService;
	
	
	@Autowired
	AccessTokenService accessTokenService;
	
	@Autowired
	CarePlanService carePlanService;
	
	public AccessTokens getAccessTokenObjectById(String atId) {	

		AccessTokens at = null;
		try {
			at = accessTokenService.findByToken(atId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return at;
	}
	

	protected boolean validateAccessToken(String accToken) {

		boolean status = false;
		AccessTokens aToken = null;
		try {
			aToken = accessTokenService.findByToken(accToken);
			if(aToken != null)
				status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	protected Users getUserObjectByID(Long userId) {

		Users usr = null;
		try {
			usr = userService.findByID(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usr;
	}
	
	protected Users getUserObjectByEmail(String email) {

		Users usr = null;
		try {
			usr = userService.findByEmailId(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usr;
	}
	
	/**
	 * Gets the update.
	 *
	 * @param usr the usr
	 * @param flag the flag
	 * @return the update
	 */
	public boolean getUpdate(Users usr, int flag) {

		if(flag == 0)
			return userService.save(usr);
		else
			return userService.update(usr);
	}
	
	public boolean removeByUserId(Long userId) {
		return accessTokenService.removeByUserID(userId);
	}
	
	
	/**
	 * Creates the accsess token.
	 *
	 * @param user the user
	 * @return the access tokens
	 */
	public AccessTokens createAccsessToken(Users user) {	

		String random = UUID.randomUUID().toString();
		AccessTokens accessToken = new AccessTokens();
		accessToken.setCreatedDate(new Timestamp(new Date().getTime()));
		accessToken.setAccessToken(random);
		accessToken.setUser(user);
		
		return accessToken;
	}
	

	public void createKey(Users user, PinAuthentication pinAuthn) {	

		String random = UUID.randomUUID().toString();
		pinAuthn.setKey(random);
		pinAuthn.setUser(user);
	}

	
	
	
	/**
	 * Validate email web.
	 *
	 * @param email the email
	 * @param ce2 the ce2
	 * @param env the env
	 * @return the string
	 */
	public void sendForgotPasswordEmail(Users user, Configuration env) {	

		/*************************** amit kumar - (22052015) -  validate email of a user from DB and return user entity object  ********************/
		String uid = UUID.randomUUID().toString();
		user.setForgetToken(uid);
		userService.update(user);
		
		Mail.sendForgotPasswordMail(user, env.get("ACCESS_KEY"), env.get("SECRET_KEY"), env.get("AWS_SENDER"), env.get("email.forgot.subject"),env.get("email.forgot.message").split(";"), env.get("email.forgot.restmessage").split(";"), env.get("email.backend.url"), RequestMappingURL.USERS_WEB);
	}
	
	public void sendEmailForChangeEmail(Users user, Configuration env) {
		
		Mail.sendEmailForChangeEmail(user, env.get("ACCESS_KEY"), env.get("SECRET_KEY"), env.get("AWS_SENDER"), env.get("email.change.subject"),env.get("email.change.message"), env.get("email.change.restmessage"), env.get("email.backend.url"), RequestMappingURL.USER_PROFILE_WEB);
	}
	
	
	public String getTimeSlot(String time) throws ParseException {
	
		String morning = "12:00:00 am";
		String afternoon = "12:00:00 pm";
		String evening = "06:00:00 pm";
		String eveningEnd = "11:59:00 pm";
		
		DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
		Date date = formatter.parse(time);
		Date dmorning = formatter.parse(morning);
		Date dafternoon = formatter.parse(afternoon);
		Date devening = formatter.parse(evening);
		Date deveningEnd = formatter.parse(eveningEnd);
		if(date.equals(dmorning) || (date.after(dmorning) && date.before(dafternoon))) {
			 return "Morning";
		 }else if(date.equals(dafternoon) || (date.after(dafternoon) && date.before(devening))){
			 return "Afternoon";
		 }else if(date.equals(devening) || (date.after(devening) && date.before(deveningEnd))){
			 return "Evening";
		 }
		return null;
	}
	
	public Shape getEnum(String shape) {
		
		 for(Shape s: Shape.values()) {
			 if(s.getType().equals(shape)) {
				 return s;
			 }
		 }
		throw new RuntimeException(ResponseMessages.INVALID_VALUE);
	}
}
