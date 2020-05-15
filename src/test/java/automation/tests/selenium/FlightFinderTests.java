package automation.tests.selenium;

import automation.driver.DriverCreator;
import automation.pages.selenium.*;
import automation.setup.selenium.WebTestSetup;
import automation.util.CookieHelper;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class FlightFinderTests extends WebTestSetup {

    //*********Tests*********

    @DataProvider(name="getTestPurchaseFlight")
    public Object[][] getTestPurchaseFlightDataProvider() throws IOException {
        return getDataProvider("testPurchaseFlight");
    }
    @Test(dependsOnGroups ={"validLogin"} ,dataProvider = "getTestPurchaseFlight",
            groups = {"functional","E2EflightPurchase"},  description = "User logs in and selects a roundtrip flight," +
            " then purchases the flight")
    public void testPurchaseRoundTripFlight(String description, String serviceClass, String passengerCount, String departureCity,
                                   String arrivalCity, String departureFlight, String arrivalFlight, String creditCardNumber) throws Exception {
        ITestContext ctx = Reporter.getCurrentTestResult().getTestContext();
        WebDriver driver = DriverCreator.getCurrentWebDriver();

        FlightBookingPage flightBookingPage = new FlightBookingPage(driver);
        FlightBookingConfirmationPage flightBookConfirmPage = new FlightBookingConfirmationPage(driver);
        FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
        FlightSelectPage flightSelectPage = new FlightSelectPage(driver);
        LeftNav leftNav = new LeftNav(driver);
        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage regPage = new RegistrationPage(driver);
        TopNav topNav = new TopNav(driver);

        loginPage.openLoginPage();
        CookieHelper.applyPriorSessionCookies(ctx);
        leftNav.clickFlights();

        flightFinderPage.selectFlightDetailsAndSearch(departureCity, arrivalCity);
        flightSelectPage.selectDepartureAndArrivalFlights(departureFlight, arrivalFlight, flightSelectPage);
        flightBookingPage.bookFlight(driver, ctx, creditCardNumber);
        flightBookConfirmPage.verifySuccessfulBooking(serviceClass, passengerCount, departureCity, arrivalCity, departureFlight, arrivalFlight, ctx, flightBookConfirmPage);
    }

    @Test(groups = {"functional"},  description = "A guest user tries navigating to flight purchase page," +
            " and is redirected to the homepage")
    public void testGuestUserUnableToPurchaseFlight() throws Exception {
        WebDriver driver = DriverCreator.getCurrentWebDriver();
        FlightBookingPage flightBookingPage = new FlightBookingPage(driver);
        FlightBookingConfirmationPage flightBookConfirmPage = new FlightBookingConfirmationPage(driver);
        FlightFinderPage flightFinderPage = new FlightFinderPage(driver);
        FlightSelectPage flightSelectPage = new FlightSelectPage(driver);
        LeftNav leftNav = new LeftNav(driver);
        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage regPage = new RegistrationPage(driver);
        TopNav topNav = new TopNav(driver);

        loginPage.openLoginPage();
        leftNav.clickFlights();
        //verify Guest user is redirected back to homepage
        loginPage.verifyNotLoggedIn();
        loginPage.verifyPageTitle();
    }

}


