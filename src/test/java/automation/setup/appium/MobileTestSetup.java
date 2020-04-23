package automation.setup.appium;

import java.lang.reflect.Method;
import java.util.HashMap;

import automation.report.CaptureArtifact;
import automation.report.Log;
import automation.utility.BrowserStackCapabilities;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.SessionNotCreatedException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import automation.appium.driver.CreateDriver;
import automation.appium.driver.AppiumHandler;
import automation.excelhelper.ExcelHelper;
import automation.report.HtmlReporter;
import automation.utility.Common;
import automation.utility.FilePaths;
import org.testng.xml.XmlTest;

public class MobileTestSetup{

	public Exception getExecutionException() {
		return executionException;
	}

	public void setExecutionException(Exception executionException) {
		this.executionException = executionException;
	}

	private Exception executionException;

	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

	// hashmap contains device infor like: platform, deviceName, uuid, browser...... etc
	public HashMap<String, String> deviceInfo;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Exception {
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
	public void beforeMethod(String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		try {
			AppiumDriver driver = new AppiumHandler().startDriver(configFile, environment, method, ctx);
			CreateDriver.getInstance().setDriver(driver);
		} catch (Exception e) {
			setExecutionException(e);
		}
		if(this.executionException != null){
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
			throw new SessionNotCreatedException(getExecutionException().toString());
		} else{
			HtmlReporter.createNode(this.getClass().getSimpleName(),
					method.getName()+ " :: " + CreateDriver.getInstance().getSessionId(), "");
			HtmlReporter.info(">>STARTING TEST: " + ctx.getName()+"::"+this.getClass().getSimpleName()+":"
					+method.getName() + " session ID: "+ CreateDriver.getInstance().getSessionId());
		}

	}

	@AfterMethod(alwaysRun = true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		XmlTest testInfo = ctx.getCurrentXmlTest();
		String message = "";
		try {
			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					message = String.format(">>The test [%s] [%s]: PASSED for session: %s", testInfo.getName(), result.getName(), CreateDriver.getInstance().getSessionId());
					HtmlReporter.pass(message);
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
						bsCaps.markTests("passed",  "all good", CreateDriver.getInstance().getSessionId(), result);
					}
					break;
				case ITestResult.SKIP:
					message = String.format(">>The test [%s] [%s]: SKIPPED for session: %s", testInfo.getName(), result.getName(), CreateDriver.getInstance().getSessionId(), result.getThrowable());
					Log.info(message);
					HtmlReporter.removeCurrentNode();
					break;

				case ITestResult.FAILURE:
					message = String.format(">>The test [%s] [%s]: FAILED for session: %s", testInfo.getName(), result.getName(), CreateDriver.getInstance().getSessionId());
					HtmlReporter.fail(message, result.getThrowable(), CaptureArtifact.takeScreenshot(CreateDriver.getInstance().getCurrentDriver()));;
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
						bsCaps.markTests("failed",  result.getThrowable().toString(), CreateDriver.getInstance().getSessionId(), result );
					}
					break;
				default:
					message = String.format(">>The test [%s] [%s]: did not get executed or get a status due to: %s ", testInfo.getName(), result.getName(), result.getThrowable());
					HtmlReporter.fail(message);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (CreateDriver.getInstance().getCurrentDriver() != null){
				HtmlReporter.info(">>Ending session ID: "+ CreateDriver.getInstance().getSessionId()
						+ " Test Name: " +result.getName());
				CreateDriver.getInstance().getCurrentDriver().quit();
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
		if (CreateDriver.getInstance().getCurrentDriver() != null){
			CreateDriver.getInstance().getCurrentDriver().quit();
		}
	}
}
