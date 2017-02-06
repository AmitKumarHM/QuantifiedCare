package com.qc.spring.configs;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.InitializingBean;


/**
 * The Class Configuration.
 */
public class Configuration implements InitializingBean {
    
    /** The properties. */
    public PropertiesConfiguration properties;
    
    /**
     * Instantiates a new configuration.
     */
    public Configuration(){
    }

    /**
     * Sets the properties.
     *
     * @param properties the new properties
     */
    public void setProperties(PropertiesConfiguration properties) {
        this.properties = properties;
    }
    
   
    @Override
    public void afterPropertiesSet() throws Exception {
    }

    /**
     * Gets the.
     *
     * @param key the key
     * @return the string
     */
    public String get(String key) {
        return properties.getString(key);
    }
    
   
    @Override
    public String toString() {
    	return "ENV working....";
    }
}