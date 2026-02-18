package com.qa.pages;

import com.microsoft.playwright.PlaywrightException;

import io.qameta.allure.Step;

/**
 * CartPage - Page Object for shopping cart functionality
 */
public class CartPage extends BasePage {
    // Locators
    private static final String CART_ITEMS = ".cart_item";
    private static final String CART_ITEM_NAMES = ".inventory_item_name";
    private static final String ITEM_PRICE = ".inventory_item_price";
    private static final String CHECKOUT_BUTTON = "[data-test='checkout']";
    private static final String CONTINUE_SHOPPING_BUTTON = "[data-test='continue-shopping']";
    private static final String CART_QUANTITY = ".cart_quantity";
    private static final String EMPTY_CART_MESSAGE = ".empty_cart";

    /**
     * Get number of items in cart
     * @return Count of items in cart
     */
    @Step("Get cart item count")
    public int getCartItemCount() {
        try {
            int count = page.locator(CART_ITEMS).count();
            logger.info(String.format("Cart contains %d items", count));
            return count;
        } catch (PlaywrightException e) {
            logger.error("Failed to get cart item count", e);
            return 0;
        }
    }

    /**
     * Click checkout button
     * @throws PlaywrightException if button not found
     */
    @Step("Click checkout button")
    public void clickCheckoutButton() {
        click(CHECKOUT_BUTTON);
    }

    /**
     * Get cart item price by index
     * @param index 1-based item index
     * @return Price as double
     * @throws PlaywrightException if price cannot be retrieved
     */
    @Step("Get cart item price at index {index}")
    public double getCartItemPrice(int index) {
        String locator = String.format("%s:nth-child(%d)", ITEM_PRICE, index);
        String priceText = getText(locator);
        
        if (priceText == null || priceText.isBlank()) {
            logger.warn(String.format("Price text is empty for item at index %d", index));
            throw new RuntimeException(String.format("Unable to retrieve price for item at index %d", index));
        }
        
        try {
            double price = Double.parseDouble(priceText.replace("$", "").trim());
            logger.info(String.format("Cart item %d price: $%.2f", index, price));
            return price;
        } catch (NumberFormatException e) {
            logger.error(String.format("Failed to parse price: %s", priceText), e);
            throw new RuntimeException(String.format("Invalid price format: %s", priceText), e);
        }
    }

    /**
     * Continue shopping (back to products)
     */
    @Step("Click continue shopping button")
    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
    }

    /**
     * Check if cart is empty
     * @return true if cart is empty, false otherwise
     */
    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        int itemCount = getCartItemCount();
        boolean isEmpty = itemCount == 0;
        logger.info(String.format("Cart is %s", isEmpty ? "empty" : "not empty"));
        return isEmpty;
    }

    /**
     * Get all item names in cart
     * @return Array of item names
     */
    @Step("Get all item names from cart")
    public String[] getCartItemNames() {
        try {
            int count = page.locator(CART_ITEM_NAMES).count();
            String[] names = new String[count];
            
            for (int i = 0; i < count; i++) {
                names[i] = page.locator(CART_ITEM_NAMES).nth(i).textContent();
            }
            
            logger.info(String.format("Retrieved %d item names from cart", count));
            return names;
        } catch (PlaywrightException e) {
            logger.error("Failed to get cart item names", e);
            throw e;
        }
    }

    /**
     * Verify item exists in cart by name
     * @param itemName Name of item to verify
     * @return true if item found, false otherwise
     */
    @Step("Verify item '{itemName}' exists in cart")
    public boolean isItemInCart(String itemName) {
        try {
            String[] itemNames = getCartItemNames();
            for (String name : itemNames) {
                if (name != null && name.contains(itemName)) {
                    logger.info(String.format("Item '%s' found in cart", itemName));
                    return true;
                }
            }
            logger.warn(String.format("Item '%s' not found in cart", itemName));
            return false;
        } catch (Exception e) {
            logger.error(String.format("Error checking if item '%s' is in cart", itemName), e);
            return false;
        }
    }

    /**
     * Get total cart price
     * @return Total price of all items in cart
     */
    @Step("Get total cart price")
    public double getTotalPrice() {
        try {
            int itemCount = getCartItemCount();
            double total = 0.0;
            
            for (int i = 1; i <= itemCount; i++) {
                total += getCartItemPrice(i);
            }
            
            logger.info(String.format("Total cart price: $%.2f", total));
            return total;
        } catch (Exception e) {
            logger.error("Failed to calculate total price", e);
            throw new RuntimeException("Unable to calculate total price", e);
        }
    }
}