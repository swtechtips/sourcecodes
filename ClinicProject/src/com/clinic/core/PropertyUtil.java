package com.clinic.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private Properties prop = null;
	
	public PropertyUtil (){
		loadProperties();
	}
	
	private void loadProperties(){
		if(prop == null)
			prop = new Properties();
		
		String filename = "Clinic.properties";
		InputStream propFile = getClass().getClassLoader().getResourceAsStream(filename);
		try {
			if (propFile == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}
			prop.load(propFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key) {
		if(prop == null) return null;
		return prop.getProperty(key);
	}
	
	public static void main(String[] arg){
		PropertyUtil obj = new PropertyUtil();
		System.out.println(obj.getProperty("db.driverName"));
	}

}
