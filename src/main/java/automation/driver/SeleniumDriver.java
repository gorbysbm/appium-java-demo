package automation.driver;

import automation.utility.BrowserStackCapabilities;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.simple.JSONObject;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumDriver extends SeleniumBaseDriver{

    public RemoteWebDriver createDriverWithCapabilities(String configFile, String environment, Method method, ITestContext context)
            throws Exception {
        RemoteWebDriver driver = null;

        if (environment.startsWith("Desktop_Local")){
            driver = setupLocalWebdriver(environment);
        }
        else if (environment.startsWith("Desktop_Remote")){
            driver = setupRemoteWebdriver(environment);
        }
        else if (environment.startsWith("Desktop_BS")){
            driver = setupBrowserStackDriver(configFile, environment, method, context);
        }


        return driver;
    }

    private RemoteWebDriver setupBrowserStackDriver(String configFile, String environment, Method method, ITestContext context) throws Exception {
        RemoteWebDriver driver = null;
        JSONObject config = parseConfigFile(configFile);
        DesiredCapabilities capabilities = getDesiredCapabilities(environment, config);
        BrowserStackCapabilities browserStackCapabilities = new BrowserStackCapabilities(method, config, capabilities, context).invoke();
        String username = browserStackCapabilities.getUsername();
        String accessKey = browserStackCapabilities.getAccessKey();


        driver = new RemoteWebDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);

        return driver;
    }

    private RemoteWebDriver setupRemoteWebdriver(String environment) throws MalformedURLException {
        RemoteWebDriver driver = null;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (environment.equalsIgnoreCase("Desktop_Remote_Chrome")) {
            capabilities= DesiredCapabilities.chrome();
        }

        else if (environment.equalsIgnoreCase("Desktop_Remote_Firefox")) {
            capabilities= DesiredCapabilities.firefox();

        }
        driver = (new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
        return driver;
    }

    private RemoteWebDriver setupLocalWebdriver(String environment){
        RemoteWebDriver driver = null;
        if (environment.contains("Desktop_Local_Chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (environment.contains("Desktop_Local_Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (environment.contains("Desktop_Local_Safari")) {
            driver = new SafariDriver();
        } else if (environment.contains("Desktop_Local_Edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        return driver;
    }
}
