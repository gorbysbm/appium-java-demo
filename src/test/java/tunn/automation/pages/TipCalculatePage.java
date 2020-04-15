package tunn.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import tunn.automation.appium.driver.AppiumBaseDriver;
import tunn.automation.report.HtmlReporter;

public class TipCalculatePage {

	private AppiumBaseDriver driver;

	@AndroidFindBy(id = "org.traeg.fastip:id/billAmtEditText")
	@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement enterTipAmount;


	public TipCalculatePage(AppiumBaseDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver.getDriver()), this);
	}

	public void enterTip(String tipAmount) throws Exception {
		driver.clearAndTypeText(enterTipAmount, tipAmount);
	}

}
