package automation.driver;


import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestContext;

import java.lang.reflect.Method;

public class DriverHandler {

	public synchronized AppiumDriver startDriver(AppiumDriver driver, String configFile, String environment, Method method, ITestContext context) throws Exception {
		if (environment.contains("Android")) {
			AppiumAndroidDriver android = new AppiumAndroidDriver();
			driver = android.createDriverWithCapabilities(configFile, environment, method, context);
		} else if (environment.contains("iOS")) {
			AppiumiOSDriver ios = new AppiumiOSDriver();
			driver = ios.createDriverWithCapabilities(configFile, environment, method, context);
		} else {
			throw new Exception(String.format("The mobile environment [%s] is not supported", environment));
		}

		return driver;
	}

	public synchronized WebDriver startDriver(WebDriver driver, String configFile, String environment, Method method, ITestContext context) throws Exception {
		if (environment.contains("LocalChrome")) {
			driver = new ChromeDriver();
			driver.manage().window().setSize(new Dimension(124,124));
		}else if (environment.contains("LocalFirefox")) {
			driver = new FirefoxDriver();
		}else if (environment.contains("LocalSafari")) {
			driver = new SafariDriver();
		}else if (environment.contains("LocalEdge")) {
			driver = new EdgeDriver();
		}
		else {
			throw new Exception(String.format("The web environment [%s] is not supported", environment));
		}
		return driver;
	}
}

