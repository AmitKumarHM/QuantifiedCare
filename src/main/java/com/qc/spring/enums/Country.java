package com.qc.spring.enums;

public enum Country {

	UNITED_STATE("United States");
	String type;
	Country(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
}
