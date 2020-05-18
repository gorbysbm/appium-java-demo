package automation.driver;

import automation.utility.BrowserStackCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;

public class SeleniumDriver extends SeleniumBaseDriver{

    public RemoteWebDriver createDriverWithCapabilities(String config_file, String environment, Method method, ITestContext context) throws Exception {
        RemoteWebDriver driver = null;
//        JSONParser parser = new JSONParser();
//        JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + config_file));
//        DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (environment.equalsIgnoreCase("RemoteChrome")) {
            capabilities= DesiredCapabilities.chrome();
        }

        else if (environment.equalsIgnoreCase("RemoteFirefox")) {
            capabilities= DesiredCapabilities.firefox();
        }
        driver = (new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
        return driver;
    }
}
