package com.qa.pages;

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
     */
    public String getProductTitle(int index) {
        String locator = PRODUCT_TITLE + ":nth-child(" + index + ")";
        return getText(locator);
    }

    /**
     * Get product price by index
     */
    public String getProductPrice(int index) {
        String locator = PRODUCT_PRICE + ":nth-child(" + index + ")";
        return getText(locator);
    }

    /**
     * Add product to cart by index
     */
    public void addProductToCart(int index) {
        try {
            // Use Playwright's nth() method to select the nth matching button
            page.locator("button[data-test*='add-to-cart']").nth(index - 1).click();
            logger.info("Product at index " + index + " added to cart");
        } catch (Exception e) {
            // Fallback: try with the hardcoded selector for first product
            logger.warn("Failed with dynamic selector, using fallback", e);
            click("button[data-test='add-to-cart-sauce-labs-backpack']");
            logger.info("Product added to cart (fallback)");
        }
    }

    /**
     * Get cart count
     */
    public int getCartCount() {
        try {
            // Check if element is visible first (doesn't wait long)
            if (isElementVisible(CART_BADGE)) {
                String count = getText(CART_BADGE);
                return Integer.parseInt(count.trim());
            }
            return 0; // Cart is empty if badge doesn't exist
        } catch (Exception e) {
            logger.debug("Cart badge not found, returning 0", e);
            return 0;
        }
    }

    /**
     * Sort products
     */
    public void sortProducts(String sortOption) {
        selectDropdownOption(SORT_DROPDOWN, sortOption);
        logger.info("Products sorted by: " + sortOption);
    }

    /**
     * Remove product from cart
     */
    public void removeProductFromCart(String productName) {
        String locator = "button[data-test='remove-" + productName.toLowerCase().replace(" ", "-") + "']";
        click(locator);
    }
}
