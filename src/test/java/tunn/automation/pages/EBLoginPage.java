package tunn.automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import tunn.automation.appium.driver.AppiumBaseDriver;
import tunn.automation.appium.driver.AppiumDriverManager;
import tunn.automation.report.Log;

public class EBLoginPage extends AppiumBaseDriver{

	private AppiumDriver driver = AppiumDriverManager.getDriver();

	@AndroidFindBy(id = "usernameTextField")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement userNameField;

	@AndroidFindBy(id = "passwordTextField")
	//@iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"$15.00\"]") //*[matches(name(), '^cup.*\d$')]
	private WebElement passwordField;

	@AndroidFindBy(id = "loginButton")
	//@iOSXCUITFindBy(accessibility = "Calculate Tip")
	private WebElement submitButton;

	public EBLoginPage() {
		setExplicitWaitToDefault();
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	public void login(String userName, String password){
		clearAndTypeText(userNameField , userName);
		clearAndTypeText(passwordField , password);
		click(submitButton);
	}

}
