package tunn.automation.appium.driver;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.browserstack.local.Local;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobilePlatform;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import tunn.automation.report.Log;
import tunn.automation.utility.FilePaths;
import tunn.automation.utility.PropertiesLoader;

public class AppiumiOSDriver extends AppiumBaseDriver{


	private static Properties ios_configuration;
	private static Properties browser_configuration;
	private static Properties appium_configuration;
	private static Properties sauce_configuration;

	public AppiumiOSDriver() throws Exception {
	}

	public void createDriverWithCapabilities(String config_file, String environment) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + config_file));
		DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);

		if (environment.equalsIgnoreCase("LocaliOS")) {
			capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/apps/"
					+ capabilities.getCapability("app"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capabilities.getCapability("device"));
			Log.info("Starting remote iOS driver for: " + capabilities.toString());
			driver = new IOSDriver<>(new URL(config.get("server").toString()), capabilities);
		}
		//Set Browser Stack capabilities
		else if (environment.contains("BS_iOS")){
			String username = System.getenv("BROWSERSTACK_USERNAME");
			if(username == null) {
				username = (String) config.get("user");
			}

			String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
			if(accessKey == null) {
				accessKey = (String) config.get("key");
			}

			String app = System.getenv("BROWSERSTACK_APP_ID");
			if(app != null && !app.isEmpty()) {
				capabilities.setCapability("app", app);
			}

			if(capabilities.getCapability("browserstack.local") != null
					&& capabilities.getCapability("browserstack.local") == "true"){
				browserStackLocal = new Local();
				Map<String, String> options = new HashMap<String, String>();
				options.put("key", accessKey);
				browserStackLocal.start(options);
			}

			driver = new IOSDriver<IOSElement>(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
		}
	}
}
