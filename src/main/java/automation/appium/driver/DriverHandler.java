package automation.appium.driver;


import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;

import java.lang.reflect.Method;

public class DriverHandler {

	public AppiumDriver startDriver(AppiumDriver driver, String configFile, String environment, Method method, ITestContext context) throws Exception {
		if (environment.contains("Android")) {
			AppiumAndroidDriver android = new AppiumAndroidDriver();
			driver = android.createDriverWithCapabilities(configFile, environment, method, context);
		} else if (environment.contains("iOS")) {
			AppiumiOSDriver ios = new AppiumiOSDriver();
			driver = ios.createDriverWithCapabilities(configFile, environment, method, context);
		} else {
			throw new Exception(String.format("The environment [%s] is not supported", environment));
		}

		return driver;
	}

	public WebDriver startDriver(WebDriver driver, String configFile, String environment, Method method, ITestContext context) throws Exception {
		if (environment.contains("chrome")) {
			driver = new ChromeDriver();
		}
		else {
			throw new Exception(String.format("The environment [%s] is not supported", environment));
		}
		return driver;
	}
}

