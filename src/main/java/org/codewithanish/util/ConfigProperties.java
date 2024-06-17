package org.codewithanish.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private final Properties properties;

    private static ConfigProperties configProperties;

    private ConfigProperties()
    {
        ClassLoader classLoader = ConfigProperties.class.getClassLoader();
        try(InputStream is = classLoader.getResourceAsStream("config.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigProperties getInstance()
    {
        if(configProperties == null)
        {
            configProperties = new ConfigProperties();
        }
        return configProperties;
    }

    public String getProperty(String key)
    {
        return properties.getProperty(key);
    }

}
