package tunn.automation.appium.driver;


public class AppiumHandler {

	public AppiumBaseDriver startDriver(String configFile, String environment) throws Exception {
		AppiumBaseDriver driver;

		if (environment.contains("Android")) {
			AppiumAndroidDriver android = new AppiumAndroidDriver();
			android.createDriverWithCapabilities(configFile, environment);
			driver = android;
		} else if (environment.contains("iOS")) {
			AppiumiOSDriver ios = new AppiumiOSDriver();
			ios.createDriverWithCapabilities(configFile, environment);
			driver = ios;
		}
		else {
			throw new Exception(String.format("The environment [%s] is not supported", environment));
		}

		AppiumDriverManager.setDriver(driver);
		return AppiumDriverManager.getDriver();
	}
}
