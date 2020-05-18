package automation.setup.appium;

import automation.driver.DriverCreator;
import automation.report.HtmlReporter;
import automation.setup.BaseTestSetup;
import com.aventstack.extentreports.AnalysisStrategy;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
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
		super.startDriver(configFile, environment, method, ctx);
		super.beforeMethod(configFile, environment, method, ctx);
	}

	@AfterMethod(alwaysRun = true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		try {
			super.afterMethod(getDriver(), configFile, environment, result, ctx);
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

	public AppiumDriver getDriver() {
		return DriverCreator.getCurrentMobileDriver();
	}

}
