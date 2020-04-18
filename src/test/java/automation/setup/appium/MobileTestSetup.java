package automation.setup.appium;

import java.lang.reflect.Method;
import java.util.HashMap;

import automation.report.CaptureArtifact;
import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import automation.appium.driver.AppiumBaseDriver;
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
	public void beforeClass() throws Exception {
		HtmlReporter.createTest(this.getClass().getSimpleName(), "");
		Common.currentTest = this.getClass().getSimpleName();
	}

	@BeforeMethod(alwaysRun=true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void beforeMethod(String configFile, String environment, Method method) throws Exception {
		AppiumDriver driver = null;

		HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
		driver = new AppiumHandler().startDriver(configFile, environment, method);
		AppiumDriverManager.setDriver(driver);
		HtmlReporter.info(">>Starting Appium session ID: "+ AppiumDriverManager.getDriver().getSessionId().toString()
				+ " Test Name: " +method.getName());
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Exception {
		String mess = "";
		try {
			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					mess = String.format("The test [%s] is PASSED", result.getName());
					HtmlReporter.pass(mess);
					break;
				case ITestResult.SKIP:
					mess = String.format("The test [%s] is PASSED", result.getName());
					HtmlReporter.pass(mess);
					break;

				case ITestResult.FAILURE:
					mess = String.format("The test [%s] is FAILED", result.getName());
					HtmlReporter.fail(mess, result.getThrowable(), CaptureArtifact.takeScreenshot(AppiumDriverManager.getDriver()));;
					break;
				default:
					break;
			}
		} catch (Exception e) {
		}


		finally {
			if (AppiumDriverManager.getDriver() != null){
				Log.info(">>Ending Appium session ID: "+ AppiumDriverManager.getDriver().getSessionId().toString()
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
