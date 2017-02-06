package com.qc.spring.enums;

public enum Dosage {

	Capsule("capsule"),
	Concentrate("teaspoon"),
	Cream("application"),
	Drops("drop"),
	Gel("application"),
	Injection("injection"),
	Inhaler("puff"),
	Kit("kit"),
	Liquid("teaspoon"),
	Lotion("application"),
	Lozenge("lozenge"),
	Ointment("application"),
	Patch("patch"),
	Powder("application"),
	Spray("spray"),
	Syrup("teaspoon"),
	Tablet("tablet");
	
	String type;
	Dosage(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
}
