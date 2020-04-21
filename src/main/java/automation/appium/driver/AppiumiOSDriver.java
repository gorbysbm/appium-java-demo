package automation.appium.driver;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import automation.report.HtmlReporter;
import automation.utility.BrowserStackCapabilities;
import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import automation.report.Log;
import org.testng.ITestContext;

public class AppiumiOSDriver extends AppiumBaseDriver{


	private static Properties ios_configuration;
	private static Properties browser_configuration;
	private static Properties appium_configuration;
	private static Properties sauce_configuration;

	public AppiumiOSDriver() throws Exception {
	}

	public AppiumDriver createDriverWithCapabilities(String config_file, String environment, Method method, ITestContext context) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + config_file));
		DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);

		if (environment.equalsIgnoreCase("LocaliOS")) {
			capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/apps/"
					+ capabilities.getCapability("app"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capabilities.getCapability("device"));
			HtmlReporter.info(">>Starting Appium iOS driver for: " + capabilities.toString());
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
