package automation.tests.selenium.examplesuite;

import automation.driver.DriverCreator;
import automation.pages.selenium.LoginPage;
import automation.pages.selenium.RegistrationPage;
import automation.pages.selenium.TopNav;
import automation.setup.selenium.WebTestSetup;
import automation.util.StringUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class SuiteTest01 extends WebTestSetup {
    @Test(enabled = true, groups = {"functional"},
            description = "User navigates to Sign in page, submits a wrong username and wrong password")
    public void testInvalidLogin1() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        String userName= DriverCreator.getCurrentSessionId();
        String password= "Password12345";

        loginPage.openLoginPage();
        loginPage.loginUser(userName,password);
        loginPage.verifyNotLoggedIn();
        Assert.assertTrue( userName == DriverCreator.getCurrentSessionId(), "Thread safety check failed");

    }
    @Test(enabled = false, groups = {"functional"},
            description = "User navigates to Sign in page, submits a wrong username and wrong password")
    public void testInvalidLogin2() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        String userName= DriverCreator.getCurrentSessionId();
        String password= "Password12345";

        loginPage.openLoginPage();
        loginPage.loginUser(userName,password);
        loginPage.verifyNotLoggedIn();
        Assert.assertTrue( userName == DriverCreator.getCurrentSessionId(), "Thread safety check failed");

    }
    @Test(enabled = false, groups = {"functional"},
            description = "User navigates to Sign in page, submits a wrong username and wrong password")
    public void testInvalidLogin3() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        String userName= DriverCreator.getCurrentSessionId();
        String password= "Password12345";

        loginPage.openLoginPage();
        loginPage.loginUser(userName,password);
        loginPage.verifyNotLoggedIn();
        Assert.assertTrue( userName == DriverCreator.getCurrentSessionId(), "Thread safety check failed");

    }
    @Test(enabled = false, groups = {"functional"},
            description = "User navigates to Sign in page, submits a wrong username and wrong password")
    public void testInvalidLogin4() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        String userName= DriverCreator.getCurrentSessionId();
        String password= "Password12345";

        loginPage.openLoginPage();
        loginPage.loginUser(userName,password);
        loginPage.verifyNotLoggedIn();
        Assert.assertTrue( userName == DriverCreator.getCurrentSessionId(), "Thread safety check failed");
    }

}
