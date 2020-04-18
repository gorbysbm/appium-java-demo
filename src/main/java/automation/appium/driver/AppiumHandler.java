package automation.appium.driver;


import io.appium.java_client.AppiumDriver;

import java.lang.reflect.Method;

public class AppiumHandler {

	public AppiumDriver startDriver(String configFile, String environment, Method method) throws Exception {
		AppiumDriver driver;

		if (environment.contains("Android")) {
			AppiumAndroidDriver android = new AppiumAndroidDriver();
			driver = android.createDriverWithCapabilities(configFile, environment, method);
		} else if (environment.contains("iOS")) {
			AppiumiOSDriver ios = new AppiumiOSDriver();
			driver = ios.createDriverWithCapabilities(configFile, environment, method);
		}
		else {
			throw new Exception(String.format("The environment [%s] is not supported", environment));
		}

		return driver;
	}
}
