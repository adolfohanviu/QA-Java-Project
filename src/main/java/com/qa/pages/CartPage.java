package com.qa.pages;

import com.microsoft.playwright.PlaywrightException;
import com.qa.utils.TestConstants;

import io.qameta.allure.Step;

/**
 * CartPage — Page Object for the SauceDemo shopping cart screen.
 */
public class CartPage extends BasePage {

    private static final String CART_ITEMS              = TestConstants.Selectors.CART_ITEMS;
    private static final String CART_ITEM_NAMES         = ".inventory_item_name";
    private static final String ITEM_PRICE              = ".inventory_item_price";
    private static final String CHECKOUT_BUTTON         = "[data-test='checkout']";
    private static final String CONTINUE_SHOPPING_BUTTON = "[data-test='continue-shopping']";
    private static final String CART_QUANTITY           = ".cart_quantity";

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /**
     * @return Number of line items currently in the cart
     */
    @Step("Get cart item count")
    public int getCartItemCount() {
        try {
            int count = page.locator(CART_ITEMS).count();
            logger.info("Cart contains {} items", count);
            return count;
        } catch (PlaywrightException e) {
            logger.error("Failed to get cart item count", e);
            return 0;
        }
    }

    /**
     * Get the name of a cart item by its zero-based index.
     *
     * @param index Zero-based item index
     * @return Item name text
     */
    @Step("Get cart item name at index {index}")
    public String getCartItemName(int index) {
        return getTextByIndex(CART_ITEM_NAMES, index);
    }

    /**
     * Get the price of a cart item by its zero-based index.
     *
     * @param index Zero-based item index
     * @return Price as a double
     * @throws RuntimeException if the price text is absent or unparseable
     */
    @Step("Get cart item price at index {index}")
    public double getCartItemPrice(int index) {
        String priceText = getTextByIndex(ITEM_PRICE, index);

        if (priceText == null || priceText.isBlank()) {
            String msg = "Price text is empty for item at index " + index;
            logger.warn(msg);
            throw new RuntimeException(msg);
        }

        try {
            double price = Double.parseDouble(priceText.replace("$", "").trim());
            logger.info("Cart item [{}] price: ${}", index, price);
            return price;
        } catch (NumberFormatException e) {
            String msg = "Invalid price format: " + priceText;
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /** @return true when the cart contains no items */
    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    @Step("Click checkout button")
    public void clickCheckoutButton() {
        click(CHECKOUT_BUTTON);
    }

    @Step("Click continue shopping button")
    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
    }
}
