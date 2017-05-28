package com.example.ekanban.util;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by mdzahidraza on 14/05/17.
 * Configuration utility class for getting application and configuration properties.
 * This is static class. It should not be instantiated.
 */
public class AutomationUtil {

    private static final String autPropsFile = getEkanbanHome() + File.separator + "conf" + File.separator + "automation.properties";
    private static Properties autProps = new Properties();

    /**
     * private constructor to prevent instantiation.
     */
    private AutomationUtil() {}

    public static String getEkanbanHome() {
        return System.getenv("EKANBAN_HOME");
    }



    static {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(autPropsFile));
            autProps.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key) {
        return autProps.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return autProps.getProperty(key, defaultValue);
    }

    /**
     * get all config properties
     * @return Properties object containing all configurations
     */
    public static Properties getProperties() {
        return autProps;
    }

    /**
     * Set specific automation property
     * @param key
     * @param value
     */
    public static void setProperty(String key, String value){
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;
        try {
            input = new FileInputStream(autPropsFile);
            prop.load(input);
            input.close();
            output = new FileOutputStream(autPropsFile);
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
