package com.siblinks.ws.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siblinks.ws.service.Application;

public class ReadProperties {
	
	private final Log logger = LogFactory.getLog(getClass());
    private static Properties prop = null;

    ReadProperties() {

        InputStream input = null;
        String rule = Application.rule;

        if (rule.equals("dev")) {
            input = getClass().getClassLoader().getResourceAsStream(
                "configuration_dev.properties");
        } else {
            input = getClass().getClassLoader().getResourceAsStream(
                "configuration.properties");
        }
        try {
            prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static String getProperties(String directory)
            throws FileNotFoundException {
        if(prop == null) {
            new ReadProperties();
         }
        return prop.getProperty(directory).toString();
    }
}
