package com.zipwhip.toolkitapi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StopallConfig {

    private String blockedPhone;
    private String testPhone;

    public static StopallConfig create(String env) {
        ObjectMapper map = new ObjectMapper();
        try {
            File f = new File(StopallConfig.class.getClassLoader().getResource(String.format("%s.json", env)).getFile());
            return map.readValue(f, StopallConfig.class);
        } catch (IOException e) {
            return null;
        }
    }

    public StopallConfig() {}

    public String getBlockedPhone() {
        return blockedPhone;
    }

    public void setBlockedPhone(String blockedPhone) {
        this.blockedPhone = blockedPhone;
    }

    public String getTestPhone() {
        return testPhone;
    }

    public void setTestPhone(String testPhone) {
        this.testPhone = testPhone;
    }
}
