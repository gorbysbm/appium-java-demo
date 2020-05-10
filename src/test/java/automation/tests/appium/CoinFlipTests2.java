package automation.tests.appium;

import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.report.Log;
import automation.setup.appium.MobileTestSetup;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class CoinFlipTests2 extends MobileTestSetup {
	EBLoginPage ebLoginPage;
	EBMainMenuPage ebMainMenuPage;
	EBMakePaymentPage ebMakePaymentPage;

	@BeforeMethod(alwaysRun = true)
	//Don't proceed with test if before method fails org.openqa.selenium.SessionNotCreatedException
	public void setupPage(Method method) throws Exception {
//		ebLoginPage = new EBLoginPage();
//		ebMainMenuPage = new EBMainMenuPage();
//		ebMakePaymentPage = new EBMakePaymentPage();
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB2coinFlip1() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 1, groups = {"functional"})
	public void EB2coinFlip2() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 0, groups = {"functional"})
	public void EB2coinFlip3() throws Exception {
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
