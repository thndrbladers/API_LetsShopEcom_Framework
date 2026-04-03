package com.apiletsshopecom.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	private static ConfigManager instance;

	private final Properties properties;

	private final String environment;

	private ConfigManager() {
		this.environment = resolveEnvironment();
		this.properties = new Properties();
		loadProperties();
	}

	private void loadProperties() {

		String fileName = "config/" + environment + ".properties";
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {// Loads
																								// config/{env}.properties
																								// from the classpath at
																								// runtime. Good for
																								// CI/CD
			if (input == null) {
				throw new RuntimeException("Configuration file not found: " + fileName);
			}
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration: " + fileName, e);
		}

	}

	public static synchronized ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	private String resolveEnvironment() {
		String env = System.getProperty("env");

		if (env == null || env.isBlank()) {
			env = System.getenv("ENV");
		}
		if (env == null || env.isBlank()) {
			env = "svt";
		}

		return env;
	}

	public String getProperty(String key) {

		String sysProp = System.getProperty(key);
		if (sysProp != null) {
			return sysProp;
		}

		return this.properties.getProperty(key);

	}

	public String getBaseUrl() {
		return this.getProperty("base.url");
	}

	public String getAuthToken() {

		return this.properties.getProperty("auth.token");
	}

	public void setProperty(String key, String token) {
		this.properties.setProperty(key, token);
	}

}
