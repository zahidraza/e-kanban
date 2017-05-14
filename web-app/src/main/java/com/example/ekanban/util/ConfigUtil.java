package com.example.ekanban.util;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by mdzahidraza on 14/05/17.
 * Configuration utility class for getting application and configuration properties.
 * This is static class. It should not be instantiated.
 */
public class ConfigUtil {

    private static final String appFile = "application.properties";
    private static final String configFile = "config.properties";
    private static Properties appProps = new Properties();

    /**
     * private constructor to prevent instantiation.
     */
    private ConfigUtil() {}

    static {
        try {
            InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(appFile);
            appProps.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getAppProperty(String key) {
        return appProps.getProperty(key);
    }

    public static String getAppProperty(String key, String defaultValue) {
        return appProps.getProperty(key, defaultValue);
    }

    /**
     * get all config properties
     * @return Properties object containing all configurations
     */
    public static Properties getConfigProperties() {
        Properties props = new Properties();
        InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(configFile);
        try {
            props.load(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return props;
    }

    /**
     * get specific config property
     * @param key
     * @return
     */
    public static String getConfigProperty(String key){
        return getConfigProperty(key, null);
    }

    /**
     * get specific config property
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getConfigProperty(String key, String defaultValue){
        Properties props = new Properties();
        InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(configFile);
        try {
            props.load(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return props.getProperty(key, defaultValue);
    }

    /**
     * Set specific config property
     * @param key
     * @param value
     */
    public static void setConfigProperty(String key, String value){
        URL url = ConfigUtil.class.getClassLoader().getResource(configFile);
        String path = null;
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;
        try {
            path = url.toURI().getPath();
            input = new FileInputStream(path);
            prop.load(input);
            input.close();
            output = new FileOutputStream(path);
            prop.setProperty(key, value);
            prop.store(output, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
