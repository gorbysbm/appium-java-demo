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

public class TipCalculatePage extends AppiumBaseDriver{

	private AppiumDriver driver = AppiumDriverManager.getDriver();

	@AndroidFindBy(id = "org.traeg.fastip:id/billAmtEditText")
	@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement billAmount;

	@AndroidFindBy(id = "org.traeg.fastip:id/tipAmtTextView")
	@iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"$15.00\"]") //*[matches(name(), '^cup.*\d$')]
	private WebElement calculatedTipAmount;

	@AndroidFindBy(id = "org.traeg.fastip:id/calcTipButton")
	@iOSXCUITFindBy(accessibility = "Calculate Tip")
	private WebElement calculateTipButton;

	public TipCalculatePage() {
		setExplicitWaitToDefault();
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	public void enterTip(String tipAmount) throws Exception {
		Log.info(">>test execution Appium session ID: "+ driver.hashCode());
		clearAndTypeText(billAmount, tipAmount);
	}

	public void clickCalculateTipButton(){
		click(calculateTipButton);
	}

	public void verifyCalculatedTipAmount(double tipAmount) throws Exception {
		String tip = "$"+String.format(java.util.Locale.US,"%.2f", (tipAmount*(15.0f/100.0f)));
		Assert.assertTrue(waitForPresenceOfTextinElement(calculatedTipAmount, tip),"Tip amount was not right");
		Assert.assertEquals("$"+String.format(java.util.Locale.US,"%.2f"
				, (tipAmount*(15.0f/100.0f))),getText(calculatedTipAmount));
	}

}
