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
 * ProductStepDefinitions - Step definitions for product and shopping scenarios
 * Uses TestConstants for consistent selector and assertion messages
 */
public class ProductStepDefinitions {
    private static final Logger logger = LogManager.getLogger(ProductStepDefinitions.class);
    private ProductPage productPage;
    private CartPage cartPage;
    private int initialCartCount;

    @Given("User is on the products page")
    @Step("Verify user is on products page")
    public void user_is_on_products_page() {
        productPage = new ProductPage();
        productPage.waitForElement(TestConstants.Selectors.PRODUCTS_CONTAINER);
        
        String currentUrl = productPage.getCurrentURL();
        Assert.assertTrue(
                String.format(TestConstants.AssertionMessages.WRONG_PAGE + ": %s", currentUrl),
                currentUrl.contains("inventory"));
        logger.info("Verified user is on products page");
    }

    @When("User adds product to cart")
    @Step("Add first product to cart")
    public void user_adds_product_to_cart() {
        initialCartCount = productPage.getCartCount();
        logger.info(String.format("Initial cart count: %d", initialCartCount));
        
        productPage.addProductToCart(1);
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
        cartPage.waitForElement(".inventory_item");
        logger.info("Navigated to cart page");
    }

    @Then("Product should be added to cart")
    @Step("Verify product was added to cart")
    public void product_should_be_added_to_cart() {
        // Wait for cart badge to appear or update using Playwright's implicit waits
        productPage.waitForElement(TestConstants.Selectors.CART_BADGE);
        
        int currentCartCount = productPage.getCartCount();
        logger.info(String.format("Current cart count: %d", currentCartCount));
        
        Assert.assertTrue(
                String.format(TestConstants.AssertionMessages.CART_COUNT_MISMATCH + 
                        ": should increase from %d but current count is %d",
                        initialCartCount, currentCartCount),
                currentCartCount > initialCartCount);
        logger.info("Verified product was successfully added to cart");
    }

    @Then("User should see {int} items in cart")
    @Step("Verify cart contains {expectedCount} items")
    public void user_should_see_items_in_cart(int expectedCount) {
        int actualCount = cartPage.getCartItemCount();
        Assert.assertEquals(
                String.format(TestConstants.AssertionMessages.CART_COUNT_MISMATCH + 
                        ": should have %d items but has %d", expectedCount, actualCount),
                expectedCount,
                actualCount);
        logger.info(String.format("Verified cart contains %d items", expectedCount));
    }

    @Then("Cart should be empty")
    @Step("Verify cart is empty")
    public void cart_should_be_empty() {
        Assert.assertTrue(
                "Cart should be empty but contains items",
                cartPage.isCartEmpty());
        logger.info("Verified cart is empty");
    }
}