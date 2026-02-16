package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.qa.pages.CartPage;
import com.qa.pages.ProductPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * ProductStepDefinitions - Step definitions for product and shopping scenarios
 */
public class ProductStepDefinitions {
    private static final Logger logger = LogManager.getLogger(ProductStepDefinitions.class);
    private ProductPage productPage;
    private CartPage cartPage;
    private int initialCartCount;

    @Given("User is on the products page")
    public void user_is_on_products_page() {
        productPage = new ProductPage();
        Assert.assertTrue("Should be on inventory page", productPage.getCurrentURL().contains("inventory"));
    }

    @When("User adds product to cart")
    public void user_adds_product_to_cart() {
        initialCartCount = productPage.getCartCount();
        productPage.addProductToCart(1);
    }

    @When("User sorts products by {string}")
    public void user_sorts_products_by(String sortOption) {
        productPage.sortProducts(sortOption);
    }

    @When("User navigates to cart")
    public void user_navigates_to_cart() {
        productPage.click(".shopping_cart_link");
        cartPage = new CartPage();
    }

    @Then("Product should be added to cart")
    public void product_should_be_added_to_cart() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted", e);
        }
        int currentCartCount = productPage.getCartCount();
        Assert.assertTrue("Cart count should increase", currentCartCount > initialCartCount);
    }

    @Then("User should see {int} items in cart")
    public void user_should_see_items_in_cart(int expectedCount) {
        int actualCount = cartPage.getCartItemCount();
        Assert.assertEquals("Cart should have " + expectedCount + " items", expectedCount, actualCount);
    }

    @Then("Cart should be empty")
    public void cart_should_be_empty() {
        Assert.assertTrue("Cart should be empty", cartPage.isCartEmpty());
    }
}
