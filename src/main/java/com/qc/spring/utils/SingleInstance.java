package com.qc.spring.utils;

import java.util.Random;


/**
 * The Class SingleInstance.
 */
public class SingleInstance {

	/** The random instance. */
	private static Random randomInstance = null;
	
	/** The instance. */
	private static SingleInstance instance = null;
	
	/**
	 * Instantiates a new single instance.
	 */
	private SingleInstance() {
	}

	/**
	 * Gets the random instance.
	 *
	 * @return the random instance
	 */
	public static Random getRandomInstance() {
		if (randomInstance == null) {
			randomInstance = new Random();
		}
		return randomInstance;
	}
	
	/**
	 * Gets the single instance of SingleInstance.
	 *
	 * @return single instance of SingleInstance
	 */
	public static SingleInstance getInstance() {
		if (instance == null) {
			instance = new SingleInstance();
		}
		return instance;
	}
	
	public static void main(String...a){
		System.out.println(new Long(9).equals(9L));
		
		
	}

}
