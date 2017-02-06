package com.qc.spring.constants;

import java.util.Date;

/**
 * The Interface ResponseMessages.
 */
public interface ResponseMessages {

	/** The Constant DECRYPTED_MESSAGE. */
	public static final String CURRENT_DATE = new Date().toString();
	
	/** The Constant DECRYPTED_MESSAGE. */
	public static final String DECRYPTED_MESSAGE = "******DECRYPTED MESSAGE===>\n";
	
	/** The Constant DECRYPTED_MESSAGE. */
	public static final String ENCRYPTED_MESSAGE = "******ENCRYPTED MESSAGE===>\n";
	
	/** The Constant JSON_CONVERTED_MESSAGE. */
	public static final String JSON_CONVERTED_MESSAGE = "******JSON CONVERTED MESSAGE===>";
		
	/** The Constant ERROR. */
	public static final String ERROR = "error";
	
	/** The Constant ERROR. */
	public static final String SUCCESS = "Success";
	
	/** The Constant LOGOUT_MESSAGE. */
	public static final String LOGOUT_MESSAGE = "User logged out successfully.";
	
	/** The Constant UNABLE_LOGOUT. */
	public static final String UNABLE_LOGOUT = "Users unable to logout.";
	
	/** The Constant MISSING_PARAMETER. */
	public static final String MISSING_PARAMETER = "Missing Parameter.";
	
	/** The Constant INVALID_USER. */
	public static final String INVALID_USER = "There was an error with your email/password combination. Please try again.";
	
	public static final String INVALID_KEY_AND_PIN = "Invalid key or pin.";
	
	/** The Constant INVALID_USER. */
	public static final String INVALID_EMAIL = "Invalid email.";
	
	/** The Constant INVALID_USER. */
	public static final String REQUIRED_FIELDS_BLANK = "Required fields are blank.";
	
	/** The Constant INVALID_USER. */
	public static final String REQUIRED_FIELDS = "Required fields are missing.";
	
	/** The Constant INVALID_ACCESS_TOKEN. */
	public static final String USER_ALREADY_LOGOUT = "User is already logged out.";
	
	/** The Constant INVALID_JSON_FORMAT. */
	public static final String INVALID_JSON_FORMAT = "Invalid JSON format.";
	
	/** The Constant UNABLE_TO_DECRPT. */
	public static final String UNABLE_TO_DECRPT = "Unable to decrypt the encrypted value.";
			
	/** The Constant PWD_MISMATCH. */
	public static final String PWD_MISMATCH = "New and confirm password should be same.";
	
	/** The Constant PWD_CHANGED. */
	public static final String PWD_CHANGED = "Password has been changed successfully.";
	
	/** The Constant ERR_TRY_AGAIN. */
	public static final String ERR_TRY_AGAIN = "Error occured, Please try again.";
	
	/** The Constant EMAIL_SEND. */
	public static final String EMAIL_SEND = "An email has been send to your email id.";
	
	/** The Constant INVALID_PASSWORD. */
	public static final String INVALID_PASSWORD = "Invalid password.";
	
	/** The Constant OLD_PASSWORD. */
	public static final String OLD_PASSWORD_MESSAGE = "Old password is not correct.";
	
	/** The Constant FORGET_TOKEN_MESSAGE. */
	public static final String FORGET_TOKEN_MESSAGE = "Forget password is initiated.";
	
	public static final String OLD_AND_NEW_PASSWORD = "Old password and new password should not be same.";
	
	public static final String PASSWORD_LIMIT = "Password should be between 6 to 15 characters.";
	
	public static final String CHANGE_PWD_EXP = "Change password session has been expire.";
	
	public static final String PIN_REGISTER = "Pin register successfully.";
	
	public static final String INVALID_KEY = "Please enter the valid key.";
	
	public static final String INVALID_EMAILID = "Please enter the valid email address.";
	
	public static final String ACCOUNT_INACTIVE = "Your account has been disabled, Please contact to admin.";
	
	public static final String EMAIL_AND_PASSWORD_NOT_EMPTY = "Email and password should not be empty.";
	
	public static final String EMAIL_NOT_EMPTY = "Email should not be empty.";
	
	public static final String INVALID_ACCESS_TOKEN = "Invalid Access Token.";
	
	public static final String INVALID_FORGET_TOKEN = "Invalid Forget Token.";
	
	public static final String INVALID_OLD_PASSWORD = "Invalid Old Password.";
	
	public static final String USER_NOT_FOUND = "User not found.";
	
	public static final String LINK_EXPIRE = "Sorry, Link has been expired.";
	
	public static final String USER_EXPIRE = "Sorry, Password can not be changed, Please try later.";
	
	public static final String ACCESSTOKEN_AND_USERID_NOT_EMPTY = "Access Token and User Id should not be empty.";
	
	public static final String ACCESSTOKEN_AND_USERID_MISMATCH = "Access Token and User Id are not of user.";
	
	public static final String ACCESS_TOKEN_NOT_EMPTY = "Access Token should not be empty.";
	
	public static final String USER_PROFILE_UPDATED = "User profile updated successfully.";
	
	public static final String EMAIL_ALREADY_EXIST = "Entered email Id already exist.";
	
	public static final String VERIFICATION_EMAIL = "Verfication email has been sent at your new email Id.";
	
	public static final String EMAIL_CHANGED = "User email changed successfully.";
	
	public static final String LABEL_MISSING = "Label is missing as required parameter.";
	
	public static final String INVALID_MESSAGE_ID = "Invalid message id.";
	
	public static final String MESSAGE_ARCHIVE = "Message successfully archive.";
	
	public static final String UNAUTHORIZED_ACCESS = "Unauthorized access.";
	
	public static final String EMPTY_OR_MISSING_PARAM = "To user email is missing or empty.";
	
	public static final String MESSAGE_SENT = "Message sent successfully.";
	
	public static final String INVALID_EMAIL_FORMAT = "Invalid Email Id Format,Please try again.";
	
	public static final String INVALID_DATE_FORMAT = "Invalid Date,Please try again.";
	
	public static final String INVALID_GENDER = "Invalid Gender,Please try again.";
	
	public static final String INVALID_HEIGHT = "Invalid Height,Please try again.";
	
	public static final String INVALID_WEIGHT = "Invalid Weight,Please try again.";
	
	public static final String INVALID_MOBILE_NUMBER = "Invalid Mobile Number,Please try again.";
	
	public static final String INVALID_DATE_OF_BIRTH = "Date of birth exceeds the current date";
	
	public static final String MAXI_HEIGHT = "Height should be between 5 to 120 inches.";
	
	public static final String MAXI_WEIGHT = "Weight should be between 600 to 5 pounds.";
	
	public static final String MESSAGE_ID_NOT = "Message ID not present.";
	
	public static final String MESSAGE_ARCHIVED ="Message already archived.";
	
	public static final String MESSAGE_ALREADY_READ ="Message already read.";
	
	public static final String INVALID_DOB ="Invalid date of birth, Please try again.";
	
	public static final String MESSAGE_READ ="Message read successfully.";
	
	public static final String MESSAGE_RESTORE ="Message restore successfully.";
	
	public static final String MESSAGE_ALLREADY_RESTORE ="Message already restore.";
	
	public static final String MESSAGE_REMOVED ="Message remove successfully.";
	
	public static final String MESSAGE_ALLREADY_REMOVED ="Message already removed.";

    public static final String SYMPTOM_REMOVED ="Symptom remove successfully.";
	
	public static final String SYMPTOM_ALLREADY_REMOVED ="Symptom already removed.";
	
	public static final String MEDICATION_SAVED ="Medication Saved Successfully.";
	
	public static final String MEDICATION_EDIT ="Medication Updated Successfully.";
	
	public static final String MEDICATION_REMOVE ="Medication Removed Successfully.";
	
	public static final String MEDICATION_NAME ="Medication name must be same.";
	
	public static final String MEDICATION_EMPTY ="Medication list is empty.";
	
	public static final String MEDICATION_ID ="Invalid Medication Id.";
	
	public static final String INVALID_VALUE ="Invalid Value for Required Paramater.";
	
	public static final String SCHEDULES_ADDED ="Schedules are added successfully for medications.";
	
	public static final String SCHEDULES_UPDATED ="Schedules are updated successfully for medications.";
	
	public static final String REMINDER_ADD ="Reminder add successfully.";
	public static final String NO_REMINDER_ADD ="No medications is schedule for reminder.";
	
	public static final String USER_ID_AND_MISMATCH ="User Id and email Id are not of same user.";
	
	
	public static final String CARETEAM_EMPTY ="Care Member or Pharmacy or Doctors should not be empty.";
	public static final String CARETEAM_ADDED ="Care Member or Pharmacy or Doctor add successfully.";
	
	
	
	public static final String INVALID_MEMBER_ID ="Invalid Member Id.";
	
	public static final String ALREDAY_EXIST_PIN ="Your pin is already registered.";
	
	public static final String PATIENT_NOT_ASSIGNED ="You are authorize to access this patient details.";
	
	public static final String INVALID_MEMBERID_AND_DOB ="Invalid Member Id or Date of Birth or both.";
	
	public static final String INVALID_POSTALCODE ="Invalid postal code.";
	
	public static final String INVALID_CITY ="Invalid city.";
	
	public static final String INVALID_COUNTRY ="Invalid country.";
	
	public static final String EMAIL_ID_AND_PASSWORD_EXIST ="Email Id and Password already exist.";
	
	public static final String USER_NOT_AUTHORIZED ="User is not Authorized to add Care Team.";
	
	public static final String INVALID_IMAGE_FORMAT ="Invalid Profile Image format, Only jpeg, gif, png format supported.";
	
}
