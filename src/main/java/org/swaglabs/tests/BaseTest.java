package org.swaglabs.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;
import org.swaglabs.pages.BasePage;
import org.swaglabs.pages.HomePage;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    public WebDriver driver;
    private static final String PATH = "/src/main/resources/drivers/";
    private static final String CHROMEDRIVER = "chromedriver.exe";
    private static final String FIREFOXDRIVER = "geckodriver.exe";
    private static final String MSEDGEDRIVER = "msedgedriver.exe";
    private static final String URL = "https://www.saucedemo.com/";
    public static ExtentHtmlReporter htmlReporter;
    static ExtentReports extent;
    public static ExtentTest test;
    HashMap<String,String> user;

    @Parameters({"browser"})
    @BeforeMethod
    public void initDriver(String browser, Method method) {
        test = extent.createTest(method.getName() + ": " + browser);
        test.assignCategory(browser);
        test.log(Status.INFO, "Starting test in browser: " + browser);

        try {
            if (browser.equalsIgnoreCase("Firefox")) {
                System.setProperty("webdriver.gecko.driver",
                        System.getProperty("user.dir") + PATH + FIREFOXDRIVER);
                driver = new FirefoxDriver();
            } else if (browser.equalsIgnoreCase("Chrome")) {
                System.setProperty("webdriver.chrome.driver",
                        System.getProperty("user.dir") + PATH +  CHROMEDRIVER);
                driver = new ChromeDriver();
            } else if (browser.equalsIgnoreCase("Edge")) {
                System.setProperty("webdriver.edge.driver",
                        System.getProperty("user.dir") + PATH +  MSEDGEDRIVER);
                driver = new EdgeDriver();
            }

        } catch (WebDriverException e) {
            throw new WebDriverException("Can not locate webdriver: " + browser);
        }

        driver.get(URL);
        driver.manage().window().maximize();
        test.log(Status.INFO, "Webpage: " + URL + " has been loaded");
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        driver.quit();
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
        } else {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
            test.skip(result.getThrowable());
        }
    }

    @BeforeSuite
    public void setUp() {
        htmlReporter = new ExtentHtmlReporter("src/main/java/org/swaglabs/report.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        extent.setSystemInfo("OS", "Windows 10");
        extent.setSystemInfo("Host Name", "Juan Manuel");
        extent.setSystemInfo("Environment", "PROD");
        extent.setSystemInfo("User Name", "Juan Manuel Cuerva");

        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Test Report - SwagLabs - Env: PROD");
        htmlReporter.config().setReportName("Test Report - SwagLabs");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
    }

    @AfterSuite
    public void tearDown() {
        extent.flush();
    }

    public HomePage doLogin(String user, String pass) {
        BasePage basePage = new BasePage(driver);
        basePage.doingLogin(user, pass);
        return new HomePage(driver);
    }

    public HomePage doValidLogin(Map<String, HashMap<String,String>> map) {
        HomePage homePage;
        user = map.get("validUser");
        homePage = doLogin(user.get("user"), user.get("pass"));
        check("HomePage is visible", homePage.isVisible(), true);
        return homePage;
    }

    public void check(String checking, Object actualValue, Object expectedValue) {
        test.log(Status.INFO, "Verifying that " + checking + ": Expected [" + expectedValue + "]" +
                " Actual [" + actualValue + "]");
        Assert.assertEquals(actualValue, expectedValue, checking);
        test.log(Status.PASS,  "" + checking + " has been verified successfully. " + " Actual [" + actualValue + "]");
    }

}
