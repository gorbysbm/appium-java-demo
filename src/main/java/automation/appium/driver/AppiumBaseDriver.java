package automation.appium.driver;

import java.time.Duration;
import java.util.*;

import com.browserstack.local.Local;
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
import org.testng.annotations.Optional;

public class AppiumBaseDriver {
	protected Local browserStackLocal;
	protected AppiumDriver driver = AppiumDriverManager.getDriver();
	private WebDriverWait wait;
	int EXPLICIT_WAIT_TIMEOUT = 6;

	public void click (WebElement element) {
		waitForClickable(element);
		element.click();
	}

	public Boolean waitForPresenceOfTextinElement(WebElement element, String text) {
		return getExplicitWait().until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForClickable(WebElement element) {
		getExplicitWait().until(ExpectedConditions.elementToBeClickable(element));
	}

	public void clearAndTypeText(WebElement element, String text) {
		waitForVisibilityOfElement(element);
		element.clear();
		element.sendKeys(text);
	}

	public WebElement scrollIntoView(String text, String predicate) {
		WebElement foundElement = null;

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
		return foundElement;
	}

	public WebElement findElementByText(String text, String predicate) {
		WebElement foundElement = null;

		if (isAndroidDriver()) {
			String locator = String.format("new UiSelector().text(\"%s\")", text);
			foundElement = waitForPresenceOfElement(MobileBy.AndroidUIAutomator(locator));
		}
		else if (isIOSDriver()) {
			String locator =  String.format("%s == '%s'",predicate, text);
			foundElement = waitForPresenceOfElement(MobileBy.iOSNsPredicateString(locator));
		}
		return foundElement;
	}


	//Wait for presence of elements before proceeding with action
	public List<WebElement> waitForPresenceOfAllElements(By elementBy) {
		return getExplicitWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementBy));
	}

	//Wait for presence of element before proceeding with action
	public WebElement waitForPresenceOfElement(By elementBy) {
		return getExplicitWait().until(ExpectedConditions.presenceOfElementLocated(elementBy));
	}


	//Wait for Visibility of element before proceeding with action
	public List<WebElement> waitForVisibilityOfAllElements(By elementBy) {
		return getExplicitWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementBy));
	}

	public WebElement waitForVisibilityOfElement(WebElement element) {
		return getExplicitWait().until(ExpectedConditions.visibilityOf(element));
	}

	public WebElement waitForVisibilityOfElement(By elementBy) {
		return getExplicitWait().until(ExpectedConditions.visibilityOfElementLocated(elementBy));
	}

	public void waitForInvincibility(By elementBy){
		getExplicitWait().until(ExpectedConditions.invisibilityOfElementLocated(elementBy));
	}

	//Wait for Clickability of element before proceeding with action
	public void waitForClickable(By elementBy) {
		getExplicitWait().until(ExpectedConditions.elementToBeClickable(elementBy));
	}



	public void waitForSelected(WebElement element){
		getExplicitWait().until(ExpectedConditions.elementToBeSelected(element));
	}

	public Boolean waitForTextUpdate(By elementBy, String expectedText){
		return getExplicitWait().until(ExpectedConditions.textToBe(elementBy, expectedText));
	}

	public void click (By elementBy) {
		waitForClickable(elementBy);
		driver.findElement(elementBy).click();
	}



	public void mouseOver (By elementBy) {
		Actions action = new Actions(driver);
		WebElement we = driver.findElement(elementBy);
		action.moveToElement(we).build().perform();
	}

	public WebElement getPresentElement(By elementBy){
		waitForPresenceOfAllElements(elementBy);
		return driver.findElement(elementBy);
	}


	public List<WebElement> getAllElementsPresent(By elementBy){
		waitForPresenceOfAllElements(elementBy);
		return driver.findElements(elementBy);
	}

	public boolean isElementPresent(By locatorKey) {
		try {
			waitForPresenceOfElement(locatorKey);
			return true;
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	public List<WebElement> getAllElementsVisible(By elementBy){
		waitForVisibilityOfAllElements(elementBy);
		return driver.findElements(elementBy);
	}

	public WebElement getVisibleElement(By elementBy) {
		return waitForVisibilityOfElement(elementBy);
	}

	//fix occasional: stale element reference: element is not attached to the page
	public void waitForStalenessOfElement(WebElement element) {
		try{
			getExplicitWait().until(ExpectedConditions.stalenessOf(element));
		}catch (TimeoutException e){

		}
	}

	public void waitForRefreshElement(WebElement element, By elementBy ){
		waitForStalenessOfElement(element);
		waitForPresenceOfAllElements(elementBy);
	}

	public void clickWithJavascript(By elementBy){
		waitForPresenceOfAllElements(elementBy);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(elementBy));
	}

	public void selectFromDropdown(By elementBy, String itemName){
		Select dropdown = new Select(driver.findElement(elementBy));
		dropdown.selectByValue(itemName);
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


	public WebElement findElement(WebElement element) {

		if (isElementDisplayed(element)) {
			return element;
		}
		int attemps = 0;
		swipe(DIRECTION.UP);
		do {
			if (isElementDisplayed(element)) {
				return element;
			}
			swipe(DIRECTION.DOWN);

			attemps++;
		} while (attemps < 2);

		throw new NoSuchElementException("Element not found");
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
	 * This method is used to navigate the browser to the url
	 * 
	 *
	 * @param url
	 *            the url of website
	 * @return None
	 * @throws Exception
	 *             The exception is thrown if the driver can't navigate to the
	 *             url
	 */
	public void openUrl(String url) throws Exception {
		try {
			driver.get(url);
			HtmlReporter.pass("\"Navigate to the url : \" + url");
		} catch (Exception e) {
			Log.error("Can't navigate to the url : " + url);
			HtmlReporter.fail("Can't navigate to the url : " + url);
			throw (e);
		}
	}

//	public void clearText(WebElement element) {
//		try {
//			waitForElementDisplayed(element, 30);
//			element.clear();
//			HtmlReporter.pass(String.format("Clear text of element [%s]", element.toString()));
//		} catch (Exception e) {
//			HtmlReporter.fail(String.format("Can't clear text of element [%s]", element.toString()));
//			throw e;
//		}
//	}

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
//	public void clearAndTypeText(WebElement element, String text) {
//			element = findElement(element);
//			element.clear();
//			if (!text.equalsIgnoreCase("")) {
//				element.sendKeys(text);
//				hideKeyboard();
//			}
//			HtmlReporter.pass(String.format("Input text [%s] to element [%s]", text, element.toString()));
//	}

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
			element = findElement(element);
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

	/**
	 * Execute javascript. This method used to execute a javascript
	 * 
	 *
	 * @param jsFunction
	 *            the js function
	 * @throws Exception
	 *             The exception is thrown if can't execute java script
	 */
	public void executeJavascript(String jsFunction) throws Exception {
		try {

			((JavascriptExecutor) driver).executeScript(jsFunction);
			Log.info("Excecuting the java script: " + jsFunction);
			HtmlReporter.pass("Excecuting the java script: " + jsFunction);
		} catch (Exception e) {
			Log.error("Can't excecute the java script: " + jsFunction);
			Log.error(e.getMessage());
			HtmlReporter.fail("Failed to excecuting the java script: " + jsFunction);
			throw (e);
		}
	}

	/**
	 * This method is used to execute a java script function for an object
	 * argument.
	 * 
	 *
	 * @param jsFunction
	 *            The java script function
	 * @param object
	 *            The argument to execute script
	 * @throws Exception
	 *             The exception is thrown if object is invalid.
	 */
	public void executeJavascript(String jsFunction, Object object) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript(jsFunction, object);
			Log.info("Excecuting the java script: " + jsFunction);
			HtmlReporter.pass("Excecuting the java script: " + jsFunction + "for object: " + object);
		} catch (Exception e) {
			Log.error("Can't excecute the java script: " + jsFunction + " for the object: " + object);
			Log.error(e.getMessage());
			HtmlReporter.fail("Can't excecute the java script: " + jsFunction + " for the object: " + object);
			throw (e);

		}
	}

	public String getText(WebElement element) throws Exception {
		try {
			String text = findElement(element).getText();
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
			String value = findElement(element).getAttribute(attribute);
			HtmlReporter.pass(
					String.format("Element [%s] has attribute [%s] is [%s]", element.toString(), attribute, value));
			return value;
		} catch (NoSuchElementException e) {
			HtmlReporter.pass(String.format("Element [%s] has attribute [%s] is empty", element.toString(), attribute));
			return "";

		}

	}
//	public boolean waitForElementDisplayed(WebElement element, int time) {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, time);
//			wait.until(ExpectedConditions.visibilityOf(element));
//		} catch (TimeoutException e) {
//			HtmlReporter.fail(String.format("Element [%s] is not displayed in expected time = %s", element, time));
//			return false;
//		}
//		return true;
//	}
//

//	public void click(WebElement element) throws Exception {
//		try {
//			element = findElement(element);
//			waitForElementClickable(element, DEFAULT_WAITTIME_SECONDS);
//			element.click();
//			HtmlReporter.pass(String.format("Click on the element [%s]", element.toString()));
//		} catch (Exception e) {
//			HtmlReporter.fail(String.format("Can't click on the element [%s]", element.toString()));
//			throw (e);
//
//		}
//	}

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

	public void selectRadioButton(WebElement element) throws Exception {
		try {
			element = findElement(element);
			if (!element.isSelected()) {
				element.click();
			}
			Log.info(String.format("The element [%s] is selected", element.toString()));
		} catch (Exception e) {
			Log.error(String.format("The element [%s] is not selected", element.toString()));
			throw (e);
		}

	}

	public void selectCheckBox(WebElement element) throws Exception {
		try {
			element = findElement(element);
			if (!element.isSelected()) {
				element.click();
			}
			Log.info(String.format("The element [%s] is selected", element.toString()));

		} catch (Exception e) {
			Log.error(String.format("The element [%s] is not selected", element.toString()));
			throw (e);
		}

	}

	public void deselectCheckBox(WebElement element) throws Exception {

		try {
			element = findElement(element);
			if (element.isSelected()) {
				element.click();
			}
			Log.info(String.format("The element [%s] is de-selected", element.toString()));

		} catch (Exception e) {

			Log.error(String.format("The element [%s] is not de-selected", element.toString()));
			throw (e);

		}

	}

	public void selectDDLByVisibleText(WebElement element, String text) throws Exception {
		try {
			element = findElement(element);
			Select ddl = new Select(element);
			ddl.selectByVisibleText(text);
			Log.info(String.format("Select [%s] option from dropdown list [%s]", text, element.toString()));

		} catch (Exception e) {

			Log.error(String.format("Can't select [%s] option from dropdown list [%s]", text, element.toString()));
			throw e;

		}
	}

	public void selectItemFromSpinner(WebElement spinner, String text) throws Exception {

		spinner = findElement(spinner);
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

	public void waitForElementClickable(WebElement element, int time) {

		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitUntilElementDisappear(WebElement element, int time) {
		FluentWait<WebDriver> wait = new WebDriverWait(driver, time).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.invisibilityOfAllElements(element));
	}

	public void waitForTextValueElementPresent(WebElement element, int time, String text) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForTextElementPresent(WebElement element, int time) {

		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until((driver) -> element.getText() != "");
	}

	public boolean isElementEnabled(WebElement element) {
		boolean result = element.isEnabled();
		if (result) {
			HtmlReporter.info(String.format("Element: [%s] is enabled", element.toString()));
		} else {
			HtmlReporter.info(String.format("Element: [%s] is not enabled", element.toString()));
		}
		return result;
	}

	public boolean isElementDisplayed(WebElement element) {
		boolean result;
		try {
			result = element.isDisplayed();
			if (result) {
				HtmlReporter.info(String.format("Element: [%s] is displayed", element.toString()));
			} else {
				HtmlReporter.info(String.format("Element: [%s] is not displayed", element.toString()));
			}
			return result;
		} catch (NoSuchElementException e) {
			HtmlReporter.info(String.format("Element: [%s] is not presented", element.toString()));
			return false;
		} catch(NullPointerException e) {
			HtmlReporter.info(String.format("Element: [%s] is not presented", element.toString()));
			return false;
		}

	}

	public boolean isElementSelected(WebElement element) throws Exception {
		boolean result = element.isSelected();
		if (result) {
			HtmlReporter.info(String.format("Element: [%s] is selected", element.toString()));
		} else {
			HtmlReporter.info(String.format("Element: [%s] is not selected", element.toString()));
		}
		return result;
	}

	public WebElement findElement(By element, int timeOut) {
		WebElement e = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			e = wait.until(ExpectedConditions.presenceOfElementLocated(element));
			HtmlReporter.info(String.format("Element: [%s] is presented", element.toString()));
			return e;
		} catch (TimeoutException ex) {
			HtmlReporter.info(String.format("Element: [%s] is not presented", element.toString()));
			return null;
		}
	}

	public Alert waitForAlert() {
		return getExplicitWait().until(ExpectedConditions.alertIsPresent());
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
	 * Verify that a text that available in screen
	 * 
	 * @param compareText
	 *            string that need to be verify
	 * 
	 * @return All string that available in screen
	 * 
	 * @throws Exception
	 */
	/*
	 * public void verifyToastMessage(String compareText) throws Exception { try
	 * { String imageClientCode = "ClientCodeEmptyImage";
	 * this.takeScreenshot(imageClientCode); String TessMessage =
	 * readToastMessage(imageClientCode);
	 * Assert.assertTrue(TessMessage.contains(compareText)); Log.info(
	 * "String \"" + compareText + "\" is available in screen");
	 * 
	 * } catch (Exception e) { Log.error("String \"" + compareText +
	 * "\" is not available in screen"); throw (e); } }
	 */


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

	//Explicit wait. Used to wait a specific time for slow elements in DOM to load
	public void setExplicitWait(int explicitWait) {
		wait = new WebDriverWait(driver, explicitWait);
	}
	public WebDriverWait getExplicitWait() {
		return wait;
	}

	public void setExplicitWaitToDefault() {
		setExplicitWait(EXPLICIT_WAIT_TIMEOUT);
	}

	protected DesiredCapabilities getDesiredCapabilities(String environment, JSONObject config) {
		JSONObject envs = (JSONObject) config.get("environments");
		DesiredCapabilities capabilities = new DesiredCapabilities();

		Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
		Iterator it = envCapabilities.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
		}

		Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
		it = commonCapabilities.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (capabilities.getCapability(pair.getKey().toString()) == null) {
				capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
			}
		}
		return capabilities;
	}
}
