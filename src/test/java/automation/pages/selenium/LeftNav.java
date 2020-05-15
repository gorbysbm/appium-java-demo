package automation.pages.selenium;

import automation.driver.SeleniumBaseDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


public class LeftNav extends SeleniumBaseDriver {

    public LeftNav(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);

    }

    //*********Locators*********
    private String lnkFlights = "tr.mouseout a[href*='mercuryreservation.php']";

    //*********Page Methods*********
    public void clickFlights(){
        click(By.cssSelector(lnkFlights));
    }

}