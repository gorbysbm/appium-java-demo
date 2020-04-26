package automation.tests;

import automation.appium.driver.AppiumHandler;
import automation.appium.driver.CreateDriver;
import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.report.CaptureArtifact;
import automation.report.HtmlReporter;
import automation.report.Log;
import automation.setup.appium.MobileTestSetup;
import automation.utility.BrowserStackCapabilities;
import automation.utility.Common;
import automation.utility.FilePaths;
import com.aventstack.extentreports.AnalysisStrategy;
import org.openqa.selenium.SessionNotCreatedException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

//public class TestNGDebugTests1 extends MobileTestSetup {
//
//
public class TestNGDebugTests1 {

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Exception {
		Log.info("Before suite: "+ Thread.currentThread().getId());
		throw new SkipException("New Skip Exception");
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext ctx) throws Exception {
		Log.info("Before class: "+ Thread.currentThread().getId());

	}


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod( Method method, ITestContext ctx) throws Exception {
		Log.info("beforeMEthod: "+ coinFlip() + Thread.currentThread().getId());
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result, ITestContext ctx) throws Exception {
		String message = "";
		XmlTest testInfo = null;
		try {
			testInfo = ctx.getCurrentXmlTest();

			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					Log.info("After method success: "+ Thread.currentThread().getId());
					break;
				case ITestResult.SKIP:
					Log.info("After method skip: "+ Thread.currentThread().getId());

					break;

				case ITestResult.FAILURE:
					Log.info("After method fail: "+ Thread.currentThread().getId());
					break;
				default:
					Log.info("After method no status: "+ Thread.currentThread().getId());
					break;
			}
		}

		finally {
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
		Log.info("After Class: "+ Thread.currentThread().getId());
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		Log.info("After suite: "+ Thread.currentThread().getId());
	}


	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip1() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(true);
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
