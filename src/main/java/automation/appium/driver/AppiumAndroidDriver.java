package automation.appium.driver;

import automation.report.HtmlReporter;
import automation.utility.BrowserStackCapabilities;
import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import automation.report.Log;
import org.testng.ITestContext;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AppiumAndroidDriver extends AppiumBaseDriver {

	public AppiumAndroidDriver() {}

	public AppiumDriver createDriverWithCapabilities(String configFile, String environment, Method method, ITestContext context) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + configFile));
		DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);

		if (environment.equalsIgnoreCase("LocalAndroid")) {
			capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/apps/"
					+ capabilities.getCapability("app"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capabilities.getCapability("device"));
			HtmlReporter.info(">>Starting Appium Android driver for: " + capabilities.toString());
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
