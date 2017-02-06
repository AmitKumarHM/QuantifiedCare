package com.qc.spring.enums;

public enum Slots {
	Morning("Morning"),
	Afternoon("Afternoon"),
	Evening("Evening"),
	AsNeeded("As Needed"),
	Weekly("Weekly"),
	Daily("Daily");
	
    String type;
	Slots(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
