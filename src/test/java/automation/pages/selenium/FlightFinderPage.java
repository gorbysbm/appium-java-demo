package automation.pages.selenium;

import automation.driver.SeleniumBaseDriver;
import automation.setup.selenium.WebTestSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class FlightFinderPage extends SeleniumBaseDriver {
    public FlightFinderPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    //*********Locators*********
    private String ddlArrival = "form[action='mercuryreservation2.php'] select[name='toPort']";
    private String ddlDeparture = "form[action='mercuryreservation2.php'] select[name='fromPort']";
    private String btnFindFlights = "input[name='findFlights']";

    //*********Page Methods*********
    public void selectDeparture(String departure) {
        selectFromDropdown(By.cssSelector(ddlDeparture), departure);
    }

    public void selectArrival(String arrival) {
        selectFromDropdown(By.cssSelector(ddlArrival), arrival);
    }

    public void clickFindFlights() {
        click(By.cssSelector(btnFindFlights));
    }

    public void selectFlightDetailsAndSearch(String departureCity, String arrivalCity) {
        selectDeparture(departureCity);
        selectArrival(arrivalCity);
        clickFindFlights();
    }

    //*********Verifications*********
    public void verifyPageTitle(){
        String expectedPageTitle = "Find a Flight: Mercury Tours:";
        Assert.assertEquals(getPageTitle() , expectedPageTitle);
    }
}