package tunn.automation.appium.driver;

import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import tunn.automation.report.Log;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AppiumAndroidDriver extends AppiumBaseDriver {

	public AppiumAndroidDriver() {}

	public AppiumDriver createDriverWithCapabilities(String configFile, String environment) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + configFile));
		DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);

		if (environment.equalsIgnoreCase("LocalAndroid")) {
			capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+"/apps/"
					+ capabilities.getCapability("app"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capabilities.getCapability("device"));
			Log.info("Starting remote Android driver for: " + capabilities.toString());
			driver = new AndroidDriver<>(new URL(config.get("server").toString()), capabilities);
		}
		//Set Browser Stack capabilities
		else if (environment.contains("BS_Android")){
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
			    driver = new AndroidDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
		}

		return driver;
	}

}
