package base;

import java.net.URL;

import org.testng.annotations.AfterClass;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {

    protected AppiumDriver driver;
    protected static final String PACKAGE_NAME = "com.appsfoundry.scoop";

    protected void setUp() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName("R9CM906E8XJ")
                .setAutomationName("UiAutomator2")
                .setAppPackage(PACKAGE_NAME)
                .amend("noReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

        Thread.sleep(3000);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void clearAppData() {
        try {
            if (driver != null) {
                ((AndroidDriver) driver).terminateApp(PACKAGE_NAME);
            }

            Process process = Runtime.getRuntime().exec("adb shell pm clear " + PACKAGE_NAME);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("App data cleared for: " + PACKAGE_NAME);
            } else {
                System.out.println("Failed to clear app data (exit code " + exitCode + ")");
            }
        } catch (Exception e) {
            System.out.println("Exception clearing app data: " + e.getMessage());
        }

    }

}
