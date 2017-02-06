package com.qc.spring.constants;

public interface RequestMappingURL {

	public static final String LOGIN = "/login";
	
	public static final String BASE_USERS = "/users";
	
	public static final String USERS_WEB = "/users/web/";
	
	public static final String USER_PROFILE_WEB = "/users/web/profile/";
	public static final String USER_PROFILE_EMAIL_VERIFY = "/web/profile/{token}/{user_id}";
	
	public static final String LOGOUT = "/logout";
	
	public static final String CHANGE_PASSWORD = "/change_password";
	public static final String FORGET_PASSWORD = "/forgot_password";
	public static final String WEB_FORGET_PASSWORD = "/forgot_pwd";
	
	public static final String WEB_FORGET_TOKEN = "/web/{token}/{user_id}";
	public static final String WEB_RESET_PASSWORD = "/reset_pwd";
	public static final String RESET_PASSWORD = "/reset_password";
	
	public static final String FORGET_TOKEN = "/{token}/{id}";
	public static final String FORGET = "/forget/{encrypted}";
	public static final String WEB_CHANGE_PWD_INITIATE = "/changepwd";
	
	public static final String WEB_CHANGE_PASSWORD = "/changepassword";	
	
	public static final String ERROR = "error";
	
	public static final String FORGET_PWD = "forgetpassword";
	
	public static final String CHANGE_PWD = "changepassword";
	
	public static final String GET_PROFILE= "profile/get";
	
	public static final String EDIT_PROFILE= "profile/edit";
	
	public static final String HEALTH= "/health";
	
	public static final String HEALTH_SUMMARY= "/summary";
	public static final String PATIENT_SUMMARY= "/patient";
	
	public static final String PATIENTS= "/patients";
	
	public static final String MESSAGES= "/messages";
	
	public static final String GET= "/get";
	
	public static final String ARCHIVE= "/archive";
	
	public static final String SEND= "/send";
	
	public static final String SYMPTOMS= "/symptoms";
	
	public static final String REMOVE= "/remove";
	
	public static final String ALL= "/";
	
	public static final String READ= "/read";
	
	public static final String RESTORE= "/restore";
	
	public static final String MEDICATIONS= "/medications";
	
	public static final String ADD= "/add";
	
	public static final String EDIT= "/edit";
	
	public static final String TRACK= "/track";
	
	public static final String RETRIEVE_ALL= "/all";
	
	public static final String SCHEDULE= "/schedule";
	
	public static final String STATUS= "/status";
	
	public static final String REMINDER= "/reminder";

	public static final String AUTHN = "/authn";

	public static final String PINLOGIN ="/pinlogin";
	
	public static final String  MEMBER="/member";
	
	public static final String  UPLOAD="/upload";
	
	public static final String  CARETEAM="/careteam";
	
	public static final String  MEMBER_ACCOUNT="/member/account";
	
}
