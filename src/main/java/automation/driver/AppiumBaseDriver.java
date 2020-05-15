package automation.driver;

import java.time.Duration;
import java.util.*;

import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.report.HtmlReporter;
import automation.report.Log;
import automation.utility.PropertiesLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class AppiumBaseDriver extends BaseDriver{
	protected AppiumDriver driver;
	int EXPLICIT_WAIT_TIMEOUT = 6;

	public AppiumBaseDriver() {
		super();
	}

	public AppiumBaseDriver(WebDriver driver) {
		super(driver);
		this.driver = (AppiumDriver) driver;
		setExplicitWaitToDefault();
	}

	//Use to scroll into view by element's text
	public WebElement scrollIntoView(String text, String predicate) {
		WebElement foundElement = null;
		HtmlReporter.info(String.format(">>Scrolling to element with text [%s]", text));
		try {
			if (isAndroidDriver()) {
				String locator = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\"%s\"))", text);
				By by = MobileBy.AndroidUIAutomator(locator);
				foundElement = driver.findElement(by);
			}
			else if (isIOSDriver()) {
				HashMap<String, String> scrollObject = new HashMap<>();
				scrollObject.put("predicateString", String.format("%s == '%s'",predicate, text));
				scrollObject.put("toVisible", "true");
				scrollObject.put("direction", "down");
				driver.executeScript("mobile: scroll", scrollObject);
				foundElement = ((IOSDriver) driver).findElementByIosNsPredicate(String.format("%s == '%s'",predicate, text));
			}
		} catch (Exception e) {
			HtmlReporter.fail(String.format(">>Failed to scroll to element with text [%s]", text));
			throw e;
		}
		return foundElement;
	}

	public WebElement findElementByText(String text, String predicate) {
		WebElement foundElement = null;
		HtmlReporter.info(String.format(">>Finding element with text [%s]", text));
		try {
			if (isAndroidDriver()) {
				String locator = String.format("new UiSelector().text(\"%s\")", text);
				foundElement = waitForPresenceOfElement(MobileBy.AndroidUIAutomator(locator));
			}
			else if (isIOSDriver()) {
				String locator =  String.format("%s == '%s'",predicate, text);
				foundElement = waitForPresenceOfElement(MobileBy.iOSNsPredicateString(locator));
			}
		} catch (Exception e) {
			HtmlReporter.fail(String.format(">>Failed to find element with text [%s]", text));
			throw e;
		}
		return foundElement;
	}

	public AppiumDriver<WebElement> getDriver() {
		return driver;
	}


	public boolean isIOSDriver() {
		return driver instanceof IOSDriver<?> ? true : false;
	}

	public boolean isAndroidDriver() {
		return driver instanceof AndroidDriver<?> ? true : false;
	}

	/////////////////////////////OLDER HELPERS////////////////////////

	public enum DIRECTION {
		DOWN, UP, LEFT, RIGHT;
	}

	public WebElement findElementIgnoreError(By by) {
		WebElement element = null;
		try {
			element = driver.findElement(by);
			return element;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * This method is used to close a webdriver
	 *
	 *
	 * @return None
	 * @throws Exception
	 */
	public void quitDriver() throws Exception {
		if (driver != null) {
			driver.quit();
			Log.info(">>Exiting Appium session ID: "+ driver.hashCode());
		}
	}


	/**
	 * This method is used to send keys into a text box.
	 * 
	 * @param element
	 *            The web element object of text box
	 * @param text
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if input text not success
	 */
	public void inputText(WebElement element, String text) throws Exception {
		try {
			element.sendKeys(text);
			hideKeyboard();
			HtmlReporter.pass(String.format("Input text [%s] to element [%s]", text, element.toString()));
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Can't input text [%s] to element [%s]", text, element.toString()));
			throw e;
		}
	}

	public void hideKeyboard() {
		try {
			if (isAndroidDriver()) {
				driver.hideKeyboard();
			}
		} catch (WebDriverException e) {
		}
	}

	public String getText(WebElement element) throws Exception {
		try {
			String text = element.getText();
			HtmlReporter.pass(String.format("The element [%s] contains text [%s]", element.toString(), text));
			return text;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Cannot get text of the element [%s]", element.toString()), e, "");
			throw e;
		}
	}

	public String getTextSelected(WebElement element) throws Exception {
		try {
			element.click();
			Thread.sleep(1000);
			WebElement wheels = (WebElement) driver
					.findElement(MobileBy.iOSClassChain("**/XCUIElementTypePickerWheel"));
			String text = wheels.getText();
			HtmlReporter.pass(String.format("The element [%s] contains text [%s]", element.toString(), text));
			clickByPosition(wheels, "top right");
			return text;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Cannot get text of the element [%s]", element.toString()), e, "");
			throw e;
		}
	}

	public String getAttribute(WebElement element, String attribute) {
		try {
			String value = element.getAttribute(attribute);
			HtmlReporter.pass(
					String.format("Element [%s] has attribute [%s] is [%s]", element.toString(), attribute, value));
			return value;
		} catch (NoSuchElementException e) {
			HtmlReporter.pass(String.format("Element [%s] has attribute [%s] is empty", element.toString(), attribute));
			return "";

		}

	}


	public void clickByPosition(WebElement element, String clickPosition) throws Exception {
		try {
			// waitForElementClickable(element, DEFAULT_WAITTIME_SECONDS);
			int leftX = element.getLocation().getX();
			int rightX = leftX + element.getSize().getWidth();
			int middleX = (rightX + leftX) / 2;
			int upperY = element.getLocation().getY();
			int lowerY = upperY + element.getSize().getHeight();
			int middleY = (upperY + lowerY) / 2;
			if (clickPosition.equalsIgnoreCase("left")) {
				new TouchAction<>(driver).tap(PointOption.point(leftX + 10, middleY)).perform();
			} else if (clickPosition.equalsIgnoreCase("right")) {
				new TouchAction<>(driver).tap(PointOption.point(rightX - 10, middleY)).perform();
			} else if (clickPosition.equalsIgnoreCase("top right")) {
				new TouchAction<>(driver).tap(PointOption.point(rightX - 10, upperY - 10)).perform();
			} else {
				new TouchAction<>(driver).tap(PointOption.point(middleX, middleY)).perform();
			}
			HtmlReporter.pass(String.format("click on the " + clickPosition + " of element [%s]", element.toString()));
		} catch (Exception e) {
			HtmlReporter.fail(
					String.format("Can't click on the " + clickPosition + " of element [%s]", element.toString()));
			throw (e);

		}
	}

	public void selectPickerWheel(WebElement element, String value) throws Exception {
		try {
			element.click();
			Thread.sleep(1000);
			MobileElement wheels = (MobileElement) driver
					.findElement(MobileBy.iOSClassChain("**/XCUIElementTypePickerWheel"));
			// Read the selected value
			String strPickerWheelSelectedValue = wheels.getText();
			if (strPickerWheelSelectedValue.equals(value)) {
				Log.info(String.format("The element [%s] is selected with value = [%s]", element.toString(), value));
				clickByPosition(wheels, "top right");
				return;
			} else {
				// get picker wheel location:
				int leftX = wheels.getLocation().getX();
				int rightX = leftX + wheels.getSize().getWidth();
				int middleX = (rightX + leftX) / 2;
				int upperY = wheels.getLocation().getY();
				int lowerY = upperY + wheels.getSize().getHeight();
				int middleY = (upperY + lowerY) / 2;

				// swipe down 3 time in picker wheel to get 1st item on list
				for (int i = 0; i < 3; i++) {
					new TouchAction<>(driver).press(PointOption.point(middleX, upperY + 50))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
							.moveTo(PointOption.point(middleX, lowerY)).release().perform();
					Thread.sleep(1000);
				}

				// set js script
				JavascriptExecutor js = (JavascriptExecutor) driver;
				Map<String, Object> params = new HashMap<>();
				params.put("order", "next");
				params.put("offset", 0.15);
				params.put("element", ((RemoteWebElement) wheels));

				js.executeScript("mobile: selectPickerWheelValue", params);

				// go to next item in the list of picker wheel
				for (int i = 0; i < 10; i++) {
					// check value
					strPickerWheelSelectedValue = wheels.getText();
					if (strPickerWheelSelectedValue.equals(value)) {
						Log.info(String.format("The element [%s] is selected with value = [%s]", element.toString(),
								value));
						clickByPosition(wheels, "top right");
						return;
					}
					js.executeScript("mobile: selectPickerWheelValue", params);
				}
				throw new Exception(
						String.format("The element [%s] cannot selected with value = [%s]", element.toString(), value));
			}
		} catch (Exception e) {
			Log.error(String.format("The element [%s] cannot selected with value = [%s]", element.toString(), value));
			throw (e);
		}

	}


	public void selectItemFromSpinner(WebElement spinner, String text) throws Exception {

		if (isAndroidDriver()) {
			click(spinner);
			driver.findElement(MobileBy.AndroidUIAutomator(
					"new UiScrollable(new UiSelector()).scrollIntoView(" + "new UiSelector().text(\"" + text + "\"));"))
					.click();
		} else {
			// scroll to object
			JavascriptExecutor js = (JavascriptExecutor) driver;
			HashMap<String, String> scrollObject = new HashMap<>();
			scrollObject.put("predicateString", "value == '" + text + "'");
			js.executeScript("mobile: scroll", scrollObject);
			// tap object
			((IOSDriver) driver).findElementByIosNsPredicate("value = '" + text + "'").click();
		}
	}



	public void swipe(DIRECTION direction) {

		switch (direction) {
		case RIGHT:
			swipe(0.2, 0.5, 0.8, 0.5);
			Log.info("Swipe RIGHT sucessfully");
			break;
		case LEFT:
			swipe(0.8, 0.5, 0.2, 0.5);
			Log.info("Swipe LEFT sucessfully");
			break;
		case UP:
			if(isIOSDriver()) {
				if(((IOSDriver) driver).isKeyboardShown()) {
					swipe(0.5, 0.5, 0.5, 0.1);
				}
			}else {
				swipe(0.5, 0.8, 0.5, 0.2);
			}
			Log.info("Swipe UP sucessfully");
			break;
		case DOWN:
			if(isIOSDriver()) {
				if(((IOSDriver) driver).isKeyboardShown()) {
					swipe(0.5, 0.1, 0.5, 0.5);
				}
			}else {
				swipe(0.5, 0.2, 0.5, 0.8);
			}
			Log.info("Swipe DOWN sucessfully");
			break;
		default:
			break;
		}
	}

	/**
	 * Swipe the android mobile by location in screen
	 * 
	 * @param fromx
	 *            % vertical of screen for starting point
	 * 
	 * @param fromy
	 *            % horizontal of screen for starting point
	 * 
	 * @param tox
	 *            % vertical of screen for ending point
	 * 
	 * @param toy
	 *            % horizontal of screen for ending point
	 * 
	 * @throws Exception
	 */
	public void swipe(double fromx, double fromy, double tox, double toy) {
		wait(1);
		// Get the size of screen.
		Dimension size = driver.manage().window().getSize();
		int startx = (int) (size.width * fromx);
		int endx = (int) (size.width * tox);
		int starty = (int) (size.height * fromy);
		int endy = (int) (size.height * toy);
		new TouchAction<>(driver).press(PointOption.point(startx, starty))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))).moveTo(PointOption.point(endx, endy))
				.release().perform();
	}

	/**
	 * Wait for seconds
	 * 
	 * @param seconds
	 */
	public void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException ex) {

		}
	}

	/**
	 * This method is used to re-launch application
	 * 
	 *
	 * @return None
	 * @throws Exception
	 */
	public void relaunchApp() throws Exception {
		String appBundleId = "";
		if (isAndroidDriver()) {
			appBundleId = PropertiesLoader.getPropertiesLoader().apppium_android_configuration
					.getProperty("appium.android.appPackage");
		} else if (isIOSDriver()) {
			appBundleId = PropertiesLoader.getPropertiesLoader().appium_ios_configuration
					.getProperty("appium.ios.app.bundleId");
		}
		try {
			driver.terminateApp(appBundleId);
			Thread.sleep(5000);
			driver.activateApp(appBundleId);
			Thread.sleep(5000);
			HtmlReporter.pass("Relaunch app [" + appBundleId + "] sucessfully");
		} catch (Exception e) {
			HtmlReporter.fail("Relaunch app [" + appBundleId + "] failed", e, "");
			throw (e);
		}
	}

	/**
	 * This method is used to reset application state before new test case run
	 * 
	 *
	 *  @return None
	 * @throws Exception
	 * @throws Exception
	 */
	public void resetApp() throws Exception {
		try {
			driver.closeApp();
			Thread.sleep(5000);
			driver.launchApp();
			Thread.sleep(3000);
			HtmlReporter.pass("Reset app successfully");
		} catch (Exception e) {
			HtmlReporter.fail("Cannot reset app!", e, "");
			throw e;
		}
	}

	public void setExplicitWaitToDefault() {
		setExplicitWait(EXPLICIT_WAIT_TIMEOUT);
	}

}
