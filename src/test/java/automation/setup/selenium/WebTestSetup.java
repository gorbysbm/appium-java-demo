package automation.setup.selenium;

import automation.driver.DriverCreator;
import automation.report.HtmlReporter;
import automation.setup.BaseTestSetup;
import com.aventstack.extentreports.AnalysisStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class WebTestSetup extends BaseTestSetup {

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
		startDriver(configFile, environment, method, ctx);
		super.beforeMethod((RemoteWebDriver) getDriver(), configFile, environment, method, ctx);
	}

	@AfterMethod(alwaysRun = true)
	@Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String sessionId = (getDriver() != null) ? ((RemoteWebDriver) getDriver()).getSessionId().toString() : "";
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

	public WebDriver getDriver() {
		return DriverCreator.getCurrentWebDriver();
	}
}
