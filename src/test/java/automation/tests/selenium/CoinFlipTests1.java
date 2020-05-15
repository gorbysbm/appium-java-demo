package automation.tests.selenium;

import automation.driver.DriverCreator;
import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.pages.selenium.FlightFinderPage;
import automation.pages.selenium.LoginPage;
import automation.report.HtmlReporter;
import automation.report.Log;
import automation.setup.selenium.WebTestSetup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CoinFlipTests1 extends WebTestSetup {

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip1() throws Exception {
		WebDriver driver = DriverCreator.getCurrentWebDriver();
		FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		DriverCreator.getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) DriverCreator.getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		WebDriver driver = DriverCreator.getCurrentWebDriver();
		FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		DriverCreator.getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) DriverCreator.getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip3() throws Exception {
		WebDriver driver = DriverCreator.getCurrentWebDriver();
		FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		DriverCreator.getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) DriverCreator.getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip4() throws Exception {
		WebDriver driver = DriverCreator.getCurrentWebDriver();
		FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		DriverCreator.getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) DriverCreator.getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip5() throws Exception {
		WebDriver driver = DriverCreator.getCurrentWebDriver();
		FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		DriverCreator.getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) DriverCreator.getCurrentWebDriver()).getSessionId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip6() throws Exception {
		WebDriver driver = DriverCreator.getCurrentWebDriver();
		FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		DriverCreator.getCurrentWebDriver().get("http://www.google.com");
		HtmlReporter.info("I'm thread: "+ ((RemoteWebDriver) DriverCreator.getCurrentWebDriver()).getSessionId());
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
