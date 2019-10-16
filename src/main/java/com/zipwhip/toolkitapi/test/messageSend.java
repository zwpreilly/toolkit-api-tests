package com.zipwhip.toolkitapi.test;

import com.zipwhip.toolkitapi.pojo.StopallConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;

public class messageSend {

    StopallConfig config;
    MessageHelper helper;

    @BeforeTest
    @Parameters("env")
    public void setup(String env) {

        config = StopallConfig.create(env);
        helper = new MessageHelper();
    }

    @BeforeMethod
    public void beforeTest() {

        sendAndSleep(config.getBlockedPhone(), config.getBlockedPhonePass(), config.getTestPhone(), "unstop", config.getBaseUrl());
    }

    @DataProvider(name = "Stops")
    public Iterator<Object[]> getStops() {

        ArrayList<Object[]> args = new ArrayList<>();
        //English
        args.add(new Object[] {"stop", "unstop"});
        args.add(new Object[] {"STOP", "UNSTOP"});
        args.add(new Object[] {"sToP", "uNsToP"});

        //French without accent
        args.add(new Object[] {"arret", "nonarret"});
        args.add(new Object[] {"ARRET", "NONARRET"});
        args.add(new Object[] {"ArREt", "nOnArReT"});

        //French with accent
        args.add(new Object[] {"arrêt", "nonarrêt"});
        args.add(new Object[] {"ARRÊT", "NONARRÊT"});

        return args.iterator();
    }

    @Test(enabled = true, dataProvider = "Stops")
    public void stopUnstop(Object st, Object unst) {

        String stop = (String) st;
        String unstop = (String) unst;
        sendAndSleep(config.getBlockedPhone(), config.getBlockedPhonePass(), config.getTestPhone(), stop, config.getBaseUrl());
        Response r = sendAndSleep(config.getTestPhone(), config.getTestPhonePass(), config.getBlockedPhone(), "Test message", config.getBaseUrl());
        String s = r.getBody().jsonPath().get("response").toString();
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertTrue(s.contains("You cannot send to line opted out numbers"));

        r = sendAndSleep(config.getBlockedPhone(), config.getBlockedPhonePass(), config.getTestPhone(), unstop, config.getBaseUrl());
        s = r.getBody().jsonPath().get("response").toString();
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertFalse(s.contains("You cannot send to line opted out numbers"));

        r = sendAndSleep(config.getTestPhone(), config.getTestPhonePass(), config.getBlockedPhone(), "Test message", config.getBaseUrl());
        s = r.getBody().jsonPath().get("response").toString();
        Assert.assertEquals(r.getStatusCode(), 200);
        Assert.assertFalse(s.contains("You cannot send to line opted out numbers"));
    }

    @Test(enabled = false)
    public void stopAll() {

        //TODO: Update to reflect the changes in the stopUnstop test.
        Response r = helper.sendMessage(config.getBlockedPhone(), config.getBlockedPhonePass(), config.getTestPhone(), "stopall", config.getBaseUrl());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("SLEEP FAILED");
            e.printStackTrace();
        }

        r = helper.sendMessage(config.getTestPhone(), config.getTestPhonePass(), config.getBlockedPhone(), "Test stopall", config.getBaseUrl());
        Assert.assertEquals(r.getStatusCode(), 400);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("SLEEP FAILED");
            e.printStackTrace();
        }

        r = helper.sendMessage(config.getBlockedPhone(), config.getBlockedPhonePass(), config.getTestPhone(), "unstop", config.getBaseUrl());
        Assert.assertEquals(r.getStatusCode(), 200);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("SLEEP FAILED");
            e.printStackTrace();
        }

        r = helper.sendMessage(config.getTestPhone(), config.getTestPhonePass(), config.getBlockedPhone(), "Test stopall", config.getBaseUrl());
        Assert.assertEquals(r.getStatusCode(), 200);
    }

    private Response sendAndSleep(String from, String fromPw, String to, String message, String url, long sleepTime) {

        Response r = helper.sendMessage(from, fromPw, to, message, url);
        Assert.assertEquals(r.getStatusCode(), 200);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("SLEEP FAILED");
            e.printStackTrace();
        }
        return r;
    }

    private Response sendAndSleep(String from, String fromPw, String to, String message, String url) {
        return sendAndSleep(from, fromPw, to, message, url, 2000);
    }
}
