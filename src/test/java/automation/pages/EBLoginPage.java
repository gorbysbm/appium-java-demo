package automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import automation.appium.driver.AppiumBaseDriver;
import automation.appium.driver.CreateDriver;

public class EBLoginPage extends AppiumBaseDriver{

	private AppiumDriver driver = CreateDriver.getInstance().getCurrentDriver();

	@AndroidFindBy(id = "usernameTextField")
	@iOSXCUITFindBy(id = "usernameTextField")
	private WebElement userNameField;

	@AndroidFindBy(id = "passwordTextField")
	@iOSXCUITFindBy(id = "passwordTextField")
	private WebElement passwordField;

	@AndroidFindBy(id = "loginButton")
	@iOSXCUITFindBy(id = "loginButton")
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
