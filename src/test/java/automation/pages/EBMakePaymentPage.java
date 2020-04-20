package automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import automation.appium.driver.AppiumBaseDriver;
import automation.appium.driver.CreateDriver;

import java.text.DecimalFormat;

public class EBMakePaymentPage extends AppiumBaseDriver{

	private AppiumDriver driver =  CreateDriver.getInstance().getCurrentDriver();

	@AndroidFindBy(id = "phoneTextField")
	@iOSXCUITFindBy(id = "phoneTextField")
	private WebElement phoneField;

	@AndroidFindBy(id = "nameTextField")
	@iOSXCUITFindBy(id = "nameTextField")
	private WebElement nameField;

	@AndroidFindBy(id = "amountTextField")
	@iOSXCUITFindBy(id = "amountTextField")
	private WebElement amountField;

	@AndroidFindBy(id = "countryButton")
	@iOSXCUITFindBy(id = "countryButton")
	private WebElement countrySelectButton;


	//@iOSXCUITFindBy( = "Enter check amount")
	private WebElement countryName;

	@AndroidFindBy(id = "sendPaymentButton")
	@iOSXCUITFindBy(id = "sendPaymentButton")
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

	public void selectCountry(String countryName){
		click(countrySelectButton);
		WebElement we = scrollIntoView(countryName, "name");
		click(we);
	}

	public void clickSendPayment(){
		click(sendPayment);
	}

	public void proceedWithPayment() {
		WebElement el = findElementByText("Yes","name");
		click(el);
	}

 	public void verifyNewBalance(String amountSent){
		String newBalance =new DecimalFormat("#.00").format(100 - Double.parseDouble(amountSent))+"$";
		String balancePhrase = "Your balance is: "+newBalance;

		if (isAndroidDriver()) {
			findElementByText(balancePhrase, "");
		} else {
			findElementByText(newBalance, "name");
		}

	}
}
