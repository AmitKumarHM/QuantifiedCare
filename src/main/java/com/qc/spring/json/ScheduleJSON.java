package com.qc.spring.json;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;

import com.qc.spring.entities.Schedule;

public class ScheduleJSON {

	@SuppressWarnings("unchecked")
	public static String getJson(List<Schedule> schedules) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Medications list retrieve successfully.");
		js.put("schedules", schedules);
	
		return js.toJSONString();
	}
}
