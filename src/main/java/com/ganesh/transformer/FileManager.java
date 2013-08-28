package com.ganesh.transformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class FileManager {

  public static synchronized InputStream tryToLoadFromEverywhere(String filename) {
    InputStream result = null;
    result = System.class.getResourceAsStream(filename);
    if (result != null) {
      return result;
    }

    result = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    if (result != null) {
      return result;
    }

    result = Thread.currentThread().getClass().getResourceAsStream(filename);
    if (result != null) {
      return result;
    }

    result = ClassLoader.getSystemResourceAsStream(filename);
    if (result != null) {
      return result;
    }
    result = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
    if (result != null) {
      return result;
    }

    return result;
  }


  public static Properties getPropertiesObject(String fileName) throws IOException {
    Properties properties = new Properties();
    InputStream fileInputStream = tryToLoadFromEverywhere(fileName);
    properties.load(fileInputStream);
    fileInputStream.close();
    return properties;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Map<String, String> readPropertiesFileAsMap(String fileName) {
    Properties properties = null;
    try {
      properties = getPropertiesObject(fileName);
      return new LinkedHashMap<String, String>((Map) properties);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getStringFromFile(String fileName) {

    InputStream inputStream = tryToLoadFromEverywhere(fileName);
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder sb = new StringBuilder();

    try {
      String line = null;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();

  }
}
