package automation.driver;

import automation.utility.BrowserStackCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;

public class AppiumAndroidDriver extends AppiumBaseDriver {

	public AppiumAndroidDriver() {
	}

	public AppiumDriver createDriverWithCapabilities(String configFile, String environment, Method method, ITestContext context) throws Exception {
		JSONObject config = parseConfigFile(configFile);
		DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);

		if (environment.equalsIgnoreCase("LocalAndroid")) {
			capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/apps/"
					+ capabilities.getCapability("app"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capabilities.getCapability("device"));
			driver = new AndroidDriver<>(new URL(config.get("server").toString()), capabilities);
		}
		//Set Browser Stack capabilities
		else if (environment.contains("BS_Android")){
			BrowserStackCapabilities browserStackCapabilities = new BrowserStackCapabilities(method, config, capabilities, context).invoke();
			String username = browserStackCapabilities.getUsername();
			String accessKey = browserStackCapabilities.getAccessKey();

			driver = new AndroidDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
		}

		return driver;
	}

}
