package org.swaglabs.pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.swaglabs.tests.BaseTest.test;

public class HomePage extends BasePage {

    @FindBy(xpath = "//span[@class='title']")
    private WebElement homePageText;

    @FindBy(css = ".shopping_cart_link")
    private WebElement shoppingCart;

    @FindBy(css = "[type = 'submit']")
    private WebElement loginButton;

    @FindBy(css = ".bm-burger-button")
    private WebElement burguerMenu;

    @FindBy(css = "#logout_sidebar_link")
    private WebElement logOutButton;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> inventoryItemName;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> inventoryPriceItems;

    @FindBy(css = ".shopping_cart_badge")
    private List<WebElement> shoppingCartBadge;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public LandingPage logOut() {
        click(burguerMenu);
        waitForElementVisibility(logOutButton);
        click(logOutButton);
        return new LandingPage(driver);
    }

    public void sortProductByLoHi() {
        test.log(Status.INFO, "Sorting elements from Lowest to Highest...");

        Select sortProduct = new Select(driver.findElement(By.className("product_sort_container")));
        sortProduct.selectByValue("lohi");

        List <Double> actualWebSortedPrices = getItemsPrice();
        List <Double> expectedSortedPrices = getItemsPrice().stream().sorted().collect(toList());

        checkThat("Products are sorted from lowest to highest", actualWebSortedPrices, expectedSortedPrices);
    }

    public HomePage addProductByName(String name) {
        test.log(Status.INFO, "Adding product by name: " + name);
        for(WebElement item : inventoryItemName) {
            if (name.equals(item.getText())) {
                String addCartItemName = name.toLowerCase().replace(" ", "-");
                WebElement addCartItem = driver.findElement(By.cssSelector("[id='add-to-cart-" + addCartItemName + "']"));
                click(addCartItem);
                test.log(Status.INFO, "Product has been added successfully");
                expectedProducts.add(item.getText());
                return this;
            }
        }
        test.log(Status.FAIL, "Product with name: [" + name + "] could not be found");
        return this;
    }

    public HomePage addAllProductsOfPage() {
        test.log(Status.INFO, "Adding all listed products...");
        List<String> actualListedProducts = inventoryItemName.stream()
                .map(WebElement::getText)
                .collect(toList());
        for (String itemName : actualListedProducts) {
            addProductByName(itemName);
        }

        int itemsInShoppingCart = Integer.parseInt(shoppingCart.getText());
        Assert.assertEquals(itemsInShoppingCart, actualListedProducts.size(), "Multiple items should be added");
        test.log(Status.INFO, "All products have been added successfully");
        return this;
    }

    public CartPage goToShoppingCart() {
        test.log(Status.INFO, "Going to Shopping Cart");
        click(shoppingCart);
        return new CartPage(driver);
    }

    public List<Double> getItemsPrice() {
        return inventoryPriceItems.stream().map(WebElement::getText)
                .map(s -> Double.parseDouble(s.substring(1)))
                .collect(toList());
    }

    public boolean isVisible() {
        waitForElementVisibility(homePageText);
        if (homePageText.isDisplayed() &&
                shoppingCart.isDisplayed()) {
            Assert.assertEquals(homePageText.getText(), "PRODUCTS");
            return true;
        }
        test.log(Status.FAIL,  "HomePage is not visible!");
        return false;
    }

}
