package com.qc.spring.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.qc.spring.entities.Feeds;
import com.qc.spring.entities.Health;
import com.qc.spring.entities.Symptoms;

public class HealthJSON {

	
	public static void main(String...a){
		
		Calendar c=Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		DateFormat df=new SimpleDateFormat("YYYY-MM-dd");
		System.out.println(df.format(c.getTime()));      // This past Sunday [ May include today ]
		c.add(Calendar.DATE,7);
		System.out.println(df.format(c.getTime()));      // Next Sunday
		c.add(Calendar.DATE,7);
		System.out.println(df.format(c.getTime())); 	
			
	
	
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static String getJson(List<Feeds> feeds,Health health,Symptoms symptoms) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Health summary found.");

		js.put("data", healthJson(feeds,health,symptoms));
        js.put("user", health.getUser());
		return js.toJSONString();
	}
	
	
	@SuppressWarnings("unchecked")
	private static JSONObject healthJson(List<Feeds> feeds,Health health,Symptoms symptoms) {
		
		JSONObject dataJson = new JSONObject();
		JSONArray feedsArray=new JSONArray();
		if(feeds !=null) {
		Iterator<Feeds> itrFeeds=feeds.iterator();
		JSONObject js;
		while(itrFeeds.hasNext()) {
			js = new JSONObject();
			Feeds feed=itrFeeds.next();
			js.put("description",feed.getDescription());
			js.put("timestamp",feed.getUpdatedDate().getTime());
			feedsArray.add(js);}
		}
		
		
		JSONObject symptomsJon = new JSONObject();
		if(symptoms !=null) {
		symptomsJon.put("value",symptoms.getSymptom());
		symptomsJon.put("timestamp",symptoms.getUpdatedDate().getTime());
		symptomsJon.put("status",symptoms.getStatus().getStatusId());}
		
		
		JSONObject weightJson = new JSONObject();
		if(health !=null) {
		weightJson.put("value",health.getWeight());
		weightJson.put("timestamp",health.getWeightDate().getTime());
		weightJson.put("status",health.getWeightStatus().getStatusId());
		weightJson.put("unit",health.getWeightUnit());
		}
		
		JSONObject medAdherenceJson = new JSONObject();
		if(health !=null) {
			medAdherenceJson.put("value",health.getMedAdherence());
			medAdherenceJson.put("timestamp",health.getMedAdherenceDate().getTime());
			medAdherenceJson.put("status",health.getMedStatus().getStatusId());
			medAdherenceJson.put("unit",health.getMedicalAdherenceUnit());
		}
		
		JSONObject bloodPressureJson = new JSONObject();
		if(health !=null) {
		bloodPressureJson.put("value",health.getBp());
		bloodPressureJson.put("timestamp",health.getBpDate().getTime());
		bloodPressureJson.put("status",health.getBpStatus().getStatusId());
		bloodPressureJson.put("unit",health.getBpUnit());
		}
		
		
		JSONObject bmiJson = new JSONObject();
		if(health !=null) {
		bmiJson.put("value",health.getBmi());
		bmiJson.put("timestamp",health.getBmiDate().getTime());
		bmiJson.put("status",health.getBmiStatus().getStatusId());
		bmiJson.put("unit",health.getBmiUnit());
		}
		
		
		JSONObject stepsJson = new JSONObject();
		if(health !=null) {
		stepsJson.put("value",health.getPhysicalActivity());
		stepsJson.put("timestamp",health.getPhysactDate().getTime());
		stepsJson.put("status",health.getPhysActStatus().getStatusId());
		stepsJson.put("unit",health.getPhysicalActivityUnit());
		}
		
		dataJson.put("feeds", feedsArray);
		dataJson.put("symptoms", symptomsJon);
		dataJson.put("weight", weightJson);
		dataJson.put("med_tracking",medAdherenceJson );
		dataJson.put("blood_pressure", bloodPressureJson);
		dataJson.put("bmi", bmiJson);
		dataJson.put("steps", stepsJson);
		return dataJson;
	}
}
