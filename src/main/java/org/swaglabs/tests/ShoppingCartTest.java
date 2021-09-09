package org.swaglabs.tests;

import org.swaglabs.DataProviders;
import org.swaglabs.pages.CartPage;
import org.swaglabs.pages.HomePage;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartTest extends BaseTest {

    HomePage homePage = new HomePage(driver);
    CartPage cartPage = new CartPage(driver);

    @Test(dataProvider = "users", dataProviderClass = DataProviders.class, groups = "addProducts", dependsOnGroups = {"LoginTest"}, priority = 3)
    public void addingSpecificProductToCartTest(Map<String, HashMap<String, String>> map) {
        homePage = doValidLogin(map);
        cartPage = homePage.addProductByName("Sauce Labs Onesie").goToShoppingCart();

        check("CartPage is visible!", cartPage.isVisible(), true);
        check("Products added appear on Cart Page", cartPage.areProductsAddedToCartVisible(), true);
    }

    @Test(dataProvider = "users", dataProviderClass = DataProviders.class, dependsOnGroups = {"LoginTest", "addProducts"}, priority = 3)
    public void addingMultipleProductsToCartTest(Map<String, HashMap<String, String>> map) {
        homePage = doValidLogin(map);
        cartPage = homePage.addAllProductsOfPage().goToShoppingCart();

        check("CartPage is visible!", cartPage.isVisible(), true);
        check("Products added appear on Cart Page", cartPage.areProductsAddedToCartVisible(), true);
    }

}
