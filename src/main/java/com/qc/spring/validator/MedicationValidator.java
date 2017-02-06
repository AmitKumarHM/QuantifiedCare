package com.qc.spring.validator;

import com.qc.spring.constants.ResponseMessages;
import com.qc.spring.entities.Medications;

public class MedicationValidator {

	public String validateMedication(Medications medications) {
			
		if(medications.getName() == null || medications.getName().length() == 0) {
		  return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getColor() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getForm() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getShape() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getStrength() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getQuantityUnit() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getTotalQuantity() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getDosageUnit() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else if(medications.getDosage() == null){
			return ResponseMessages.REQUIRED_FIELDS;	
		}else {
			return null;
		}
			
	} 
	
}
