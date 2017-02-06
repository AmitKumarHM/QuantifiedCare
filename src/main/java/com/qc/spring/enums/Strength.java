package com.qc.spring.enums;

public enum Strength {

	Capsule("mg"),
	Concentrate("mL"),
	Cream("grams"),
	Drops("mL"),
	Gel("grams"),
	Injection("mL"),
	Inhaler("mL"),
	Kit("empty"),
	Liquid("mL"),
	Lotion("mL"),
	Lozenge("empty"),
	Ointment("grams"),
	Patch("grams"),
	Powder("grams"),
	Spray("mL"),
	Syrup("mL"),
	Tablet("mg");
	
	String type;
	Strength(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
