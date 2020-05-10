package automation.appium.driver;

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

    public void setDriver (WebDriver driver) {
        webDriver.set(driver);
    }

    public  void  setDriver(AppiumDriver<MobileElement> driver){
        mobileDriver.set(driver);
    }

    private WebDriver getWebDriver(){
        return webDriver.get();
    }

    private  AppiumDriver<MobileElement> getMobileDriver(){
        return  mobileDriver.get();
    }

    //TODO: This is only set up to support Appium at this moment, not yet Webdriver
    public AppiumDriver getCurrentMobileDriver(){
        return  getInstance().getMobileDriver();
    }

    public WebDriver getCurrentWebDriver(){
        return  getInstance().getWebDriver();
    }

}

