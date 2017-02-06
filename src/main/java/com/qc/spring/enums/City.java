package com.qc.spring.enums;

public enum City {

	New_York_City("New York City"),
	Los_Angeles("Los Angeles"),
	Miami("Miami"),
	Boston("Boston"),
	San_Francisco("San Francisco"),
	Louisville("Louisville"),
	Philadelphia("Philadelphia");
	
	String type;
	City(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
	
}
