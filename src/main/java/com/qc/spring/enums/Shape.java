package com.qc.spring.enums;

public enum Shape {

	Sided3("3 sided"),
	Sided5("5 sided"),
	Sided6("6 sided"),
	Sided7("7 sided"),
	Sided8("8 sided"),
	Barrel("Barrel"),
	Circular("Circular"),
	Diamond("Diamond"),
	Egg("egg"),
	Figure8("Figure 8"),
	Gear("Gear"),
	HalfCircle("Half Circle"),
	Heart("Heart"),
	Kidney("Kidney"),
	Oblong("Oblong"),
	Oval("Oval"),
	Rectangular("Rectangular"),
	Square("Square"),
	Trapezoid("Trapezoid");

	String type;
	Shape(String type) {
        this.type = type;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
}
