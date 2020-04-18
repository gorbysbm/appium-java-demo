package automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import automation.appium.driver.AppiumBaseDriver;
import automation.appium.driver.AppiumDriverManager;

import java.text.DecimalFormat;

public class EBMakePaymentPage extends AppiumBaseDriver{

	private AppiumDriver driver = AppiumDriverManager.getDriver();

	@AndroidFindBy(id = "phoneTextField")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement phoneField;

	@AndroidFindBy(id = "nameTextField")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement nameField;

	@AndroidFindBy(id = "amountTextField")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement amountField;

	@AndroidFindBy(id = "countryButton")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement countrySelectButton;

	@AndroidFindBy(uiAutomator = "new UiScrollable(new UiSelector().className(\"android.widget.ListView\")).scrollIntoView("
			+ "new UiSelector().text(\"Brazil\"));")
			//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement countryName;

	@AndroidFindBy(id = "sendPaymentButton")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement sendPayment;


	//////////////////////////////////Page Objects////////////////////////////////////

	public EBMakePaymentPage() {
		setExplicitWaitToDefault();
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	public void enterName(String txt){
		clearAndTypeText(nameField, txt);
	}

	public void enterAmount(String txt){
		clearAndTypeText(amountField, txt);
	}

	public void enterPhone(String txt){
		clearAndTypeText(phoneField, txt);
	}

	public void selectCountry(String txt){
		click(countrySelectButton);
		click(countryName);
	}

	public void clickSendPayment(){
		click(sendPayment);
	}

	public void proceedWithPayment() {
		click(MobileBy.ByAndroidUIAutomator.AndroidUIAutomator("new UiSelector().text(\"Yes\")"));
	}

 	public void verifyNewBalance(String amountSent){
		String newBalance =new DecimalFormat("#.00").format(100 - Double.parseDouble(amountSent));
		if (isAndroidDriver()){
			waitForVisibilityOfElement(new MobileBy.ByAndroidUIAutomator("new UiSelector().textContains(\"Your balance is: "+newBalance+"$\")"));
		}
	}
}
