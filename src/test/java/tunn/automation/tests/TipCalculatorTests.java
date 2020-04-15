package tunn.automation.tests;

import tunn.automation.pages.LoginPage;
import tunn.automation.pages.RegisterPage;
import tunn.automation.pages.TipCalculatePage;
import tunn.automation.setup.appium.Constant;
import tunn.automation.setup.appium.MobileTestSetup;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static tunn.automation.utility.Assertion.*;

import java.lang.reflect.Method;

public class TipCalculatorTests extends MobileTestSetup {
	TipCalculatePage tipCalculatePage;

	@BeforeMethod
	public void setupPage(Method method) throws Exception {
		tipCalculatePage = new TipCalculatePage(driver);
	}

	@Test
	public void tipCalculate1() throws Exception {
		tipCalculatePage.enterTip("55");
	}

	@Test
	public void tipCalculate2() throws Exception {
		tipCalculatePage.enterTip("66");
	}

}
