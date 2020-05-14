package automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import automation.driver.AppiumBaseDriver;
import automation.driver.DriverCreator;

public class EBMainMenuPage extends AppiumBaseDriver{

	private AppiumDriver driver =  DriverCreator.getCurrentMobileDriver();

	@AndroidFindBy(id = "makePaymentButton")
	//@iOSXCUITFindBy(accessibility = "Enter check amount")
	private WebElement makePaymentButton;

	public EBMainMenuPage(AppiumDriver driver) {
		super(driver);
		setExplicitWaitToDefault();
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	public void clickMakePayment(){
		click(makePaymentButton);
	}

}
