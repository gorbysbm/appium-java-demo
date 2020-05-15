package automation.util;

import automation.driver.DriverCreator;
import org.openqa.selenium.Cookie;
import org.testng.ITestContext;

import java.util.Set;

public class CookieHelper {

    public static void storeCurrentSessionCookies(ITestContext ctx){
        Set<Cookie> allCookies = DriverCreator.getCurrentWebDriver().manage().getCookies();
        ctx.setAttribute("driverCookies", allCookies);
    }

    //Only cookies from current domain of the browser can be applied (i.e. homepage). It can't be applied to "About: Blank" page
    public static void applyPriorSessionCookies(ITestContext ctx){
        clearCurrentSessionCookies();
        Set<Cookie> allCookies  = (Set<Cookie>)ctx.getAttribute("driverCookies");
        for(Cookie cookie : allCookies)
        {
            DriverCreator.getCurrentWebDriver().manage().addCookie(cookie);
        }
        //Refresh causes cookies to get applied to the session.
        DriverCreator.getCurrentWebDriver().navigate().refresh();
    }

    public static void clearCurrentSessionCookies(){
        DriverCreator.getCurrentWebDriver().manage().deleteAllCookies();
    }

}
