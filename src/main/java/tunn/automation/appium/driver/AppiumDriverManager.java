package tunn.automation.appium.driver;

public class AppiumDriverManager {
    private static ThreadLocal<AppiumBaseDriver> driver = new ThreadLocal<>();

    public static AppiumBaseDriver getDriver(){
        return driver.get();
    }

    public static void setDriver(AppiumBaseDriver driver) {
        AppiumDriverManager.driver.set(driver);
    }
}

