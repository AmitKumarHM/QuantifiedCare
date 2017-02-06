package com.qc.spring.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.qc.spring.entities.CarePlans;

public class PatientJSON {

	@SuppressWarnings("unchecked")
	public static String getJson(List<CarePlans> patients,int size) {

		JSONObject js = new JSONObject();

		js.put("result", "success");
		js.put("statusCode", "200");
		js.put("current_date", new Date().toString());
		js.put("message", "Patients list retrieve successfully.");
		js.put("patients", patientsJson(patients));
		js.put("total_patients", size);
		return js.toJSONString();
	}
	
	
	@SuppressWarnings("unchecked")
	private static JSONArray patientsJson(List<CarePlans> patients) {
		
        JSONArray patientArray=new JSONArray();
        Iterator<CarePlans> patientsItr=patients.iterator();
        JSONObject patientsJson=null;
        DateFormat formatter= new SimpleDateFormat("dd MMM YYYY");
        while(patientsItr.hasNext()) {
			CarePlans carePlans=patientsItr.next();
			patientsJson = new JSONObject();
			patientsJson.put("first_name", carePlans.getPatient().getFirstName());
			patientsJson.put("patient_id", carePlans.getPatient().getUserId());
			patientsJson.put("last_name", carePlans.getPatient().getLastName());
			patientsJson.put("gender", carePlans.getPatient().getGender());
			patientsJson.put("height", carePlans.getPatient().getHeight());
			patientsJson.put("age", carePlans.getPatient().getAge());
			patientsJson.put("address", carePlans.getPatient().getAddress());
			patientsJson.put("mobile_number", carePlans.getPatient().getMobileNumber());
			patientsJson.put("blood_group", carePlans.getPatient().getBloodGroup());
			patientsJson.put("medical_condition", carePlans.getPatient().getMedicalCondition());
			patientsJson.put("last_contact_date", ((carePlans.getPatient().getLastContactDate() != null)?formatter.format(carePlans.getPatient().getLastContactDate()):null));
			patientsJson.put("created_date", (carePlans.getPatient().getCreatedDate() == null)? null:carePlans.getPatient().getCreatedDate().getTime());
			patientsJson.put("profile_image", carePlans.getPatient().getProfile_image());
			patientsJson.put("state",(carePlans.getPatient().getState() == null)? null:carePlans.getPatient().getState().getState());
			patientArray.add(patientsJson);
		}
		return patientArray;
	}
	
}
