package automation.setup.selenium;

import java.lang.reflect.Method;

import com.aventstack.extentreports.AnalysisStrategy;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import automation.report.HtmlReporter;
import automation.selenium.WebDriverMethod;
import automation.utility.FilePaths;

public class WebTestBaseSetup {

	// Web driver
	public WebDriverMethod driver;
	// browser
	public String browserName;
	// platform
	public String platform;

	@BeforeSuite
	public void beforeSuite(ITestContext ctx) throws Exception {
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath(), AnalysisStrategy.CLASS, ctx);
	}

	// @BeforeClass
	// public void beforeClass() throws Exception {
	//
	// driver = new WebDriverMethod(browserName,platform);
	// String description = platform + " - " + browserName;
	// HtmlReporter.createTest(this.getClass().getSimpleName() + " - " +
	// description,"On " + description);
	// }

	@BeforeClass
	public void beforeClass() throws Exception {
		//browserName = PropertiesLoader.getPropertiesLoader().selenium_configuration.getProperty("selenium.browser"); // chrome,firefox
		//platform = PropertiesLoader.getPropertiesLoader().selenium_configuration.getProperty("selenium.platform");

		String description = browserName + " - " + platform;
		HtmlReporter.createTest(this.getClass().getSimpleName() + " - " + description, "On " + description);
		
	}

	@BeforeMethod
	public void beforeMethod(Method method) throws Exception {
		driver = new WebDriverMethod(browserName, platform);
		HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(),
				// Common.getDataProviderString(data));
				"this is a test");
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws Exception {
		driver.closeDriver();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		HtmlReporter.flush();
	}
}
