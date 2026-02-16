package com.qa.pages;

/**
 * CartPage - Page Object for shopping cart functionality
 */
public class CartPage extends BasePage {
    // Locators
    private static final String CART_ITEMS = ".cart_item";
    private static final String ITEM_PRICE = ".inventory_item_price";
    private static final String CHECKOUT_BUTTON = "[data-test='checkout']";
    private static final String CONTINUE_SHOPPING_BUTTON = "[data-test='continue-shopping']";
    private static final String CART_QUANTITY = ".cart_quantity";

    /**
     * Get number of items in cart
     */
    public int getCartItemCount() {
        try {
            return page.locator(CART_ITEMS).count();
        } catch (Exception e) {
            logger.warn("Failed to get cart item count");
            return 0;
        }
    }

    /**
     * Click checkout button
     */
    public void clickCheckoutButton() {
        click(CHECKOUT_BUTTON);
        logger.info("Checkout button clicked");
    }

    /**
     * Get cart item price
     */
    public double getCartItemPrice(int index) {
        String locator = ITEM_PRICE + ":nth-child(" + index + ")";
        String priceText = getText(locator).replace("$", "").trim();
        return Double.parseDouble(priceText);
    }

    /**
     * Continue shopping
     */
    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        logger.info("Continuing shopping");
    }

    /**
     * Is cart empty
     */
    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }
}
