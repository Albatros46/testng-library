package com.servet.library;

import static org.testng.Assert.assertTrue;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/** testng.xml'den parametre alma: @Parameters + @Optional (varsayılan değer). */
public class EnvironmentTest {

    private String env;
    private String baseUrl;

    @BeforeClass(alwaysRun = true)
    @Parameters({ "env", "baseUrl" })
    public void readParameters(@Optional("local") String env, @Optional("http://localhost:8080") String baseUrl) {
        this.env = env;
        this.baseUrl = baseUrl;
        Reporter.log("Ortam: " + env + " → " + baseUrl, true);
    }

    @Test(groups = { "smoke", "regression" })
    public void environmentIsConfigured() {
        assertTrue(baseUrl.startsWith("http"), "baseUrl http ile başlamalı");
        assertTrue(env.matches("local|qa|staging"), "bilinmeyen ortam: " + env);
    }
}
