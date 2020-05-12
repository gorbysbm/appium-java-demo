package automation.setup.appium;

import automation.driver.DriverHandler;
import automation.driver.CreateDriver;
import automation.report.HtmlReporter;
import automation.setup.BaseTestSetup;
import com.aventstack.extentreports.AnalysisStrategy;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestException;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class MobileTestSetup extends BaseTestSetup {
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Exception {
		super.beforeSuite(ctx, AnalysisStrategy.CLASS);
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext ctx) throws Exception {
		super.beforeClass(ctx);
	}

	@BeforeMethod(alwaysRun=true)
	@Parameters(value={"config", "environment"})
	public void beforeMethod(String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		AppiumDriver driver = null;
		startDriver(driver, configFile, environment, method, ctx);
		super.beforeMethod(getDriver(), configFile, environment, method, ctx);
	}

	@AfterMethod(alwaysRun = true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String sessionId = (getDriver() != null) ? getDriver().getSessionId().toString() : "";
		try {
			super.afterMethod(getDriver(), sessionId, configFile, environment, result, ctx);
		}

		finally {
			if (getDriver() != null){
				HtmlReporter.info(">>ENDING TEST: "+ctx.getCurrentXmlTest().getName()+"::" +result.getMethod().getQualifiedName());
				getDriver().quit();
			}
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		super.afterSuite();
	}
	private AppiumDriver startDriver(AppiumDriver driver, String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		//Try to start driver and fail the test if not successful
		try {
			driver = new DriverHandler().startDriver(driver, configFile, environment, method, ctx);
			CreateDriver.getInstance().setDriver(driver);
		} catch (Exception e) {
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
			HtmlReporter.fail(">>FAILED to create driver. Ending Test. Error was "+ e);
			throw new Exception(e.toString());
		}
		return getDriver();
	}
	public AppiumDriver getDriver() {
		return CreateDriver.getInstance().getCurrentMobileDriver();
	}
}
