package automation.setup.appium;

import automation.appium.driver.AppiumHandler;
import automation.appium.driver.CreateDriver;
import automation.excelhelper.ExcelHelper;
import automation.report.CaptureArtifact;
import automation.report.HtmlReporter;
import automation.utility.BrowserStackCapabilities;
import automation.utility.Common;
import automation.utility.FilePaths;
import automation.utility.StringUtilities;
import com.aventstack.extentreports.AnalysisStrategy;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestException;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

public class MobileTestSetup{

	private AppiumDriver driver;

	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Exception {
		ctx.getSuite().getXmlSuite().setName(ctx.getSuite().getName()
				+" :: "+ StringUtilities.getFormattedDate(ctx.getStartDate().getTime(),"yyyy-MM-dd HH:mm:ss z"));
		/*********** Init Html reporter *************************/
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath(), AnalysisStrategy.CLASS, ctx);

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
 			driver = new AppiumHandler().startDriver(configFile, environment, method, ctx);
			CreateDriver.getInstance().setDriver(driver);
		} catch (Exception e) {
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
			HtmlReporter.fail(">>FAILED to create Appium driver. Ending Test. Error was "+ e);
			throw new TestException(e.toString());
		}

		HtmlReporter.createNode(this.getClass().getSimpleName(),
				method.getName()+ " :: " + driver.getSessionId(), "");
		HtmlReporter.info(">>Created Appium driver with capabilities: " + driver.getCapabilities());
		HtmlReporter.info(">>STARTING TEST: " + ctx.getName()+"::"+this.getClass().getSimpleName()+":"
				+method.getName() + " session ID: "+ driver.getSessionId());
	}

	@AfterMethod(alwaysRun = true)
	@org.testng.annotations.Parameters(value={"config", "environment"})
	public void afterMethod(String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String message = "";
		XmlTest testInfo = null;
		try {
			testInfo = ctx.getCurrentXmlTest();
			//Since extent Reports doesn't yet have a "was retried" status remove the test to avoid false failure reports
			if (result.wasRetried()){
				HtmlReporter.removeCurrentNode();
			}

			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					message = String.format(">>The test [%s] [%s]: PASSED for session: %s", testInfo.getName(), result.getName(), driver.getSessionId().toString());
					HtmlReporter.pass(message);
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
						bsCaps.markTests("passed",  "all good", driver.getSessionId().toString(), result);
					}
					break;
				case ITestResult.SKIP:
					message = String.format(">>The test [%s] [%s]: was SKIPPED: %s", testInfo.getName(),
							result.getName(), result.getThrowable());
					HtmlReporter.skip(message);
					break;

				case ITestResult.FAILURE:
					message = String.format(">>The test [%s] [%s]: FAILED for session: %s", testInfo.getName(),
							result.getName(), driver.getSessionId().toString());
					HtmlReporter.fail(message, result.getThrowable(), CaptureArtifact.takeScreenshot(driver));;
					if(environment.startsWith("BS_")){
						BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
						bsCaps.markTests("failed",  result.getThrowable().toString(), driver.getSessionId().toString(), result );
					}
					break;
				default:
					//Mark Test as failed if it did not get a status for some unexpected reason
					message = String.format(">>The test [%s] [%s]: did not get executed or get a status due to: %s ",
							testInfo.getName(), result.getMethod().getQualifiedName(), "");
					if (!HtmlReporter.getNode().getStatus().name().equalsIgnoreCase("Fail")){
						HtmlReporter.fail(message);
					}
					break;
			}
		}

		finally {
			if (driver != null){
				HtmlReporter.info(">>ENDING TEST: "+testInfo.getName()+"::" +result.getMethod().getQualifiedName());
				driver.quit();
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
		if (driver != null){
			driver.quit();
		}
	}
}
