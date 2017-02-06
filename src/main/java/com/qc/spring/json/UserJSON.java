package com.qc.spring.json;

import java.util.Date;

import org.json.simple.JSONObject;

import com.qc.spring.entities.AccessTokens;
import com.qc.spring.entities.PinAuthentication;
import com.qc.spring.entities.Users;

public class UserJSON {

	@SuppressWarnings("unchecked")
	public static String getJson(AccessTokens at) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "User profile retrieve successfully.");

		js.put("user", userJson(at));
	
		return js.toJSONString();
	}
	
	
	@SuppressWarnings("unchecked")
	public static String getJsonProfileImage(AccessTokens at) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "User profile image uploaded successfully.");
		js.put("user_id", at.getUser().getUserId());
		js.put("email", at.getUser().getEmailId());
	
		return js.toJSONString();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static String getJSON(Users user,int register) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("registered", register);
		if(register == 1) {
				js.put("message", "Patient Account is already created.");
				js.put("user", user);
				}
		else {
			js.put("message", "Patient Account is not created.");
		}
			
		return js.toJSONString();
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static String getAuthenicateJSON(PinAuthentication pinAuthn) {

		JSONObject js = new JSONObject();
		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Key generated successfully.");
		js.put("key", pinAuthn.getKey());
		js.put("user_id", pinAuthn.getUser().getUserId());
		return js.toJSONString();
	}
	
	
	
	@SuppressWarnings("unchecked")
	private static JSONObject userJson(AccessTokens at) {
		
		JSONObject js = new JSONObject();

		js.put("first_name", at.getUser().getFirstName());
		js.put("middle_name", at.getUser().getMiddleName());
		js.put("last_name", at.getUser().getLastName());
		js.put("dob", at.getUser().getDateOfBirth().toString());
		js.put("gender", at.getUser().getGender());
		js.put("email", at.getUser().getEmailId());
		js.put("mobile_number", at.getUser().getMobileNumber());
		js.put("profile_image", at.getUser().getProfile_image());
		js.put("address", at.getUser().getAddress());
		js.put("height", at.getUser().getHeight());
		
		return js;
	}
}
