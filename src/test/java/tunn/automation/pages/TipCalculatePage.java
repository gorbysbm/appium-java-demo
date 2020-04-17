package tunn.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import tunn.automation.appium.driver.AppiumBaseDriver;
import tunn.automation.appium.driver.AppiumDriverManager;

public class TipCalculatePage {

	private AppiumBaseDriver driver;

	@AndroidFindBy(id = "org.traeg.fastip:id/billAmtEditText")
	@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement billAmount;

	@AndroidFindBy(id = "org.traeg.fastip:id/tipAmtTextView")
	private WebElement calculatedTipAmount;

	@AndroidFindBy(id = "org.traeg.fastip:id/calcTipButton")
	private WebElement calculateTipButton;

	public TipCalculatePage() {
		this.driver = AppiumDriverManager.getDriver();
		PageFactory.initElements(new AppiumFieldDecorator(driver.getDriver()), this);
	}

	public void enterTip(String tipAmount) throws Exception {
		driver.clearAndTypeText(billAmount, tipAmount);
	}

	public void clickCalculateTipButton(){
		driver.click(calculateTipButton);
	}

	public void verifyCalculatedTipAmount(double tipAmount) throws Exception {
		Assert.assertEquals("$"+String.format(java.util.Locale.US,"%.2f"
				, (tipAmount*(15.0f/100.0f))),driver.getText(calculatedTipAmount));
	}

}
