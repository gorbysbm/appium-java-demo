package tunn.automation.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tunn.automation.pages.EBLoginPage;
import tunn.automation.pages.EBMainMenuPage;
import tunn.automation.pages.EBMakePaymentPage;
import tunn.automation.pages.TipCalculatePage;
import tunn.automation.setup.appium.MobileTestSetup;

import java.lang.reflect.Method;

public class ExperiBankTests extends MobileTestSetup {
	EBLoginPage ebLoginPage;
	EBMainMenuPage ebMainMenuPage;
	EBMakePaymentPage ebMakePaymentPage;

	@BeforeMethod
	public void setupPage(Method method) throws Exception {
		ebLoginPage = new EBLoginPage();
		ebMainMenuPage = new EBMainMenuPage();
		ebMakePaymentPage = new EBMakePaymentPage();
	}

	@Test
	public void payBillTest1() throws Exception {
		double payment = 91.50;

		ebLoginPage.login("company","company");
		ebMainMenuPage.clickMakePayment();
		ebMakePaymentPage.enterPhone("1234567");
		ebMakePaymentPage.enterName("My Name");
		ebMakePaymentPage.enterAmount(String.valueOf(payment));
		ebMakePaymentPage.selectCountry("Brazil");
		ebMakePaymentPage.clickSendPayment();
		ebMakePaymentPage.proceedWithPayment();
		ebMakePaymentPage.verifyNewBalance(payment);
	}

}
