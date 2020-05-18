package automation.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverCreator {

    private static DriverCreator instance = null;
    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();
    private ThreadLocal <String> sessionId = new ThreadLocal<>();

    private DriverCreator(){
    }

    public static DriverCreator getInstance(){
        if (instance == null) {
            instance = new DriverCreator();
        }
        return instance;
    }

    public synchronized void setDriver (WebDriver driver) {
        if(driver instanceof AppiumDriver){
            mobileDriver.set((AppiumDriver) driver);
            sessionId.set(mobileDriver.get().getSessionId().toString());
        }else{
            webDriver.set(driver);
            sessionId.set(((RemoteWebDriver) webDriver.get()).getSessionId().toString());
        }
    }

    private synchronized WebDriver getWebDriver(){
        return webDriver.get();
    }

    private synchronized AppiumDriver<MobileElement> getMobileDriver(){
        return  mobileDriver.get();
    }

    private synchronized String getSessionId(){
        return sessionId.get();
    }

    public static synchronized AppiumDriver getCurrentMobileDriver(){
        return  getInstance().getMobileDriver();
    }

    public static synchronized WebDriver getCurrentWebDriver(){
        return  getInstance().getWebDriver();
    }

    public static synchronized String getCurrentSessionId(){
        return  getInstance().getSessionId() != null ? getInstance().getSessionId()  :  "";
    }


}

