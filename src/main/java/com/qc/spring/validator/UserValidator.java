package com.qc.spring.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qc.spring.constants.ResponseMessages;
import com.qc.spring.constants.StatusCode;
import com.qc.spring.entities.CareTeam;
import com.qc.spring.entities.Users;
import com.qc.spring.enums.City;
import com.qc.spring.enums.Country;
import com.qc.spring.utils.CustomException;

public class UserValidator {

	/** The _logger. */
	private static Log _logger = LogFactory.getLog(UserValidator.class);

	public String validator(Users user,String dob, CustomException ce) {

		if(user.getEmailId() != null && user.getEmailId().length() != 0) {

			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
			Boolean b = user.getEmailId().matches(EMAIL_REGEX);

			if(b== false){
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_EMAIL_FORMAT, ResponseMessages.ERROR);	
				return ce.toString();}

		}

		if(dob != null && dob.length() != 0){

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date =null;

			try {
				date = formatter.parse(dob);
				int flag=date.compareTo(new Date());
				if(flag>0){
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_DATE_OF_BIRTH, ResponseMessages.ERROR);	
					return ce.toString();					
				}

			} catch (ParseException e) {
				_logger.error(e);
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_DOB, ResponseMessages.ERROR);	
				return ce.toString();
			}

		}

		if(user.getGender() != null && user.getGender().length() != 0){
			if(!(user.getGender().equals("FEMALE")) && !(user.getGender().equals("MALE"))) {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_GENDER, ResponseMessages.ERROR);	
				return ce.toString();

			}		
		}

		if(user.getHeight() != null && user.getHeight().length() != 0) {
			try{
				Integer height=Integer.parseInt(user.getHeight());
				if(height.intValue()>120 || height.intValue()<5) {
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.MAXI_HEIGHT, ResponseMessages.ERROR);	
					return ce.toString();					
				}
			}
			catch(Exception e) {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_HEIGHT, ResponseMessages.ERROR);	
				return ce.toString();
			}

		}

		if(user.getMobileNumber() != null && user.getMobileNumber().length() != 0) {
			
			if(user.getMobileNumber().matches("\\d{3}[-\\s]\\d{3}[-\\s]\\d{4}")== false){
				 ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MOBILE_NUMBER, ResponseMessages.ERROR);	
					return ce.toString();				 
			 }
		}else {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MOBILE_NUMBER, ResponseMessages.ERROR);	
				    return ce.toString();
			}

		return null;
	}

	public boolean validatorEmail(String email) {
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(EMAIL_REGEX);
	}

	public String validator(CareTeam careTeam,CustomException ce) {


		if(careTeam.getMobileNumber() != null && careTeam.getMobileNumber().length() != 0) {

			if(careTeam.getMobileNumber().matches("\\d{3}[-\\s]\\d{3}[-\\s]\\d{4}")== false) {

				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MOBILE_NUMBER, ResponseMessages.ERROR);	
				return ce.toString();				

			}
		}else {
			ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);	
			return ce.toString();
		}



		if(careTeam.getPostalCode() != null) {

			if (careTeam.getPostalCode().toString().matches("\\d{5}") == false){

				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_POSTALCODE, ResponseMessages.ERROR);	
				return ce.toString();
			}
		}else {
			ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);	
			return ce.toString();
		}

		return null;
	}


	
	public String validateMobileNumber(String mobileNumber,CustomException ce) {

		if(mobileNumber != null && mobileNumber.length() != 0) {
			
			 if(mobileNumber.matches("\\d{3}[-\\s]\\d{3}[-\\s]\\d{4}")== false){
				 ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MOBILE_NUMBER, ResponseMessages.ERROR);	
					return ce.toString();				 
			 }
			 
		}else {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MOBILE_NUMBER, ResponseMessages.ERROR);	
				    return ce.toString();
			}
		
		return null;
	}

	public String validateDOB(String dob, CustomException ce) {
		if(dob != null && dob.length() != 0){

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date =null;

			try {
				date = formatter.parse(dob);
				int flag=date.compareTo(new Date());
				if(flag>0){
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_DATE_OF_BIRTH, ResponseMessages.ERROR);	
					return ce.toString();					
				}

			} catch (ParseException e) {
				_logger.error(e);
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_DOB, ResponseMessages.ERROR);	
				return ce.toString();
			}

		}
		return null;
	}

	public String validatorGender(String gender, CustomException ce) {

		if(gender != null && gender.length() != 0){
			if(!(gender.equals("FEMALE")) && !(gender.equals("MALE"))) {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_GENDER, ResponseMessages.ERROR);	
				return ce.toString();

			}		
		}

		return null;
	}

	public String validateHeight(String height, CustomException ce) {

		if(height != null && height.length() != 0) {
			try{
				Integer heightInt=Integer.parseInt(height);
				if(heightInt.intValue()>120 || heightInt.intValue()<5) {
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.MAXI_HEIGHT, ResponseMessages.ERROR);	
					return ce.toString();					
				}
			}
			catch(Exception e) {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_HEIGHT, ResponseMessages.ERROR);	
				return ce.toString();
			}

		}

		return null;
	}

	public String validateWeight(String weight, CustomException ce) {

		if(weight != null && weight.length() != 0) {
			try{
				Integer weightInt=Integer.parseInt(weight);
				if(weightInt.intValue()>600 || weightInt.intValue()<5) {
					ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.MAXI_WEIGHT, ResponseMessages.ERROR);	
					return ce.toString();					
				}
			}
			catch(Exception e) {
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_WEIGHT, ResponseMessages.ERROR);	
				return ce.toString();
			}

		}

		return null;
	}

	public String validatePostalCode(Integer postalCode, CustomException ce) {

		if(postalCode != null) {

			if (postalCode.toString().matches("\\d{5}") == false){

				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_POSTALCODE, ResponseMessages.ERROR);	
				return ce.toString();
			}
		}else {
			ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);	
			return ce.toString();
		}

		return null;
	}


	public City validateCity(String city, CustomException ce) {

		if(city != null) {
			 for(City s: City.values()) {
				 if(s.getType().equals(city)) {
					 return s;
				 }
			 }
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_CITY, ResponseMessages.ERROR);	
			}
		else {
			ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);		
		}
		return null;
	}
	
	
	public Country validateCountry(String country, CustomException ce) {

		if(country != null) {
			 for(Country s: Country.values()) {
				 if(s.getType().equals(country)) {
					 return s;
				 }
			 }
				ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_COUNTRY, ResponseMessages.ERROR);	
			}
		else {
			ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.REQUIRED_FIELDS_BLANK, ResponseMessages.ERROR);		
		}
		return null;
	}
	
	
}
