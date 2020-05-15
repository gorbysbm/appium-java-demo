package automation.pages.selenium;

import automation.driver.SeleniumBaseDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


public class TopNav extends SeleniumBaseDriver {

    public TopNav(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    //*********Locators*********
    private String lnkRegister = "td.mouseout a[href*='mercuryregister.php']";
    private String lnkSignIn = "td.mouseout a[href*='mercurysignon.php']";

    //*********Page Methods*********
    public void clickRegisterUser(){
        click(By.cssSelector(lnkRegister));
    }

    public void clickSignInUser(){
        click(By.cssSelector(lnkSignIn));
    }

}