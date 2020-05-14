package automation.pages.selenium;

import automation.driver.DriverCreator;
import automation.driver.SeleniumBaseDriver;
import automation.report.HtmlReporter;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

public class LoginPage extends SeleniumBaseDriver {

    //*********Locators*********
    @FindBy(css = "table input[name='userName']")
    private WebElement txtUsername;

    @FindBy(css = "table input[name='password']")
    private WebElement txtPassword;

    @FindBy(css = "td.mouseout a[href*='mercurysignon.php']")
    private WebElement lnkSignIn;

    @FindBy(css = "table input[name='login']")
    private WebElement btnLogin ;

    public LoginPage(WebDriver driver) {
        super(driver);
        setExplicitWaitToDefault();
        PageFactory.initElements(driver, this);
    }

    //*********Page Methods*********

    public void openLoginPage() throws Exception {
        openUrl(BASE_URL);
    }

    public void enterUsername(String username){
        clearAndTypeText(txtUsername ,username);
    }

    public void enterPassword(String password){
        clearAndTypeText(txtPassword ,password);
    }

    public void clickLogin(){
        click(btnLogin);
    }

    public void loginUser(String userName, String password) {
        HtmlReporter.info(String.format(">>> Attempting to login username: %s and passsword: %s",userName, password));
        enterUsername(userName);
        enterPassword(password);
        clickLogin();
    }

    public void verifyNotLoggedIn() {
        //Sign in link is still present
        waitForVisibilityOfElement(lnkSignIn);
        //username field is still present
        waitForVisibilityOfElement(txtUsername);
        //password field is still present
        waitForVisibilityOfElement(txtPassword);
    }

    //*********Verifications*********

}