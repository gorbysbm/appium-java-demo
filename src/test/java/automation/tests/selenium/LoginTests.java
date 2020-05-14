package automation.tests.selenium;

import automation.driver.DriverCreator;
import automation.pages.selenium.LoginPage;
import automation.report.HtmlReporter;
import automation.setup.selenium.WebTestSetup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class LoginTests extends WebTestSetup {

    //*********Tests*********

//    @Test(dependsOnGroups ={"validFlightRegistration"} ,groups = {"functional", "validLogin"},
//            description = "User navigates to Sign in page, submits a previously created username and password")
//    @Test(groups = {"functional", "validLogin"},
//            description = "User navigates to Sign in page, submits a previously created username and password")
//
//    public void testValidLogin() throws Exception {
//        WebDriver driver = DriverCreator.getCurrentWebDriver();
//        LoginPage loginPage = new LoginPage(driver);
//
//        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
//        String userName = "testuser";
//        String password = "test1234";
//
//        loginPage.openLoginPage();
//        loginPage.loginUser(userName,password);
//        //A successful login will redirect the user to the flight finder page
//
//        //flightFinderPage.verifyPageTitle();
//    }

//    @Test(groups = {"functional"},
//            description = "User navigates to Sign in page, submits a wrong username and wrong password")
//    public void testInvalidLogin() {
//        WebDriver driver = LocalDriverManager.getDriver();
//        HomePage homePage = new HomePage(driver);
//        LoginPage loginPage = new LoginPage(driver);
//        TopNav topNav = new TopNav(driver);
//        String userName= "InvalidUsername162763154655";
//        String password= "Password12345";
//
//        homePage.goToHomepage();
//        topNav.clickSignInUser();
//        loginPage.loginUser(userName,password);
//        loginPage.verifyNotLoggedIn();
//    }

    //*********Helper Methods*********
//}

    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin1() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());
        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        Assert.assertTrue(1==2);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }


    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin2() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());

        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }

    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin3() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());

        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }

    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin4() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());

        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }

    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin5() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());

        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }

    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin6() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());

        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }

    @Test(groups = {"functional", "validLogin"},
            description = "User navigates to Sign in page, submits a previously created username and password")
    public void testValidLogin7() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        LoginPage loginPage = new LoginPage(driver);

        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        String userName = "testuser";
        String password = "test1234";
        HtmlReporter.info(">>SessionId:" + ((RemoteWebDriver)driver).getSessionId());

        loginPage.openLoginPage();
        loginPage.loginUser(userName, password);
        //A successful login will redirect the user to the flight finder page

        //flightFinderPage.verifyPageTitle();
    }

}