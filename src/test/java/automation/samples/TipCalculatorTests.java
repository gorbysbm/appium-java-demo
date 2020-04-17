package automation.samples;

import automation.pages.TipCalculatePage;
import automation.setup.appium.MobileTestSetup;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.lang.reflect.Method;

public class TipCalculatorTests extends MobileTestSetup {
	TipCalculatePage tipCalculatePage;

	@BeforeMethod
	public void setupPage(Method method) throws Exception {
		tipCalculatePage = new TipCalculatePage();
	}

	@Test
	public void tipCalculate1() throws Exception {
		double tipAmount = 100;
		tipCalculatePage.enterTip(String.valueOf(tipAmount));
		tipCalculatePage.clickCalculateTipButton();
		tipCalculatePage.verifyCalculatedTipAmount(tipAmount);
	}

//	@Test
//	public void tipCalculate2() throws Exception {
//		double tipAmount = 50;
//		tipCalculatePage.enterTip(String.valueOf(tipAmount));
//		tipCalculatePage.clickCalculateTipButton();
//		tipCalculatePage.verifyCalculatedTipAmount(tipAmount);
//	}
}
