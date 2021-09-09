package org.swaglabs.tests;

import org.swaglabs.DataProviders;
import org.swaglabs.pages.HomePage;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class ProductSortTest extends BaseTest {

    HomePage homePage = new HomePage(driver);

    @Test(dataProvider = "users", dataProviderClass = DataProviders.class, dependsOnGroups = {"LoginTest"}, priority = 2)
    public void sortProductsByPriceLoHi(Map<String, HashMap<String, String>> map) {
        homePage = doValidLogin(map);
        homePage.sortProductByLoHi();
    }

}
