package automation.driver;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;

import automation.utility.BrowserStackCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.testng.ITestContext;

public class AppiumiOSDriver extends AppiumBaseDriver{

	public AppiumiOSDriver() {

	}

	public AppiumDriver createDriverWithCapabilities(String configFile, String environment, Method method, ITestContext context) throws Exception {
		JSONObject config = parseConfigFile(configFile);
		DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);

		if (environment.equalsIgnoreCase("LocaliOS")) {
			capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/apps/"
					+ capabilities.getCapability("app"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capabilities.getCapability("device"));
			driver = new IOSDriver<>(new URL(config.get("server").toString()), capabilities);
		}
		//Set Browser Stack capabilities
		else if (environment.contains("BS_iOS")){
			BrowserStackCapabilities browserStackCapabilities = new BrowserStackCapabilities(method, config, capabilities, context).invoke();
			String username = browserStackCapabilities.getUsername();
			String accessKey = browserStackCapabilities.getAccessKey();

			driver = new IOSDriver<IOSElement>(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);

		}
		return driver;
	}

}
