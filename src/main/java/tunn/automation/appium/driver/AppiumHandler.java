package tunn.automation.appium.driver;


import io.appium.java_client.AppiumDriver;

public class AppiumHandler {

	public AppiumDriver startDriver(String configFile, String environment) throws Exception {
		AppiumDriver driver;

		if (environment.contains("Android")) {
			AppiumAndroidDriver android = new AppiumAndroidDriver();
			driver = android.createDriverWithCapabilities(configFile, environment);
		} else if (environment.contains("iOS")) {
			AppiumiOSDriver ios = new AppiumiOSDriver();
			driver = ios.createDriverWithCapabilities(configFile, environment);
		}
		else {
			throw new Exception(String.format("The environment [%s] is not supported", environment));
		}

		return driver;
	}
}
