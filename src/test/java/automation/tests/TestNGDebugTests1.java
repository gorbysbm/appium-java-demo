package automation.tests;

import automation.report.CaptureArtifact;
import automation.report.HtmlReporter;
import automation.report.Log;
import automation.utility.BrowserStackCapabilities;
import automation.utility.Common;
import automation.utility.FilePaths;
import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

//public class TestNGDebugTests1 extends MobileTestSetup {

//@Listeners({ExtentITestListenerClassAdapter.class})
public class TestNGDebugTests1 {

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Exception {
		FilePaths.initReportFolder();
		HtmlReporter.setReporter(FilePaths.getReportFilePath(), AnalysisStrategy.CLASS);
		Log.info("Before suite: "+ Thread.currentThread().getId());
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext ctx) throws Exception {
		HtmlReporter.createTest(ctx.getName()+" :: "+this.getClass().getSimpleName(), "");
		Common.currentTest = this.getClass().getSimpleName();
		Log.info("Before class: "+ Thread.currentThread().getId());

	}


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod( Method method, ITestContext ctx) throws Exception {
		HtmlReporter.createNode(this.getClass().getSimpleName(),
				method.getName()+ " :: ", "");
		HtmlReporter.info(">>STARTING TEST: " + ctx.getName()+"::"+this.getClass().getSimpleName()+":"
				+method.getName() + " session ID: ");
		Log.info("beforeMEthod: "+ coinFlip() + Thread.currentThread().getId());
		//Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result, ITestContext ctx) throws Exception {
		String message = "";
		XmlTest testInfo = null;
		try {
			testInfo = ctx.getCurrentXmlTest();

			if (result.wasRetried()){
				HtmlReporter.removeCurrentNode();
			}

			switch (result.getStatus()) {
				case ITestResult.SUCCESS:
					message = String.format(">>The test [%s] [%s]: PASSED", testInfo.getName(), result.getName());
					HtmlReporter.pass(message);
					break;
				case ITestResult.SKIP:
					message = String.format(">>The test [%s] [%s]: was SKIPPED: %s", testInfo.getName(),
							result.getName(), result.getThrowable());
					HtmlReporter.skip(message);
					break;

				case ITestResult.FAILURE:
					message = String.format(">>The test [%s] [%s]: FAILED for session: %s", testInfo.getName(),
							result.getName(), "");
					HtmlReporter.fail(message);;
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
			HtmlReporter.info(">>ENDING TEST: "+testInfo.getName()+"::" +result.getMethod().getQualifiedName());
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
		Log.info("After Class: "+ Thread.currentThread().getId());
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		Log.info("After suite: "+ Thread.currentThread().getId());
	}


	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip1() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(false);
	}
	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip3() throws Exception {
		//ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	private String coinFlip(){
		if (Math.random() < 0.5){
			Log.info("**Heads");
			return "Heads";
		}else{
			Log.info("**Tails");
			return "Tails";
		}
	}


}
