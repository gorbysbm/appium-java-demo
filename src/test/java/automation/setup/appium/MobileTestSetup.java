package automation.setup.appium;

import java.lang.reflect.Method;
import java.util.HashMap;

import automation.report.CaptureArtifact;
import automation.report.Log;
import automation.utility.BrowserStackCapabilities;
import automation.utility.ConfigListener;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import automation.appium.driver.CreateDriver;
import automation.appium.driver.AppiumHandler;
import automation.excelhelper.ExcelHelper;
import automation.report.HtmlReporter;
import automation.utility.Common;
import automation.utility.FilePaths;
import org.testng.xml.XmlTest;

public class MobileTestSetup{

	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

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
		final AppiumDriver currentMobileDriver;
		try {
			AppiumDriver driver = new AppiumHandler().startDriver(configFile, environment, method, ctx);
			CreateDriver.getInstance().setDriver(driver);
		} catch (Exception e) {
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
			HtmlReporter.fail(">>FAILED to create Appium driver with capabilities: ");
			throw new SessionNotCreatedException(e.toString());
		}

		currentMobileDriver = CreateDriver.getInstance().getCurrentMobileDriver();
		HtmlReporter.createNode(this.getClass().getSimpleName(),
				method.getName()+ " :: " + currentMobileDriver.getSessionId(), "");
		HtmlReporter.info(">>Created Appium driver with capabilities: " + currentMobileDriver.getCapabilities());
		HtmlReporter.info(">>STARTING TEST: " + ctx.getName()+"::"+this.getClass().getSimpleName()+":"
				+method.getName() + " session ID: "+ currentMobileDriver.getSessionId());
	}

	@AfterMethod(alwaysRun = true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String message = "";
		XmlTest testInfo = null;
		AppiumDriver currentMobileDriver = null;
		try {
			testInfo = ctx.getCurrentXmlTest();
			currentMobileDriver = CreateDriver.getInstance().getCurrentMobileDriver();

			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					message = String.format(">>The test [%s] [%s]: PASSED for session: %s", testInfo.getName(), result.getName(), currentMobileDriver.getSessionId().toString());
					HtmlReporter.pass(message);
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
						bsCaps.markTests("passed",  "all good", currentMobileDriver.getSessionId().toString(), result);
					}
					break;
				case ITestResult.SKIP:
					message = String.format(">>The test [%s] [%s]: SKIPPED for session: %s", testInfo.getName(), result.getName(), currentMobileDriver.getSessionId().toString(), result.getThrowable());
					Log.info(message);
					HtmlReporter.removeCurrentNode();
					break;

				case ITestResult.FAILURE:
					message = String.format(">>The test [%s] [%s]: FAILED for session: %s", testInfo.getName(), result.getName(), currentMobileDriver.getSessionId().toString());
					HtmlReporter.fail(message, result.getThrowable(), CaptureArtifact.takeScreenshot(currentMobileDriver));;
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
						bsCaps.markTests("failed",  result.getThrowable().toString(), currentMobileDriver.getSessionId().toString(), result );
					}
					break;
				default:
					message = String.format(">>The test [%s] [%s]: did not get executed or get a status due to: %s ",
							testInfo.getName(), result.getMethod().getQualifiedName(), "");
					if (!HtmlReporter.getNode().getStatus().name().equalsIgnoreCase("Fail")){
						HtmlReporter.fail(message);
					}
					break;
			}
		}

		finally {
			if (currentMobileDriver != null){
				HtmlReporter.info(">>Ending session ID: "+ currentMobileDriver.getSessionId().toString()
						+ " Test Name: " +result.getMethod().getQualifiedName());
				currentMobileDriver.quit();
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
		if (CreateDriver.getInstance().getCurrentMobileDriver() != null){
			CreateDriver.getInstance().getCurrentMobileDriver().quit();
		}
	}
}
