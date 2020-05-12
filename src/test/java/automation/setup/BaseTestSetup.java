package automation.setup;

import automation.excelhelper.ExcelHelper;
import automation.report.CaptureArtifact;
import automation.report.HtmlReporter;
import automation.utility.BrowserStackCapabilities;
import automation.utility.Common;
import automation.utility.FilePaths;
import automation.utility.StringUtilities;
import com.aventstack.extentreports.AnalysisStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

public class BaseTestSetup {

	public Object[][] getTestProvider(String filepPath, String sheetName) throws Exception {
		// return the data from excel file
		Object[][] data = ExcelHelper.getTableArray(filepPath, sheetName);
		return data;
	}

	public void beforeSuite(ITestContext ctx, AnalysisStrategy analysisStrategy) throws Exception {
		String timeStampedSuiteName = ctx.getSuite().getName() + " :: "
				+ StringUtilities.getFormattedDate(ctx.getStartDate().getTime(), "yyyy-MM-dd HH:mm:ss z");
		ctx.getSuite().getXmlSuite().setName(timeStampedSuiteName);
		/*********** Init Html reporter *************************/
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath(), analysisStrategy, ctx);
	}

	public void beforeClass(ITestContext ctx) throws Exception {
		Common.currentTest = this.getClass().getSimpleName();
		HtmlReporter.createTest(ctx.getName()+" :: "+this.getClass().getSimpleName(), "");
	}

	public void beforeMethod(RemoteWebDriver driver, String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		try {
			HtmlReporter.createNode(this.getClass().getSimpleName(),
					method.getName()+ " :: " + driver.getSessionId(), "");
			HtmlReporter.info(">>Created driver with capabilities: " + driver.getCapabilities());
			HtmlReporter.info(">>STARTING TEST: " + ctx.getName()+"::"+this.getClass().getSimpleName()+":"
					+method.getName() + " session ID: "+ driver.getSessionId());
		} catch (Exception e) {
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName()+ " :: NULL Driver", "");
			HtmlReporter.info(">>FAIL: The current driver is null so cannot start test");
			throw new Exception(e);
		}
	}

	public void afterMethod(WebDriver driver, String sessionId, String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String message = "";
		XmlTest testInfo = ctx.getCurrentXmlTest();
		//Since extent Reports doesn't yet have a "was retried" status remove the test to avoid false failure reports
		if (result.wasRetried()){
			HtmlReporter.removeCurrentNode();
		}

		switch (result.getStatus()) {
			case ITestResult.SUCCESS:
				message = String.format(">>The test [%s] [%s]: PASSED for session: %s", testInfo.getName(), result.getName(), sessionId);
				HtmlReporter.pass(message);
				if(environment.startsWith("BS_")){
					BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
					bsCaps.markTests("passed",  "all good", sessionId, result);
				}
				break;
			case ITestResult.SKIP:
				message = String.format(">>The test [%s] [%s]: was SKIPPED: %s", testInfo.getName(),
						result.getName(), result.getThrowable());
				HtmlReporter.skip(message);
				break;

			case ITestResult.FAILURE:
				message = String.format(">>The test [%s] [%s]: FAILED for session: %s", testInfo.getName(),
						result.getName(), sessionId);
				HtmlReporter.fail(message, result.getThrowable(), CaptureArtifact.takeScreenshot(driver));;
				if(environment.startsWith("BS_")){
					BrowserStackCapabilities bsCaps = new BrowserStackCapabilities();
					bsCaps.markTests("failed",  result.getThrowable().toString(), sessionId, result );
				}
				break;
			default:
				//Mark Test as failed if it did not get a status for some unexpected reason
				message = String.format(">>The test [%s] [%s]: did not get executed or get a status",
						testInfo.getName(), result.getMethod().getQualifiedName(), "");
				if (!HtmlReporter.getNode().getStatus().name().equalsIgnoreCase("Fail")){
					HtmlReporter.fail(message);
				}
				break;
			}
		}

	public void afterClass() throws Exception {
	}

	public void afterSuite() throws Exception {
		HtmlReporter.flush();
	}

}
