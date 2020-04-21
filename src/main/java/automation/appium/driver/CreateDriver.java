package automation.appium.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CreateDriver {

    private static CreateDriver instance = null;
    private String browserHandle = null;
    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    private ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();
    private ThreadLocal<String> sessionID = new ThreadLocal<String>();
    private ThreadLocal<String> sessionBrowser = new ThreadLocal<String>();
    private ThreadLocal<String> sessionPlatform = new ThreadLocal<String>();
    private ThreadLocal<String> sessionVersion = new ThreadLocal<String>();
    private String getEnv = null;

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
        sessionID.set(((RemoteWebDriver)webDriver.get()).getSessionId().toString());
        sessionBrowser.set(((RemoteWebDriver) webDriver.get()).getCapabilities().getBrowserName());
        sessionPlatform.set(((RemoteWebDriver) webDriver.get()).getCapabilities().getPlatform().toString());

    }

    public  void  setDriver(AppiumDriver<MobileElement> driver){
        mobileDriver.set(driver);
        sessionBrowser.set(mobileDriver.get().getCapabilities().getBrowserName());
        sessionID.set(mobileDriver.get().getSessionId().toString());
        sessionPlatform.set(mobileDriver.get().getCapabilities().getPlatform().toString());
    }

    private WebDriver getDriver(){
        return webDriver.get();
    }

    private  AppiumDriver<MobileElement> getDriver(boolean mobile){
        return  mobileDriver.get();
    }

    //TODO: This is only set up to support Appium at this moment, not yet Webdriver
    public AppiumDriver getCurrentDriver(){
        return  getInstance().getDriver(true);
    }

    public String getSessionBrowser(){
        return sessionBrowser.get();
    }

    public  String getSessionID(){
        return sessionID.get();
    }

    public  String getSessionVersion(){
        return sessionVersion.get();
    }

    public  String getSessionPlatform(){
        return sessionPlatform.get();
    }
}

