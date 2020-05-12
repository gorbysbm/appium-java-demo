package automation.tests.appium;

import automation.report.Log;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import automation.pages.EBLoginPage;
import automation.pages.EBMainMenuPage;
import automation.pages.EBMakePaymentPage;
import automation.setup.appium.MobileTestSetup;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Random;

public class ExperiBankTests1 extends MobileTestSetup {

	@Test(invocationCount = 1, groups = {"functional"})
	public void EB1coinFlip1() throws Exception {
		EBLoginPage ebLoginPage = new EBLoginPage();
		EBMainMenuPage ebMainMenuPage = new EBMainMenuPage();
		EBMakePaymentPage ebMakePaymentPage = new EBMakePaymentPage();

		ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip2() throws Exception {
		EBLoginPage ebLoginPage = new EBLoginPage();
		EBMainMenuPage ebMainMenuPage = new EBMainMenuPage();
		EBMakePaymentPage ebMakePaymentPage = new EBMakePaymentPage();

		ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}
	@Test(invocationCount = 0, groups = {"functional"})
	public void EB1coinFlip3() throws Exception {
		EBLoginPage ebLoginPage = new EBLoginPage();
		EBMainMenuPage ebMainMenuPage = new EBMainMenuPage();
		EBMakePaymentPage ebMakePaymentPage = new EBMakePaymentPage();

		ebLoginPage.login("company","company");
		Assert.assertTrue(coinFlip().equalsIgnoreCase("heads"));
	}



	@Test(invocationCount = 1, groups = {"functional", "passingTest"})
	public void EB1payBillTest1() throws Exception {
		EBLoginPage ebLoginPage = new EBLoginPage();
		EBMainMenuPage ebMainMenuPage = new EBMainMenuPage();
		EBMakePaymentPage ebMakePaymentPage = new EBMakePaymentPage();
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
	public void EB1payBillTest2ShouldFail() throws Exception {
		EBLoginPage ebLoginPage = new EBLoginPage();
		EBMainMenuPage ebMainMenuPage = new EBMainMenuPage();
		EBMakePaymentPage ebMakePaymentPage = new EBMakePaymentPage();
		String payment = generateRandomAmount();

		ebLoginPage.login("company","company");
		ebMainMenuPage.clickMakePayment();
//		ebMakePaymentPage.enterPhone("1234567");
//		ebMakePaymentPage.enterName("My Name");
		ebMakePaymentPage.enterAmount(payment);
		ebMakePaymentPage.selectCountry("Germany");
		ebMakePaymentPage.clickSendPayment();
		ebMakePaymentPage.proceedWithPayment();
		ebMakePaymentPage.verifyNewBalance(payment);
	}
	@Test(invocationCount = 0, groups = {"functional", "passingTest"})
	public void EB1payBillTest3() throws Exception {
		EBLoginPage ebLoginPage = new EBLoginPage();
		EBMainMenuPage ebMainMenuPage = new EBMainMenuPage();
		EBMakePaymentPage ebMakePaymentPage = new EBMakePaymentPage();
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
