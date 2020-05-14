package automation.driver;

import automation.report.HtmlReporter;
import automation.report.Log;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseDriver {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected int EXPLICIT_WAIT_TIMEOUT ;

	public BaseDriver() {

	}

	public BaseDriver(WebDriver driver) {
		this.driver = driver;
	}


	public void click (WebElement element) {
		HtmlReporter.info(String.format(">>Clicking on element [%s]", element.toString()));
		try {
			waitForClickable(element);
			element.click();
		} catch (Exception e) {
			HtmlReporter.fail(String.format(">>Can't click on element [%s]", element.toString()));
			throw e;
		}
	}

	public void clearAndTypeText(WebElement element, String text) {
		HtmlReporter.info(String.format(">>Typing text [%s] to element [%s]", text, element.toString()));
		try {
			waitForVisibilityOfElement(element);
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			HtmlReporter.fail(String.format(">>Can't clear / type text of element [%s]", element.toString()));
			throw e;
		}
	}

	public Boolean waitForPresenceOfTextinElement(WebElement element, String text) {
		return getExplicitWait().until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForClickable(WebElement element) {
		getExplicitWait().until(ExpectedConditions.elementToBeClickable(element));
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

	public void selectDDLByVisibleText(WebElement element, String text) throws Exception {
		try {
			Select ddl = new Select(element);
			ddl.selectByVisibleText(text);
			Log.info(String.format("Select [%s] option from dropdown list [%s]", text, element.toString()));

		} catch (Exception e) {

			Log.error(String.format("Can't select [%s] option from dropdown list [%s]", text, element.toString()));
			throw e;

		}
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



	public void selectRadioButton(WebElement element) throws Exception {
		try {

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
			if (!element.isSelected()) {
				element.click();
			}
			Log.info(String.format("The element [%s] is selected", element.toString()));

		} catch (Exception e) {
			Log.error(String.format("The element [%s] is not selected", element.toString()));
			throw (e);
		}

	}

	/////////////////////////////OLDER HELPERS////////////////////////

	public enum DIRECTION {
		DOWN, UP, LEFT, RIGHT;
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

	public Alert waitForAlert() {
		return getExplicitWait().until(ExpectedConditions.alertIsPresent());
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
			Log.info(">>Exiting Driver session ID: "+ driver.hashCode());
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
			HtmlReporter.pass("Navigate to the url :" + url);
		} catch (Exception e) {
			Log.error("Can't navigate to the url : " + url);
			HtmlReporter.fail("Can't navigate to the url : " + url);
			throw (e);
		}
	}
	public void deselectCheckBox(WebElement element) throws Exception {

		try {

			if (element.isSelected()) {
				element.click();
			}
			Log.info(String.format("The element [%s] is de-selected", element.toString()));

		} catch (Exception e) {

			Log.error(String.format("The element [%s] is not de-selected", element.toString()));
			throw (e);

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
//	public void clearAndTypeText(WebElement element, String text) {
//
//			element.clear();
//			if (!text.equalsIgnoreCase("")) {
//				element.sendKeys(text);
//				hideKeyboard();
//			}
//			HtmlReporter.pass(String.format("Input text [%s] to element [%s]", text, element.toString()));
//	}




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
