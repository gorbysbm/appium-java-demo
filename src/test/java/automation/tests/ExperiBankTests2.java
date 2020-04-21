package automation.tests;

import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.report.Log;
import automation.setup.appium.MobileTestSetup;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Random;

public class ExperiBankTests2 extends MobileTestSetup {
	EBLoginPage ebLoginPage;
	EBMainMenuPage ebMainMenuPage;
	EBMakePaymentPage ebMakePaymentPage;

	@BeforeMethod(alwaysRun = true)
	public void setupPage(Method method) throws Exception {
		ebLoginPage = new EBLoginPage();
		ebMainMenuPage = new EBMainMenuPage();
		ebMakePaymentPage = new EBMakePaymentPage();
	}

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip1() throws Exception {
		ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}

	@Test(invocationCount = 0, groups = {"functional"})
	public void EB2payBillTest1() throws Exception {
		String payment = generateRandomAmount();

		ebLoginPage.login("company","company");
		ebMainMenuPage.clickMakePayment();
		ebMakePaymentPage.enterPhone("1234567");
		ebMakePaymentPage.enterName("My Name");
		ebMakePaymentPage.enterAmount(payment);
		ebMakePaymentPage.selectCountry("Vietnam");
		ebMakePaymentPage.clickSendPayment();
		ebMakePaymentPage.proceedWithPayment();
		ebMakePaymentPage.verifyNewBalance(payment);
	}

	@Test(invocationCount = 0, groups = {"functional"})
	public void EB2payBillTest2() throws Exception {
		String payment = generateRandomAmount();

		ebLoginPage.login("company","company");
		ebMainMenuPage.clickMakePayment();
//		ebMakePaymentPage.enterPhone("1234567");
//		ebMakePaymentPage.enterName("My Name");
//		ebMakePaymentPage.enterAmount(payment);
//		ebMakePaymentPage.selectCountry("Germany");
		ebMakePaymentPage.clickSendPayment();
		ebMakePaymentPage.proceedWithPayment();
		ebMakePaymentPage.verifyNewBalance(payment);
	}

	@Test(invocationCount = 0, groups = {"functional"})
	public void EB2payBillTest3() throws Exception {
		String payment = generateRandomAmount();

		ebLoginPage.login("company","company");
		ebMainMenuPage.clickMakePayment();
		ebMakePaymentPage.enterPhone("1234567");
		ebMakePaymentPage.enterName("My Name");
		ebMakePaymentPage.enterAmount(payment);
		ebMakePaymentPage.selectCountry("Germany");
		ebMakePaymentPage.clickSendPayment();
		ebMakePaymentPage.proceedWithPayment();
		ebMakePaymentPage.verifyNewBalance(payment);
	}

	public String generateRandomAmount(){
		DecimalFormat df2 = new DecimalFormat("#.00");
		double min = 50.00;
		double max = 99.00;
		double rand = new Random().nextDouble();
		String result = df2.format(min + (rand * (max - min)));
		return result;
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
