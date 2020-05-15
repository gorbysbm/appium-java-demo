package automation.pages.selenium;

import automation.driver.SeleniumBaseDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;

public class FlightBookingPage extends SeleniumBaseDriver {

    public FlightBookingPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);

    }

    //*********Locators*********
    private String txtLastName = "input[name='passLast0']";
    private String txtFirstName = "input[name='passFirst0']";
    private String txtCreditNumber = "input[name='creditnumber']";
    private String txtBillingAddress1 = "input[name='billAddress1']";
    private String txtBillingCity = "input[name='billCity']";
    private String txtBillingState = "input[name='billState']";
    private String txtBillingZip = "input[name='billZip']";
    private String chkTicketLess = "input[name='ticketLess']";
    private String btnSecurePurchase = "input[name='buyFlights']";

    //*********Page Methods*********
    public void enterFirstName (String firstName){
        clearAndTypeText(By.cssSelector(txtFirstName), firstName);
    }

    public void enterLastName(String surName){
        clearAndTypeText(By.cssSelector(txtLastName), surName);
    }

    public void enterBillingAddress1(String billingAddress1){
        clearAndTypeText(By.cssSelector(txtBillingAddress1), billingAddress1);
    }

    public void enterBillingCity(String billingCity){
        clearAndTypeText(By.cssSelector(txtBillingCity), billingCity);
    }

    public void enterBillingState(String billingState){
        clearAndTypeText(By.cssSelector(txtBillingState), billingState);
    }

    public void enterBillingZip(String billingZip){
        clearAndTypeText(By.cssSelector(txtBillingZip), billingZip);
    }

    public void selectTicketless(){
        click(By.cssSelector(chkTicketLess));
    }

    public void enterCreditCardNumber(String creditCardNumber){
        clearAndTypeText(By.cssSelector(txtCreditNumber), creditCardNumber);
    }

    public void clickContinue() {
        click(By.cssSelector(btnSecurePurchase));
    }

    //*********Verifications*********
    public void bookFlight(WebDriver driver, ITestContext ctx, String creditCardNumber) {
        enterFirstName((String) ctx.getAttribute("firstName"));
        enterLastName((String)  ctx.getAttribute("lastName"));
        enterCreditCardNumber(creditCardNumber);
        enterBillingAddress1((String) ctx.getAttribute("streetAddress"));
        enterBillingCity((String) ctx.getAttribute("city"));
        enterBillingZip((String) ctx.getAttribute("postCode"));
        enterBillingState((String) ctx.getAttribute("state"));
        selectTicketless();
        clickContinue();
    }
}