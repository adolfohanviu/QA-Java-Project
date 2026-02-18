package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.qa.pages.CartPage;
import com.qa.pages.ProductPage;
import com.qa.utils.TestConstants;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;

/**
 * ProductStepDefinitions — Cucumber step implementations for product and cart scenarios.
 */
public class ProductStepDefinitions {

    private static final Logger logger = LogManager.getLogger(ProductStepDefinitions.class);

    private ProductPage productPage;
    private CartPage cartPage;
    private int initialCartCount;

    // -------------------------------------------------------------------------
    // Given
    // -------------------------------------------------------------------------

    @Given("User is on the products page")
    @Step("Verify user is on products page")
    public void user_is_on_products_page() {
        productPage = new ProductPage();
        productPage.waitForElement(TestConstants.Selectors.PRODUCTS_CONTAINER);
        String currentUrl = productPage.getCurrentURL();
        Assert.assertTrue(
                TestConstants.AssertionMessages.WRONG_PAGE + ": " + currentUrl,
                currentUrl.contains("inventory"));
        logger.info("Verified user is on products page");
    }

    // -------------------------------------------------------------------------
    // When
    // -------------------------------------------------------------------------

    @When("User adds product to cart")
    @Step("Add first product to cart")
    public void user_adds_product_to_cart() {
        initialCartCount = productPage.getCartCount();
        logger.info("Initial cart count: {}", initialCartCount);
        productPage.addProductToCart(1);
    }

    /**
     * Add a specific product by its one-based index.
     */
    @When("User adds product {int} to cart")
    @Step("Add product at index {index} to cart")
    public void user_adds_product_at_index_to_cart(int index) {
        initialCartCount = productPage.getCartCount();
        logger.info("Initial cart count: {}", initialCartCount);
        productPage.addProductToCart(index);
    }

    @When("User sorts products by {string}")
    @Step("Sort products by: {sortOption}")
    public void user_sorts_products_by(String sortOption) {
        productPage.sortProducts(sortOption);
    }

    @When("User navigates to cart")
    @Step("Navigate to shopping cart")
    public void user_navigates_to_cart() {
        productPage.click(TestConstants.Selectors.CART_LINK);
        cartPage = new CartPage();
        cartPage.waitForURL("cart.html");
        cartPage.waitForElement(TestConstants.Selectors.CART_ITEMS);
        logger.info("Navigated to cart page");
    }

    // -------------------------------------------------------------------------
    // Then
    // -------------------------------------------------------------------------

    @Then("Product should be added to cart")
    @Step("Verify product was added to cart")
    public void product_should_be_added_to_cart() {
        productPage.waitForElement(TestConstants.Selectors.CART_BADGE);

        int currentCount = productPage.getCartCount();
        logger.info("Current cart count: {}", currentCount);

        Assert.assertTrue(
                TestConstants.AssertionMessages.CART_COUNT_MISMATCH
                        + " — should be > " + initialCartCount + " but was " + currentCount,
                currentCount > initialCartCount);
        logger.info("Verified product was added to cart (count {} → {})",
                initialCartCount, currentCount);
    }

    @Then("User should see {int} items in cart")
    @Step("Verify cart has {expectedCount} items")
    public void user_should_see_items_in_cart(int expectedCount) {
        int actualCount = cartPage.getCartItemCount();
        Assert.assertEquals(
                "Cart should contain " + expectedCount + " item(s) but found " + actualCount,
                expectedCount, actualCount);
        logger.info("Cart contains {} item(s) as expected", actualCount);
    }

    @Then("Cart should be empty")
    @Step("Verify cart is empty")
    public void cart_should_be_empty() {
        Assert.assertTrue("Cart should be empty", cartPage.isCartEmpty());
        logger.info("Verified cart is empty");
    }

    @Then("Products should be sorted by price low to high")
    @Step("Verify products are sorted low to high")
    public void products_should_be_sorted_price_low_to_high() {
        int count = productPage.getProductCount();
        Assert.assertTrue("Need at least 2 products to verify sort order", count >= 2);

        double previousPrice = productPage.parsePrice(productPage.getProductPrice(0));
        for (int i = 1; i < count; i++) {
            double currentPrice = productPage.parsePrice(productPage.getProductPrice(i));
            Assert.assertTrue(
                    TestConstants.AssertionMessages.SORT_ORDER_WRONG
                            + ": product[" + i + "] price $" + currentPrice
                            + " < product[" + (i - 1) + "] price $" + previousPrice,
                    currentPrice >= previousPrice);
            previousPrice = currentPrice;
        }
        logger.info("Verified products are sorted by price (low to high), {} items checked", count);
    }
}
