package automation.pages.selenium;

import automation.driver.SeleniumBaseDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class RegistrationPage extends SeleniumBaseDriver {

    public RegistrationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    //*********Locators*********
    private String txtPassword = "form[action='mercurycreate_account.php'] [name='password']";
    private String txtConfirmPassword = "form[action='mercurycreate_account.php'] [name='confirmPassword']";
    private String txtFirstName = "form[action='mercurycreate_account.php'] [name='firstName']";
    private String txtSurName = "form[action='mercurycreate_account.php'] [name='lastName']";
    private String txtPhone = "form[action='mercurycreate_account.php'] [name='phone']";
    private String txtEmail = "form[action='mercurycreate_account.php'] [name='userName']";
    private String txtUsername = "form[action='mercurycreate_account.php'] [name='email']";
    private String txtPostCode = "form[action='mercurycreate_account.php'] [name='postalCode']";
    private String txtStreetAddress = "form[action='mercurycreate_account.php'] [name='address1']";
    private String txtCity = "form[action='mercurycreate_account.php'] [name='city']";
    private String txtState = "form[action='mercurycreate_account.php'] [name='state']";
    private String btnSubmit = "form[action='mercurycreate_account.php'] [name='register']";
    private String txtRegSuccessFullname = "//b[contains(text(),'Dear %s')]";
    private String txtRegSuccessUsername = "//b[contains(text(),'Note: Your user name is %s')]";
    private String txtRegSuccessMessage = "//font[contains(normalize-space(),'Thank you for registering.')]";

    private String userName;
    private String fullName;

    //*********Page Methods*********

    public void registerUser(String firstName, String lastName, String phoneNumber, String email,
                             String streetAddress, String city, String state, String postCode, String password) {

        setFullName(firstName, lastName);
        setUserName(email);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPhone(phoneNumber);
        enterEmail(email);
        enterUsername(email);
        enterPassword(password);
        reenterPassword(password);
        enterStreetAddress(streetAddress);
        enterCity(city);
        enterState(state);
        enterPostCode(postCode);
        clickSubmit();
    }

    private void enterUsername(String email) {
        clearAndTypeText(By.cssSelector(txtUsername), email);
    }

    private void reenterPassword(String password) {
        clearAndTypeText(By.cssSelector(txtConfirmPassword), password);
    }

    public void enterPassword(String password) {
        clearAndTypeText(By.cssSelector(txtPassword), password);
    }


    public void enterFirstName (String firstName){
        clearAndTypeText(By.cssSelector(txtFirstName), firstName);
    }

    public void enterLastName(String surName){
        clearAndTypeText(By.cssSelector(txtSurName), surName);
    }

    public void enterPostCode(String postCode) {
        clearAndTypeText(By.cssSelector(txtPostCode), postCode);
    }

    public void enterStreetAddress(String streetAddress) {
        clearAndTypeText(By.cssSelector(txtStreetAddress), streetAddress);
    }

    public void enterCity(String city) {
        clearAndTypeText(By.cssSelector(txtCity), city);
    }

    public void enterState(String state) {
        clearAndTypeText(By.cssSelector(txtState), state);
    }

    public void clickSubmit() {
        click(By.cssSelector(btnSubmit));
    }

    public void enterEmail(String email) {
        clearAndTypeText(By.cssSelector(txtEmail), email);
    }

    public void enterPhone(String phone) {
        clearAndTypeText(By.cssSelector(txtPhone), phone);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName, String lastName) {
        this.fullName = firstName +" " +lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //*********Verifications*********

    //Verify that the full name and the username are displayed in the Registration success message
    public void verifyRegistrationSuccess() {
        verifyRegistrationSuccessMessage();
        waitForVisibilityOfElement(By.xpath(String.format(txtRegSuccessFullname, getFullName())));
        waitForVisibilityOfElement(By.xpath(String.format(txtRegSuccessUsername, getUserName())));
    }

    public void verifyRegistrationSuccessMessage() {
        String expectedSuccessMessage = "Thank you for registering. You may now sign-in using the user name " +
                "and password you've just entered.";
        String actualSuccessMessage = waitForVisibilityOfElement(By.xpath(txtRegSuccessMessage)).getText();
        Assert.assertEquals(actualSuccessMessage, expectedSuccessMessage);
    }

    public void verifyRegistrationFail(){
        //Should not see registration success message
        Assert.assertFalse(waitForPresenceOfElement(By.xpath(txtRegSuccessMessage)).isDisplayed(),
                "FAIL: Got a successful Registration message but shouldn't have, since was using invalid data");
    }
}
