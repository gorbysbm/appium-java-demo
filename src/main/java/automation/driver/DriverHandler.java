package automation.driver;


import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;

import java.lang.reflect.Method;

public class DriverHandler {

	public static synchronized void startDriver(String configFile, String environment, Method method, ITestContext context) throws Exception {
		WebDriver driver = null;
		///////////////////////MOBILE///////////////////////////////
		if (environment.contains("Android")) {
			AppiumAndroidDriver android = new AppiumAndroidDriver();
			driver = android.createDriverWithCapabilities(configFile, environment, method, context);
		} else if (environment.contains("iOS")) {
			AppiumiOSDriver ios = new AppiumiOSDriver();
			driver = ios.createDriverWithCapabilities(configFile, environment, method, context);
		}

		////////////////////////REMOTE DESKTOP///////////////////////////////
		else if (environment.startsWith("Desktop")) {
			SeleniumDriver selenium = new SeleniumDriver();
			driver = selenium.createDriverWithCapabilities(configFile, environment, method, context);
		}
		else {
			throw new Exception(String.format("The driver environment [%s] is not supported", environment));
		}
		DriverCreator.getInstance().setDriver(driver);
	}
}




