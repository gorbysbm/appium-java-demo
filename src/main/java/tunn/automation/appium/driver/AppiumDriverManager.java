package tunn.automation.appium.driver;

import io.appium.java_client.AppiumDriver;

public class AppiumDriverManager {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static AppiumDriver getDriver(){
        return driver.get();
    }

    public static void setDriver(AppiumDriver driver) {
        AppiumDriverManager.driver.set(driver);
    }
}

