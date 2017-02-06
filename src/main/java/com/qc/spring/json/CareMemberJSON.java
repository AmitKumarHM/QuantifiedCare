package com.qc.spring.json;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CareMemberJSON {

	@SuppressWarnings("unchecked")
	public static String getJson(List<Long> careMemberIds) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Care Team added successfully.");
		Iterator<Long> itrCareMembers=careMemberIds.iterator();
		JSONArray careMemberIdArray=new JSONArray();
		while(itrCareMembers.hasNext()){
			careMemberIdArray.add(itrCareMembers.next());
		}
		js.put("caremember_id", careMemberIdArray);
		return js.toJSONString();
	}
	
	
}
