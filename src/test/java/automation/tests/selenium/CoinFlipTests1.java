package automation.tests.selenium;

import automation.driver.CreateDriver;
import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.report.HtmlReporter;
import automation.report.Log;
import automation.setup.selenium.WebTestSetup;
import org.openqa.selenium.remote.RemoteWebDriver;
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
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) CreateDriver.getInstance().getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		//ebLoginPage.login("company","company");
		CreateDriver.getInstance().getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) CreateDriver.getInstance().getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip3() throws Exception {
		//ebLoginPage.login("company","company");
		CreateDriver.getInstance().getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) CreateDriver.getInstance().getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip4() throws Exception {
		//ebLoginPage.login("company","company");
		CreateDriver.getInstance().getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) CreateDriver.getInstance().getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip5() throws Exception {
		//ebLoginPage.login("company","company");
		CreateDriver.getInstance().getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) CreateDriver.getInstance().getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip6() throws Exception {
		//ebLoginPage.login("company","company");
		CreateDriver.getInstance().getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) CreateDriver.getInstance().getCurrentWebDriver()).getSessionId());
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
