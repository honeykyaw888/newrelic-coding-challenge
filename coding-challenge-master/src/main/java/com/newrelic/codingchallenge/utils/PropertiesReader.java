package com.newrelic.codingchallenge.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	public static String readProperty(String key) throws IOException {
		Properties prop = readPropertiesFile("application.properties");
		return prop.getProperty(key);
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	public static int readPropertyInInteger(String key) throws IOException {
		String str = PropertiesReader.readProperty(key);
		int intVal;
		try {
			intVal = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			intVal = 0;
		}
		return intVal;
	}

}
