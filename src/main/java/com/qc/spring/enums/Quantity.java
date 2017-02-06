package com.qc.spring.enums;

public enum Quantity {

	Capsule("capsule"),
	Concentrate("mL"),
	Cream("grams"),
	Drops("mL"),
	Gel("grams"),
	Injection("mL"),
	Inhaler("puff"),
	Kit("kit"),
	Liquid("mL"),
	Lotion("mL"),
	Lozenge("lozenge"),
	Ointment("gram"),
	Patch("patch"),
	Powder("grams"),
	Spray("mL"),
	Syrup("mL"),
	Tablet("tablet");
	
	String type;
	Quantity(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
