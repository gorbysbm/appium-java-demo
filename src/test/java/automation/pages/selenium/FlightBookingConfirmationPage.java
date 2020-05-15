package automation.pages.selenium;

import automation.driver.SeleniumBaseDriver;
import automation.util.StringUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestContext;

public class FlightBookingConfirmationPage extends SeleniumBaseDriver {

    public FlightBookingConfirmationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    //*********Locators*********
    private String txtItineraryBooked = "//font[contains(normalize-space(),'Your itinerary has been booked!')]";
    //TODO: we have to add proper element id's in app to avoid having to use such fragile Xpath locators
    private String txtDepartingFlightInfo = "//font[contains(text(),'Departing')]/../../../following-sibling::tr";
    private String txtArrivingFlightInfo = "//font[contains(text(),'Returning')]/../../../following-sibling::tr";
    private String txtBillingAddressInfo = "//b[contains(text(),'Billed To')]/../../../../following-sibling::tr";
    private String txtPassengerCount = "//font[contains(text(),'%s passenger')]";

    //*********Page Methods*********

    //*********Verifications*********
    public void verifyItineraryBookedSuccess(){
        getVisibleElement(By.xpath(txtItineraryBooked));
    }

    public void verifyDepartingFlightInfo(String serviceClass, String departureCity, String arrivalCity,
                                          String departureFlight){
        String[] departingFlightData = departureFlight.split("\\$");
        String actualDepartingFlightMessage = getVisibleElement(By.xpath((txtDepartingFlightInfo))).getText()
                .replace("\n"," ");
        String expectedDepartingFlightMessage= departureCity +" to " + arrivalCity +" "
                + StringUtilities.getFormattedDate("M/d/yyyy") +" @ "+departingFlightData[3] + " w/ "
                +departingFlightData[0] + " "+ departingFlightData[1] + " "+ serviceClass +" $"+ departingFlightData[2] +" each";

        Assert.assertEquals(actualDepartingFlightMessage, expectedDepartingFlightMessage);
    }

    public void verifyArrivingFlightInfo(String serviceClass, String departureCity, String arrivalCity,
                                          String arrivalFlight){
        String[] arrivingFlightData = arrivalFlight.split("\\$");
        String actualArrivingFlightMessage = getVisibleElement(By.xpath((txtArrivingFlightInfo))).getText()
                .replace("\n"," ");
        String expectedArrivingFlightMessage= arrivalCity +" to " + departureCity +" "
                + StringUtilities.getFormattedDate("M/d/yyyy") +" @ "+arrivingFlightData[3] + " w/ "
                +arrivingFlightData[0] + " "+ arrivingFlightData[1] + " "+ serviceClass +" $"+ arrivingFlightData[2] +" each";

        Assert.assertEquals(actualArrivingFlightMessage, expectedArrivingFlightMessage);
    }

    public void verifyBillingAddressInfo(ITestContext ctx){
        String actualBillingAddressInfo = getVisibleElement(By.xpath((txtBillingAddressInfo))).getText()
                .replace("\n"," ");
        String expectedBillingAddressInfo = ctx.getAttribute("streetAddress") + "  " + ctx.getAttribute("city")
                +", " + ctx.getAttribute("state")+ ", " + ctx.getAttribute("postCode");
        Assert.assertTrue(actualBillingAddressInfo.contains(expectedBillingAddressInfo));
    }

    public void verifyPassengerCount(String passengerCount) {
        waitForVisibilityOfElement(By.xpath(String.format(txtPassengerCount, passengerCount)));
    }

    public void verifySuccessfulBooking(String serviceClass, String passengerCount, String departureCity, String arrivalCity, String departureFlight, String arrivalFlight, ITestContext ctx, FlightBookingConfirmationPage flightBookConfirmPage) {
        verifyItineraryBookedSuccess();
        verifyDepartingFlightInfo(serviceClass, departureCity, arrivalCity, departureFlight);
        verifyArrivingFlightInfo(serviceClass, departureCity, arrivalCity, arrivalFlight);
        verifyPassengerCount(passengerCount);
        verifyBillingAddressInfo(ctx);
    }
}