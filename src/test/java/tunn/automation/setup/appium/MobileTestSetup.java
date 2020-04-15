package tunn.automation.setup.appium;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.browserstack.local.Local;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import tunn.automation.appium.driver.AppiumBaseDriver;
import tunn.automation.appium.driver.AppiumHandler;
import tunn.automation.appium.driver.AppiumDriverManager;
import tunn.automation.excelhelper.ExcelHelper;
import tunn.automation.report.HtmlReporter;
import tunn.automation.utility.Common;
import tunn.automation.utility.FilePaths;

public class MobileTestSetup {

	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

	// Web driver
	public static AppiumBaseDriver driver;
	protected Local browserStackLocal;
	// hashmap contains device infor like: platform, deviceName, uuid,
	// browser...... etc
	public HashMap<String, String> deviceInfo;

	@BeforeSuite
	public void beforeSuite() throws Exception {
		/*********** Init Html reporter *************************/
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath());

	}

	@BeforeClass
	public void beforeClass() throws Exception {
		HtmlReporter.createTest(this.getClass().getSimpleName(), "");
		Common.currentTest = this.getClass().getSimpleName();
	}

	@BeforeMethod(alwaysRun=true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void beforeMethod(String config_file, String environment, Method method) throws Exception {
		HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
		driver = new AppiumHandler().startDriver(config_file, environment);
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
					HtmlReporter.fail(mess, result.getThrowable(), driver.takeScreenshot());;
					break;
				default:
					break;
			}
		} catch (Exception e) {
		}
		finally {
			if (AppiumDriverManager.getDriver() != null){
				AppiumDriverManager.getDriver().quitDriver();
				//driver.resetApp();
			}
		}

	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		HtmlReporter.setSystemInfo("Platform", driver.getDriver().getCapabilities().getPlatform().toString());
		HtmlReporter.setSystemInfo("Platform Version", driver.getDriver().getCapabilities().getVersion());
		HtmlReporter.flush();
		if (AppiumDriverManager.getDriver() != null){
			AppiumDriverManager.getDriver().quitDriver();
		}
	}
}
