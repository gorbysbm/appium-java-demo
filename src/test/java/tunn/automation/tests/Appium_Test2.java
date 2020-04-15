package tunn.automation.tests;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tunn.automation.pages.LoginPage;
import tunn.automation.pages.RegisterPage;
import tunn.automation.setup.appium.Constant;
import tunn.automation.setup.appium.MobileTestSetup;

import java.lang.reflect.Method;
import java.util.List;

import static tunn.automation.utility.Assertion.assertTrue;

public class Appium_Test2 extends MobileTestSetup {
	private LoginPage loginPage;
	private RegisterPage registerPage;

	@BeforeMethod
	public void setupPage(Method method) throws Exception {
		registerPage = new RegisterPage(driver);

	}

	@Test
	public void test() throws Exception {
		System.out.println("****"+driver.isAndroidDriver());
//		AndroidElement searchElement = (AndroidElement) new WebDriverWait(driver, 30).until(
//				ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Search Wikipedia")));
//		searchElement.click();
//		AndroidElement insertTextElement = (AndroidElement) new WebDriverWait(driver, 30).until(
//				ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia.alpha:id/search_src_text")));
//		insertTextElement.sendKeys("BrowserStack");
//		Thread.sleep(5000);
//
//		List<AndroidElement> allProductsName = driver.findElementsByClassName("android.widget.TextView");
//		Assert.assertTrue(allProductsName.size() > 0);
	}

}
