package org.swaglabs.pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.swaglabs.tests.BaseTest.test;


public class BasePage {
    WebDriver driver;
    List<String> expectedProducts = new ArrayList<>();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void click(WebElement elementToClick) {
        test.log(Status.INFO, "Clicking on " + getHumanFriendlyName(elementToClick));
        elementToClick.click();
    }

    public void type(WebElement element, String text) {
        waitForElementVisibility(element);
        test.log(Status.INFO, "Typing [" + text + "]" + " into " + getHumanFriendlyName(element));
        element.sendKeys(text);
    }

    public void waitForElementVisibility(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 15L);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            test.log(Status.INFO, "Verifying visibility of: " + getHumanFriendlyName(element));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Can't find "+ getHumanFriendlyName(element) + " presence");
        }
    }

    public String getHumanFriendlyName(WebElement element) {
        String elementName = element.toString();
        String result = null;
        int index = elementName.indexOf("-> ");
        if (index != -1) {
            result = elementName.substring(index+3, elementName.length()-1);
        }
        return result;
    }

    public void doingLogin(String user, String password) {
        LandingPage landingPage = new LandingPage(driver);
        landingPage.isVisible();
        test.log(Status.INFO, "Doing Log In...");
        landingPage.login(user, password);
        test.log(Status.INFO, "Log In done successfully");
    }

    public void checkThat(String checking, Object actualValue, Object expectedValue) {
        test.log(Status.INFO, "Verifying that " + checking + " Expected [" + expectedValue + "]" +
                " Actual [" + actualValue + "]");
        Assert.assertEquals(actualValue, expectedValue, checking);
        test.log(Status.PASS,  "" + checking + " has been verified successfully. " + " Actual [" + actualValue + "]");
    }

}
