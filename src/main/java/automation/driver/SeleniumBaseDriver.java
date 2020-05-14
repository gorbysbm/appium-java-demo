package automation.driver;

import automation.report.HtmlReporter;
import automation.report.Log;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SeleniumBaseDriver extends BaseDriver {
	protected WebDriver driver;
	private int EXPLICIT_WAIT_TIMEOUT = 6;
	public static String BASE_URL = "http://www.newtours.demoaut.com";

	public SeleniumBaseDriver() {
		super();
	}

	public SeleniumBaseDriver(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}


	public void setExplicitWaitToDefault() {
		setExplicitWait(EXPLICIT_WAIT_TIMEOUT);
	}
}
