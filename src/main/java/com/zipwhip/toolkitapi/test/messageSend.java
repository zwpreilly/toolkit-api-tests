package com.zipwhip.toolkitapi.test;

import com.zipwhip.toolkitapi.pojo.StopallConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class messageSend {

    StopallConfig config;

    @BeforeClass
    @Parameters("env")
    public void setup(String env) {
        config = StopallConfig.create(env);
    }

    @Test
    public void valid() {
        Assert.assertNotNull(config);
    }

    @Test
    public void invalid() {
        Assert.fail();
    }
}
