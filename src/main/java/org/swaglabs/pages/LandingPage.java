package org.swaglabs.pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import static org.swaglabs.tests.BaseTest.test;

public class LandingPage extends BasePage {

    @FindBy(css = "[placeholder='Username']")
    private WebElement userNameElement;

    @FindBy(css = "[placeholder='Password']")
    private WebElement passwordElement;

    @FindBy(css = "[type = 'submit']")
    private WebElement loginButton;

    @FindBy(css = "[data-test=error]")
    private WebElement invalidLoginGraphic;

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    public LandingPage enterUser(String user) {
        type(userNameElement, user);
        return this;
    }

    public LandingPage enterPassword(String password) {
        type(passwordElement, password);
        return this;
    }

    public LandingPage clickLoginButton() {
        click(loginButton);
        return this;
    }

    public LandingPage login(String user, String pass) {
            this.enterUser(user).enterPassword(pass).clickLoginButton();
            return this;
    }

    public boolean isVisible() {
        if (loginButton.isDisplayed() &&
                passwordElement.isDisplayed() &&
                userNameElement.isDisplayed()) {
            return true;
        }
        test.log(Status.FAIL,  "LandingPage is not visible!");
        return false;
    }

    public boolean isInvalidLoginMessageShown() {
        test.log(Status.INFO, "Verifying invalid login error message visibility");
        invalidLoginGraphic.isDisplayed();
        String invalidLoginText = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(invalidLoginGraphic.getText(), invalidLoginText, "Invalid login text");
        test.log(Status.INFO, "Invalid login text: [" + invalidLoginGraphic.getText() + "] is shown");
        return true;
    }

}
