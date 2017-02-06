package com.qc.spring.json;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.qc.spring.entities.Messages;

public class MessagesJSON {
	
	@SuppressWarnings("unchecked")
	public static String getJson(List<Messages> messages) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Messages retrieve successfully.");

		js.put("messages", messagesJson(messages));
	
		return js.toJSONString();
	}
	
	
	@SuppressWarnings("unchecked")
	private static JSONArray messagesJson(List<Messages> messages) {
		
        JSONArray messageArray=new JSONArray();
        Iterator<Messages> patientsItr=messages.iterator();
        JSONObject messagesJson=null;
		while(patientsItr.hasNext()) {
			Messages message=patientsItr.next();
			messagesJson = new JSONObject();
			messagesJson.put("message_id", message.getMessageId());
			messagesJson.put("label", message.getLabel());
			messagesJson.put("subject", message.getSubject());
			messagesJson.put("body", message.getBody());
			messagesJson.put("toUser", message.getToUser().getEmailId());
			messagesJson.put("fromUser", message.getFromUser().getEmailId());
			messagesJson.put("date", message.getCreatedDate().getTime());
			messagesJson.put("flag", message.getFlag());
			messagesJson.put("to_username", message.getToUser().getFirstName()+" "+message.getToUser().getLastName());
			messagesJson.put("from_username", message.getFromUser().getFirstName()+" "+message.getFromUser().getLastName());
			messageArray.add(messagesJson);
		}
	
		return messageArray;
		
	}

	@SuppressWarnings("unchecked")
	public static String getJson(Messages message) {
		
		JSONObject js = new JSONObject();
		JSONObject messageobj = new JSONObject();
		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Message retrieve successfully.");
		
		messageobj.put("message_id", message.getMessageId());
		messageobj.put("label", message.getLabel());
		messageobj.put("subject", message.getSubject());
		messageobj.put("body", message.getBody());
		messageobj.put("toUser", message.getToUser().getEmailId());
		messageobj.put("fromUser", message.getFromUser().getEmailId());
		messageobj.put("date", message.getCreatedDate().getTime());
		messageobj.put("flag", message.getFlag());
		messageobj.put("to_username", message.getToUser().getFirstName()+" "+message.getToUser().getLastName());
		messageobj.put("from_username", message.getFromUser().getFirstName()+" "+message.getFromUser().getLastName());
		
		js.put("message",messageobj );
		
		return js.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String getSendJson(Messages message) {
		
		JSONObject js = new JSONObject();
		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Message sent successfully.");
		js.put("message_id", message.getMessageId());
		return js.toJSONString();
	}
	
	
}
