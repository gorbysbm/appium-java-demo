package automation.setup.selenium;

import automation.appium.driver.DriverHandler;
import automation.appium.driver.CreateDriver;
import automation.report.HtmlReporter;
import automation.setup.BaseTestSetup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestException;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

public class WebTestSetup extends BaseTestSetup {

	private WebDriver driver;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Exception {
		super.beforeSuite(ctx);
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext ctx) throws Exception {
		super.beforeClass(ctx);
	}

	@BeforeMethod(alwaysRun=true)
	@Parameters(value={"config", "environment"})
	public void beforeMethod(String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		//Try to start driver and fail the test if not successful
		try {
 			driver = new DriverHandler().startDriver(driver, configFile, environment, method, ctx);
			CreateDriver.getInstance().setDriver(driver);
		} catch (Exception e) {
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
			HtmlReporter.fail(">>FAILED to create driver. Ending Test. Error was "+ e);
			throw new TestException(e.toString());
		}
		super.beforeMethod((RemoteWebDriver) driver, configFile, environment, method, ctx);
	}

	@AfterMethod(alwaysRun = true)
	@Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
		try {
			super.afterMethod(driver, sessionId, configFile, environment, result, ctx);
		}

		finally {
			if (driver != null){
				HtmlReporter.info(">>ENDING TEST: "+ctx.getCurrentXmlTest().getName()+"::" +result.getMethod().getQualifiedName());
				driver.quit();
			}
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		super.afterSuite();
		if (driver != null){
			driver.quit();
		}
	}
}
