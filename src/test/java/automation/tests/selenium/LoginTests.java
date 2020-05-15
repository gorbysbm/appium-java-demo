package automation.tests.selenium;

import automation.driver.DriverCreator;
import automation.pages.selenium.FlightFinderPage;
import automation.pages.selenium.LoginPage;
import automation.setup.selenium.WebTestSetup;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class LoginTests extends WebTestSetup {

    //*********Tests*********

    @Test(enabled = true, dependsOnGroups ={"validFlightRegistration"} ,groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = (String) ctx.getAttribute("email");
        String password = (String) ctx.getAttribute("password");

        loginPage.openLoginPage();
        loginPage.loginUser(userName,password);
        //A successful login will redirect the user to the flight finder page
        flightFinderPage.verifyPageTitle();
    }

    @Test(enabled = true, groups = {"functional"},
            description = "User navigates to Sign in page, submits a wrong username and wrong password")
    public void testInvalidLogin() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        String userName= "InvalidUsername162763154655";
        String password= "Password12345";

        loginPage.openLoginPage();
        loginPage.loginUser(userName,password);
        loginPage.verifyNotLoggedIn();
    }

    //*********Helper Methods*********
}
