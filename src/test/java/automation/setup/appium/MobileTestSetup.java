package automation.setup.appium;

import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.util.HashMap;

import automation.report.CaptureArtifact;
import automation.utility.BrowserStackCapabilities;
import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import automation.appium.driver.AppiumDriverManager;
import automation.appium.driver.AppiumHandler;
import automation.excelhelper.ExcelHelper;
import automation.report.HtmlReporter;
import automation.report.Log;
import automation.utility.Common;
import automation.utility.FilePaths;

public class MobileTestSetup{

	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

	// Web driver
	protected Local browserStackLocal;
	// hashmap contains device infor like: platform, deviceName, uuid, browser...... etc
	public HashMap<String, String> deviceInfo;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception {
		/*********** Init Html reporter *************************/
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath());
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext ctx) throws Exception {
		HtmlReporter.createTest(ctx.getName()+" :: "+this.getClass().getSimpleName(), "");
		Common.currentTest = this.getClass().getSimpleName();
	}

	@BeforeMethod(alwaysRun=true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void beforeMethod(String configFile, String environment, Method method) throws Exception {
		AppiumDriver driver = null;

		driver = new AppiumHandler().startDriver(configFile, environment, method);
		AppiumDriverManager.setDriver(driver);
		HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName()+" :: "
				+ AppiumDriverManager.getDriver().getSessionId().toString(), "");
		HtmlReporter.info(">>Starting Appium session ID: "+ AppiumDriverManager.getDriver().getSessionId().toString()
				+ " Test Name: " +method.getName());
	}

	@AfterMethod(alwaysRun = true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result) throws Exception {
		String message = "";
		try {
			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					message = String.format(">>The test [%s]: PASSED for session: [%s]", result.getName(), AppiumDriverManager.getDriver().getSessionId().toString());
					HtmlReporter.pass(message);
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps =new BrowserStackCapabilities();
						bsCaps.markTests("passed",  "all good");
					}
					break;
				case ITestResult.SKIP:
					message = String.format(">>The test [%s]: was SKIPPED because of [%s]", result.getName(), result.getThrowable());
					Log.info(message);
					//HtmlReporter.skip(message, result.getThrowable(), CaptureArtifact.takeScreenshot(AppiumDriverManager.getDriver()));;
					break;

				case ITestResult.FAILURE:
					message = String.format(">>The test [%s]: FAILED for session: [%s]", result.getName(), AppiumDriverManager.getDriver().getSessionId().toString());
					HtmlReporter.fail(message, result.getThrowable(), CaptureArtifact.takeScreenshot(AppiumDriverManager.getDriver()));;
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps =new BrowserStackCapabilities();
						bsCaps.markTests("failed",  result.getThrowable().toString());
					}
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (AppiumDriverManager.getDriver() != null){
				//				AppiumDriverManager.getDriver().getAllSessionDetails()
				HtmlReporter.info(">>Ending Appium session ID: "+ AppiumDriverManager.getDriver().getSessionId().toString()
						+ " Test Name: " +result.getName());
				AppiumDriverManager.getDriver().quit();
				//driver.resetApp();
			}
		}

	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		HtmlReporter.flush();
		if (AppiumDriverManager.getDriver() != null){
			AppiumDriverManager.getDriver().quit();
		}
	}
}
