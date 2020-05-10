package automation.tests.selenium;

import automation.appium.driver.CreateDriver;
import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.report.Log;
import automation.setup.selenium.WebTestSetup;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CoinFlipTests1 extends WebTestSetup {
	EBLoginPage ebLoginPage;
	EBMainMenuPage ebMainMenuPage;
	EBMakePaymentPage ebMakePaymentPage;

//	@BeforeMethod(alwaysRun = true)
//	//Don't proceed with test if before method fails org.openqa.selenium.SessionNotCreatedException
//	public void setupPage(Method method) throws Exception {
////		ebLoginPage = new EBLoginPage();
////		ebMainMenuPage = new EBMainMenuPage();
////		ebMakePaymentPage = new EBMakePaymentPage();
//	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip1() throws Exception {
		//ebLoginPage.login("company","company");
		CreateDriver.getInstance().getCurrentWebDriver().get("http://www.google.com");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip3() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	private String coinFlip(){
		if (Math.random() < 0.5){
			Log.info("**Heads");
			return "Heads";
		}else{
			Log.info("**Tails");
			return "Tails";
		}
	}
}
