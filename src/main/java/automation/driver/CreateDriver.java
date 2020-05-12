package automation.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriver;

public class CreateDriver {

    private static CreateDriver instance = null;
    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();

    private  CreateDriver(){
    }

    public static CreateDriver getInstance(){
        if (instance == null) {
            instance = new CreateDriver();
        }
        return instance;
    }

    public synchronized void setDriver (WebDriver driver) {
        webDriver.set(driver);
    }

    public synchronized void  setDriver(AppiumDriver<MobileElement> driver){
        mobileDriver.set(driver);
    }

    private synchronized WebDriver getWebDriver(){
        return webDriver.get();
    }

    private synchronized AppiumDriver<MobileElement> getMobileDriver(){
        return  mobileDriver.get();
    }

    public synchronized AppiumDriver getCurrentMobileDriver(){
        return  getInstance().getMobileDriver();
    }

    public synchronized WebDriver getCurrentWebDriver(){
        return  getInstance().getWebDriver();
    }

}

