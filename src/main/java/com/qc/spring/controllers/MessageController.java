package com.qc.spring.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.qc.spring.entities.Messages;
import com.qc.spring.entities.Users;
import com.qc.spring.json.MessagesJSON;
import com.qc.spring.service.MessageService;
import com.qc.spring.utils.CustomException;
import com.qc.spring.utils.Encrypter;
import com.qc.spring.validator.UserValidator;

@Controller
@RequestMapping(value = RequestMappingURL.MESSAGES)
public class MessageController extends BaseController{

	/** The LOGGER. */
	private static Log LOGGER = LogFactory.getLog(MessageController.class);

	/** The env. */
	@Autowired
	private Configuration env;

	@Autowired
	 private MessageService messageService;
	
	
	@ResponseBody
	@RequestMapping(value = RequestMappingURL.ALL, method = RequestMethod.GET)
	public String getMessageList(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in getMessageList() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long limit = (Long) jsonObject.get(RequestParameter.LIMIT);
				Long offset = (Long) jsonObject.get(RequestParameter.OFFSET);
				String label = ((String) jsonObject.get(RequestParameter.LABEL));
				String keyword = ((String) jsonObject.get(RequestParameter.KEYWORD));
				Long flag = ((Long) jsonObject.get(RequestParameter.FLAG));

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId ){
								limit=(limit==null)?10:limit;
								offset=(offset == null)?0:offset;
								if(label != null && label.length() !=0 && (label.equalsIgnoreCase("inbox") || label.equalsIgnoreCase("sent") || label.equalsIgnoreCase("archive"))) {
									List<Messages> messages=null;	
									if(keyword !=null && keyword.length() !=0) {
										messages=messageService.findByUserIdAndSearch(userId, limit, offset, label, keyword,flag);					
									}else {
										messages=messageService.findByUserId(userId,limit,offset,label,flag);			
									}
									encryptedString = Encrypter.encrypt(MessagesJSON.getJson(messages),key);
									return encryptedString;
								}else {
									ce.setFields(StatusCode.BAD_REQUEST, ResponseMessages.CURRENT_DATE, ResponseMessages.LABEL_MISSING, ResponseMessages.ERROR);
								}
							}else{
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
				LOGGER.info(e);
				ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
			}
		} else {
			ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.UNABLE_TO_DECRPT, ResponseMessages.ERROR);
		}

		encryptedString = Encrypter.encrypt(ce.toString(), key);
		return encryptedString;
	}

	@ResponseBody
	@RequestMapping(value = RequestMappingURL.GET, method = RequestMethod.GET)
	public String getMessage(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		LOGGER.info("Entry in getMessage() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long messageId = (Long) jsonObject.get(RequestParameter.MESSAGE_ID);

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								if(messageId != null) {
									Messages message=messageService.findByID(messageId);	
									if(message != null && (message.getToUser().getUserId().equals(userId) || message.getFromUser().getUserId().equals(userId))) {
											   message.setFlag((short)1);
											   messageService.update(message); 
										       encryptedString = Encrypter.encrypt(MessagesJSON.getJson(message),key);
										       return encryptedString;
										}else {
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ID_NOT, ResponseMessages.ERROR);
									}
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MESSAGE_ID, ResponseMessages.ERROR);
								}    									
							}else{
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
	@RequestMapping(value = RequestMappingURL.ARCHIVE, method = RequestMethod.GET)
	public String archiveMessage(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {


		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		LOGGER.info("Entry in archiveMessage() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				JSONArray jsonArray=(JSONArray)jsonObject.get(RequestParameter.MESSAGES);
				
				List<Long> messagesIds=null;
				if(jsonArray != null) {
				messagesIds=new ArrayList<Long>();
				for (int i = 0; i < jsonArray.size(); i++) {
					 messagesIds.add((Long)jsonArray.get(i));
					 }
				}
				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								for (int i = 0; i < messagesIds.size(); i++){
									
									   Long messageId=messagesIds.get(i);
											if(messageId != null) {
												Messages message=messageService.findByID(messageId);	
												if(message != null && (message.getToUser().getUserId().equals(userId) || message.getFromUser().getUserId().equals(userId))) {
													if(!(message.getLabel().equalsIgnoreCase("archive"))) {
													    String restoreStatus=message.getLabel();
													    message.setRestore(restoreStatus);
														message.setLabel("archive");
													   if(messageService.update(message)) {
														ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ARCHIVE, ResponseMessages.SUCCESS);
														  
													   }else{
														  ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);	
													      break;
													   }
												    }else {
													  ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ARCHIVED, ResponseMessages.ERROR);	
													  break;
												    }
												}else {
													ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ID_NOT, ResponseMessages.ERROR);
												    break;
												}
											}else {
												ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MESSAGE_ID, ResponseMessages.ERROR);
											    break;
											} 										
								}   									
							}else{
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
	@RequestMapping(value = RequestMappingURL.SEND, method = RequestMethod.POST)
	public String sendMessage(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {


		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in sendMessage() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				String body = (String) jsonObject.get(RequestParameter.BODY);
				String subject = (String) jsonObject.get(RequestParameter.SUBJECT);
				String toUser = (String) jsonObject.get(RequestParameter.TO_USER);

				if ((accessToken != null) && (userId != null)) {
					
					if(accessToken.length() != 0) {
						
						AccessTokens at = getAccessTokenObjectById(accessToken);
						
						if(at != null){
							
							if(at.getUser().getUserId() == userId){
								
								UserValidator validator=new UserValidator();
								
								if(toUser != null && toUser.trim().length() != 0 ) {
									body=(body == null)?"":body;
									subject=(subject == null)?"":subject;
									
									if(validator.validatorEmail(toUser)) {
										
										Users toUserRef=getUserObjectByEmail(toUser);
										Users fromUserRef=at.getUser();

										if(toUserRef != null && fromUserRef != null) {

											Messages inboxMessage=new Messages();
											Messages sendMessage=new Messages();

											inboxMessage.setBody(body);sendMessage.setBody(body);
											inboxMessage.setSubject(subject);sendMessage.setSubject(subject);
											inboxMessage.setCreatedDate(new Date());sendMessage.setCreatedDate(new Date());
											inboxMessage.setLabel("inbox");sendMessage.setLabel("sent");
											inboxMessage.setDeleted((short)1);sendMessage.setDeleted((short)1);
											inboxMessage.setToUser(toUserRef);sendMessage.setToUser(toUserRef);
											inboxMessage.setFromUser(fromUserRef);sendMessage.setFromUser(fromUserRef);
											inboxMessage.setFlag((short)0);sendMessage.setFlag((short)0);
											
											inboxMessage=messageService.merge(inboxMessage);
											sendMessage=messageService.merge(sendMessage);
											if(inboxMessage != null && sendMessage != null ) {
												encryptedString = Encrypter.encrypt(MessagesJSON.getSendJson(sendMessage),key);
												return encryptedString;

											}else {
												ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);
											}
										}else {
											ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_EMAILID, ResponseMessages.ERROR);
										}
									}else {
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_EMAILID, ResponseMessages.ERROR);
									}
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.EMPTY_OR_MISSING_PARAM, ResponseMessages.ERROR);
								}    									
							}else{
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
	@RequestMapping(value = RequestMappingURL.READ, method = RequestMethod.GET)
	public String readMessage(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {


		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in readMessage() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long messageId = (Long) jsonObject.get(RequestParameter.MESSAGE_ID);

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								if(messageId != null) {
									Messages message=messageService.findByID(messageId);	
									if(message != null && (message.getToUser().getUserId().equals(userId) || message.getFromUser().getUserId().equals(userId))) {
										if(message.getFlag().equals((short)0)) {
										   message.setFlag((short)1);
										   if(messageService.update(message)) {
											ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_READ, ResponseMessages.SUCCESS);
											encryptedString = Encrypter.encrypt(ce.toString(),key);
											return encryptedString;
										   }else{
											  ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);	
										   }
									    }else {
										  ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ARCHIVED, ResponseMessages.ERROR);	
										}
									}else {
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ID_NOT, ResponseMessages.ERROR);
									}
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MESSAGE_ID, ResponseMessages.ERROR);
								}    									
							}else{
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
	@RequestMapping(value = RequestMappingURL.RESTORE, method = RequestMethod.GET)
	public String restoreMessage(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {


		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in restoreMessage() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long messageId = (Long) jsonObject.get(RequestParameter.MESSAGE_ID);

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								if(messageId != null) {
									Messages message=messageService.findByID(messageId);	
									if(message != null && (message.getToUser().getUserId().equals(userId) || message.getFromUser().getUserId().equals(userId))) {
										if(message.getLabel().equalsIgnoreCase("archive") && message.getRestore() != null && message.getRestore().trim().length() != 0) {
										   message.setLabel(message.getRestore());
										   message.setRestore(null);
										   if(messageService.update(message)) {
											ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_READ, ResponseMessages.SUCCESS);
											encryptedString = Encrypter.encrypt(ce.toString(),key);
											return encryptedString;
										   }else{
											  ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);	
										   }
									    }else {
										  ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ALLREADY_RESTORE, ResponseMessages.ERROR);	
										}
									}else {
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ID_NOT, ResponseMessages.ERROR);
									}
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MESSAGE_ID, ResponseMessages.ERROR);
								}    									
							}else{
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
	@RequestMapping(value = RequestMappingURL.REMOVE, method = RequestMethod.GET)
	public String removeMessage(@RequestParam(value = RequestParameter.ENCRYPTEDVALUE) String encryptedValue) throws Exception {

		CustomException ce = CustomException.getInstance();
		 String decryptedString = null;
		 String encryptedString = null;
		 JSONObject jsonObject = null;
		 JSONParser parser = new JSONParser();
		
		
		LOGGER.info("Entry in restoreMessage() method of MessageController class :" + ResponseMessages.ENCRYPTED_MESSAGE + encryptedValue);
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
				Long messageId = (Long) jsonObject.get(RequestParameter.MESSAGE_ID);

				if ((accessToken != null) && (userId != null)) {
					if(accessToken.length() != 0) {
						AccessTokens at = getAccessTokenObjectById(accessToken);
						if(at != null){
							if(at.getUser().getUserId() == userId){
								if(messageId != null) {
									Messages message=messageService.findByID(messageId);	
									if(message != null && (message.getToUser().getUserId().equals(userId) || message.getFromUser().getUserId().equals(userId))) {
										if(message.getDeleted() == (short)1) {
										   message.setDeleted((short)0);
										   if(messageService.update(message)) {
											ce.setFields(StatusCode.SUCCESS, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_READ, ResponseMessages.SUCCESS);
											encryptedString = Encrypter.encrypt(ce.toString(),key);
											return encryptedString;
										   }else{
											  ce.setFields(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessages.CURRENT_DATE, ResponseMessages.ERR_TRY_AGAIN, ResponseMessages.ERROR);	
										   }
									    }else {
										  ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ALLREADY_RESTORE, ResponseMessages.ERROR);	
										}
									}else {
										ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.MESSAGE_ID_NOT, ResponseMessages.ERROR);
									}
								}else {
									ce.setFields(StatusCode.FORBIDDEN, ResponseMessages.CURRENT_DATE, ResponseMessages.INVALID_MESSAGE_ID, ResponseMessages.ERROR);
								}    									
							}else{
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
