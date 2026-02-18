package com.qa.pages;

import com.microsoft.playwright.PlaywrightException;

import io.qameta.allure.Step;

/**
 * ProductPage - Page Object for product listing and details
 */
public class ProductPage extends BasePage {
    // Locators
    private static final String PRODUCT_TITLE = ".inventory_item_name";
    private static final String PRODUCT_PRICE = ".inventory_item_price";
    private static final String ADD_TO_CART_BUTTON = "button[data-test*='add-to-cart']";
    private static final String REMOVE_BUTTON = "button[data-test*='remove']";
    private static final String CART_BADGE = ".shopping_cart_badge";
    private static final String SORT_DROPDOWN = "[data-test='product_sort_container']";

    /**
     * Get product title by index
     * @param index 1-based product index
     * @return Product title text
     */
    @Step("Get product title at index {index}")
    public String getProductTitle(int index) {
        String locator = String.format("%s:nth-child(%d)", PRODUCT_TITLE, index);
        return getText(locator);
    }

    /**
     * Get product price by index
     * @param index 1-based product index
     * @return Product price as string
     */
    @Step("Get product price at index {index}")
    public String getProductPrice(int index) {
        String locator = String.format("%s:nth-child(%d)", PRODUCT_PRICE, index);
        return getText(locator);
    }

    /**
     * Add product to cart by index with fallback mechanism
     * @param index 1-based product index
     * @throws PlaywrightException if unable to add product after all attempts
     */
    @Step("Add product at index {index} to cart")
    public void addProductToCart(int index) {
        try {
            page.locator("button[data-test*='add-to-cart']").nth(index - 1).click();
            logger.info(String.format("Product at index %d added to cart", index));
        } catch (PlaywrightException e) {
            logger.warn(String.format("Failed with dynamic selector for index %d, retrying with alternative strategy", index), e);
            
            // Fallback: wait and retry with scroll
            try {
                page.locator("button[data-test*='add-to-cart']").nth(index - 1).scrollIntoViewIfNeeded();
                page.locator("button[data-test*='add-to-cart']").nth(index - 1).click();
                logger.info(String.format("Product at index %d added to cart (after scroll)", index));
            } catch (PlaywrightException retryError) {
                logger.error(String.format("Failed to add product at index %d to cart after retry", index), retryError);
                throw retryError;
            }
        }
    }

    /**
     * Get number of items currently in cart
     * @return Count of items in cart, 0 if cart is empty
     */
    @Step("Get current cart count")
    public int getCartCount() {
        try {
            if (isElementVisible(CART_BADGE)) {
                String count = getText(CART_BADGE);
                if (count == null || count.isBlank()) {
                    logger.warn("Cart badge text is empty, returning 0");
                    return 0;
                }
                int cartCount = Integer.parseInt(count.trim());
                logger.info(String.format("Current cart count: %d", cartCount));
                return cartCount;
            }
            logger.debug("Cart badge not visible, cart is empty");
            return 0;
        } catch (NumberFormatException e) {
            logger.error("Cart badge contains non-numeric value", e);
            return 0;
        } catch (PlaywrightException e) {
            logger.debug("Unable to get cart count", e);
            return 0;
        }
    }

    /**
     * Sort products by specified option
     * @param sortOption Sort option value (e.g., "Name (A to Z)", "Price (low to high)")
     */
    @Step("Sort products by: {sortOption}")
    public void sortProducts(String sortOption) {
        selectDropdownOption(SORT_DROPDOWN, sortOption);
    }

    /**
     * Remove product from cart by product name
     * @param productName Name of the product to remove
     */
    @Step("Remove product from cart: {productName}")
    public void removeProductFromCart(String productName) {
        String locator = String.format("button[data-test='remove-%s']",
                productName.toLowerCase().replace(" ", "-"));
        click(locator);
    }
}