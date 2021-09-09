package org.swaglabs.tests;

import org.swaglabs.DataProviders;
import org.swaglabs.pages.HomePage;
import org.swaglabs.pages.LandingPage;
import org.testng.annotations.Test;
import java.util.*;

public class LoginTest extends BaseTest {

    HomePage homePage = new HomePage(driver);
    LandingPage landingPage = new LandingPage(driver);

    @Test(dataProvider = "users", dataProviderClass = DataProviders.class)
    public void loginTestInvalidCredentials(Map<String, HashMap<String, String>> map) {
        LandingPage landingInvalidPage = new LandingPage(driver);
        HashMap<String,String> user = map.get("invalidUser");
        doLogin(user.get("user"), user.get("pass"));
        check("LandingPage is visible", landingInvalidPage.isVisible(), true);
    }

    @Test(dataProvider = "users", dataProviderClass = DataProviders.class, groups = {"LoginTest"})
    public void loginTestValidCredentials(Map<String, HashMap<String, String>> map) {
        doValidLogin(map);
    }

    @Test(dataProvider = "users", dataProviderClass = DataProviders.class, dependsOnGroups = "LoginTest")
    public void loginTestLogOut(Map<String, HashMap<String, String>> map) {
        homePage = doValidLogin(map);
        landingPage = homePage.logOut();

        check("LandingPage is visible!", landingPage.isVisible(), true);
    }

}
