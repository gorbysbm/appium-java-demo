package automation.setup;

import automation.driver.DriverCreator;
import automation.driver.DriverHandler;
import automation.report.CaptureArtifact;
import automation.report.HtmlReporter;
import automation.utility.BrowserStackCapabilities;
import automation.utility.Common;
import automation.utility.FilePaths;
import automation.util.StringUtilities;
import com.aventstack.extentreports.AnalysisStrategy;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
public class BaseTestSetup {

	public void beforeSuite(ITestContext ctx, AnalysisStrategy analysisStrategy) throws Exception {
		String timeStampedSuiteName = ctx.getSuite().getName() + " :: "
				+ StringUtilities.getFormattedDate(ctx.getStartDate().getTime(), "yyyy-MM-dd HH:mm:ss z");
		ctx.getSuite().getXmlSuite().setName(timeStampedSuiteName);
		/*********** Init Html reporter *************************/
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath(), analysisStrategy, ctx);
	}

	public void beforeClass(ITestContext ctx) throws Exception {
		HtmlReporter.createTest(ctx.getName()+" :: "+this.getClass().getSimpleName(), "");
	}

	public void beforeMethod(String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		String sessionId = DriverCreator.getCurrentSessionId();
		try {
			HtmlReporter.createNode(method.getDeclaringClass().getSimpleName(),
					method.getName()+ " :: " + sessionId, "");
			HtmlReporter.info(">>STARTING TEST: " + ctx.getName()+"::"+method.getDeclaringClass().getSimpleName()+"."
					+method.getName() + " session ID: "+ sessionId);
		} catch (Error | Exception e) {
			HtmlReporter.createNode(method.getDeclaringClass().getSimpleName(), method.getName()+ " :: NULL Driver", "");
			HtmlReporter.info(">>FAIL: The current driver is null so cannot start test");
			throw new Exception(e);
		}
	}

	public void afterMethod(WebDriver driver, String configFile, String environment, ITestResult result, ITestContext ctx) throws Exception {
		String message = "";
		String sessionId = DriverCreator.getCurrentSessionId();
		XmlTest testInfo = ctx.getCurrentXmlTest();
		//Since extent Reports doesn't yet have a "was retried" status remove the test to avoid false failure reports
		if (result.wasRetried()){
			HtmlReporter.removeCurrentNode();
		}

		switch (result.getStatus()) {
			case ITestResult.SUCCESS:
				message = String.format(">>The test [%s] [%s]: PASSED for session: %s", testInfo.getName(), result.getName(), sessionId);
				HtmlReporter.pass(message);
				if(environment.contains("BS_")){
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
				if(environment.contains("BS_")){
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

	public void startDriver(String configFile, String environment, Method method, ITestContext ctx) throws Exception {
		//Try to start driver and fail the test if not successful
		try {
			DriverHandler.startDriver(configFile, environment, method, ctx);
		} catch (Error | Exception e) {
			HtmlReporter.createNode(this.getClass().getSimpleName(), method.getName(), "");
			HtmlReporter.fail(">>FAILED to create driver. Ending Test. Error was "+ e);
			throw new Exception(e.toString());
		}
	}

	public Object[][] getDataProvider(String testName) throws IOException {
		String filePath = FilePaths.DATA_PROVIDERS_FILE_PATH + testName + ".csv";
		Reader reader = Files.newBufferedReader(Paths.get(filePath));

		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
		List<String[]> found = csvReader.readAll();
		Object[][] dataProviderObj = found.toArray(new Object[found.size()][]);
		return dataProviderObj;
	}

	protected void quitDriver(WebDriver driver, ITestResult result, ITestContext ctx) {
		if (driver != null){
			HtmlReporter.info(">>ENDING TEST: "+ctx.getCurrentXmlTest().getName()+"::" +result.getMethod().getQualifiedName());
			driver.quit();
		}
	}
}
