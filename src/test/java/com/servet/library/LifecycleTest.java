package com.servet.library;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/** TestNG yaşam döngüsü: konsolda hangi metodun hangi sırada çalıştığını gösterir. */
public class LifecycleTest {

    private static void log(String message) {
        Reporter.log("[lifecycle] " + message, true);
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log("@BeforeSuite  → tüm suite başlamadan bir kez (ör. rapor klasörü, test verisi)");
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        log("@BeforeTest   → testng.xml'deki her <test> bloğundan önce");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        log("@BeforeClass  → bu sınıftaki ilk testten önce (ör. WebDriver başlat)");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        log("@BeforeMethod → her test metodundan önce");
    }

    @Test(groups = "smoke")
    public void first() {
        log("@Test first");
    }

    @Test(groups = "smoke")
    public void second() {
        log("@Test second");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        log("@AfterMethod  → her test metodundan sonra (ör. hata varsa screenshot)");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        log("@AfterClass   → bu sınıftaki son testten sonra (ör. driver.quit())");
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        log("@AfterTest    → her <test> bloğundan sonra");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log("@AfterSuite   → tüm suite bittikten sonra bir kez");
    }
}