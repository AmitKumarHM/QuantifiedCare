package com.qc.spring.utils;


/**
 * The Class CustomException.
 */
public class CustomException {

	/** The status code. */
	private String statusCode;
	
	/** The message. */
	private String message;
	
	/** The current date. */
	private String currentDate;
	
	/** The result. */
	private String result;
	
	/** The instance. */
	private static CustomException instance = null;
	
	/**
	 * Instantiates a new custom exception.
	 */
	private CustomException(){}

	/**
	 * Gets the single instance of CustomException.
	 *
	 * @return single instance of CustomException
	 */
	public static CustomException getInstance() {
	         instance = new CustomException();
	      return instance;
	}
 
	/**
	 * Gets the status code.
	 *
	 * @return the status code
	 */
	public String getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Sets the status code.
	 *
	 * @param statusCode the new status code
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the current date.
	 *
	 * @return the current date
	 */
	public String getCurrentDate() {
		return currentDate;
	}
	
	/**
	 * Sets the current date.
	 *
	 * @param currentDate the new current date
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	
	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	/**
	 * Sets the fields.
	 *
	 * @param sCode the s code
	 * @param cDate the c date
	 * @param msg the msg
	 * @param rslt the rslt
	 */
	public void setFields(String sCode, String cDate, String msg, String rslt){
		this.setStatusCode(sCode);
		this.setCurrentDate(cDate);
		this.setMessage(msg);
		this.setResult(rslt);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	   return "{\"statusCode\":\"" + statusCode +  "\",\"currentDate\":\"" + currentDate +  "\",\"result\":\"" + result +  "\",\"message\":\"" + message + "\"}";
	}
}
