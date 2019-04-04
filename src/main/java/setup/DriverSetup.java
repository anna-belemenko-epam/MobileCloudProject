package setup;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;

import static java.lang.String.format;

public class DriverSetup extends TestProperties {
    protected AppiumDriver driver;
    protected DesiredCapabilities capabilities;
    protected WebDriverWait wait;

    protected static String AUT;
    protected static String SUT;
    protected static String TEST_PLATFORM;
    protected static String DRIVER;
    protected static String DEVICE_NAME;
    protected static String API_KEY;
    protected static String APPIUM_HUB;
    protected static String PLATFORM_VERSION;
    protected static String PROJECT_NAME;

    private static AppiumDriver driverSingle = null;
    private static WebDriverWait waitSingle;

    protected DriverSetup(){

    }

    protected AppiumDriver driver() throws Exception {
        if (driverSingle == null)
            prepareDriver();
        return  driverSingle;
    }

    protected WebDriverWait driverWait(){
        return waitSingle;
    }

    protected void prepareDriver() throws Exception {
        AUT = getProperty("aut");
        String t_sut = getProperty("sut");
        SUT = t_sut == null ? null : "http://" + t_sut;
        TEST_PLATFORM = getProperty("platform");
        DRIVER = getProperty("driver");
        DEVICE_NAME = getProperty("deviceName");
        API_KEY = getProperty("apiKey");
        APPIUM_HUB = getProperty("appiumHub");
        PLATFORM_VERSION = getProperty("platformVersion");
        PROJECT_NAME = getProperty("projectName");
        capabilities = new DesiredCapabilities();
        String browserName;
        if ("Android".equals(TEST_PLATFORM)) {
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);
            browserName = "Chrome";
        } else if ("iOS".equals(TEST_PLATFORM)){
            browserName = "Safari";
        }
        else { throw new Exception("Unknown mobile platform");}
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, TEST_PLATFORM);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, PLATFORM_VERSION);
        if (AUT != null && SUT == null){
            File app = new File(AUT);
            capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        } else if (SUT != null && AUT == null){
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
        }
        else{
            throw new Exception("Unclear type of mobile app");
        }
        driver = new AppiumDriver(new URL(format("http://%s:%s@%s/wd/hub", PROJECT_NAME, API_KEY, APPIUM_HUB)), capabilities);
        wait = new WebDriverWait(driver, 10);
        if (driverSingle == null) driverSingle = driver;
        if (waitSingle == null) waitSingle = wait;
    }
}
