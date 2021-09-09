package org.swaglabs.pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import java.util.List;

import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static org.swaglabs.tests.BaseTest.test;

public class CartPage extends HomePage {

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> inventoryItemName;

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = "#checkout")
    private WebElement checkOutButton;

    @FindBy(css = "#first-name")
    private WebElement firstNameInput;

    @FindBy(css = "#last-name")
    private WebElement lastNameInput;

    @FindBy(css = "#postal-code")
    private WebElement postalCodeInput;

    @FindBy(css = "#continue")
    private WebElement continueButton;

    @FindBy(css = "#finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement headerText;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement itemTotalPriceLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalPriceLabel;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean areProductsAddedToCartVisible() {
        List<String> actualListedProducts = inventoryItemName.stream()
                .map(WebElement::getText)
                .collect(toList());
        return  actualListedProducts.containsAll(expectedProducts);
    }

    public CartPage completePurchase(String name, String lastName, String postalCode) {
        List<Double> productsPricesInCartPage = getItemsPrice();
        click(checkOutButton);

        checkThat("CHECKOUT - INFORMATION Page is shown",pageTitle.getText(),"CHECKOUT: YOUR INFORMATION");

        type(firstNameInput, name);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        click(continueButton);

        checkThat("CHECKOUT - OVERVIEW Page is shown",pageTitle.getText(),"CHECKOUT: OVERVIEW");

        List<Double> productPricesInCheckOut = getItemsPrice();
        boolean checkProductPrices = productsPricesInCartPage.containsAll(productPricesInCheckOut);

        checkThat("Product prices are the same throughout pages",checkProductPrices, true);

        verifyThatProductPricesMatchExpectedOnes();

        click(finishButton);

        checkThat("CHECKOUT - COMPLETE Page is shown",pageTitle.getText(),"CHECKOUT: COMPLETE!");
        checkThat("Purchase has been completed",headerText.getText(),"THANK YOU FOR YOUR ORDER");

        return this;
    }

    public void verifyThatProductPricesMatchExpectedOnes() {

        List<Double> productPricesInCheckOut = getItemsPrice();
        Double itemTotalPrice = getDoubleValueFromPriceLabel(itemTotalPriceLabel);
        Double totalPriceInCheckOut = productPricesInCheckOut.stream().collect(summingDouble(f->f));

        checkThat("Total sum is correct", totalPriceInCheckOut, itemTotalPrice);

        Double tax = Math.round((itemTotalPrice*0.08) * 100.0)/100.0;
        Double taxValue = getDoubleValueFromPriceLabel(taxLabel);

        checkThat("Tax is calculated correctly", taxValue, tax);

        Double totalPrice = itemTotalPrice + tax;
        Double totalPriceValue = getDoubleValueFromPriceLabel(totalPriceLabel);

        checkThat("Total price is calculated correctly", totalPriceValue, totalPrice);

    }

    public Double getDoubleValueFromPriceLabel(WebElement priceLabel) {
        int indexOfDollar = priceLabel.getText().indexOf("$");
        return Double.parseDouble(priceLabel.getText().substring(indexOfDollar+1));
    }

    @Override
    public boolean isVisible() {
        waitForElementVisibility(pageTitle);
        if (pageTitle.isDisplayed()) {
            Assert.assertEquals(pageTitle.getText(), "YOUR CART");
            return true;
        }
        test.log(Status.FAIL,  "CartPage is not visible!");
        return false;
    }

}
