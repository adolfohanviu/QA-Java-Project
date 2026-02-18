package com.qa.pages;

import com.qa.utils.TestConstants;

import io.qameta.allure.Step;

/**
 * ProductPage — Page Object for the SauceDemo product inventory screen.
 */
public class ProductPage extends BasePage {

    // Selectors
    private static final String PRODUCT_TITLE      = TestConstants.Selectors.PRODUCT_TITLE;
    private static final String PRODUCT_PRICE      = TestConstants.Selectors.PRODUCT_PRICE;
    private static final String ADD_TO_CART_BUTTON = TestConstants.Selectors.ADD_TO_CART_BUTTON;
    private static final String CART_BADGE         = TestConstants.Selectors.CART_BADGE;
    private static final String SORT_DROPDOWN      = TestConstants.Selectors.SORT_DROPDOWN;

    // -------------------------------------------------------------------------
    // Product queries
    // -------------------------------------------------------------------------

    /**
     * Get the title of a product by its zero-based display index.
     * 
     * @param index Zero-based product index
     * @return Product title text
     */
    @Step("Get product title at index {index}")
    public String getProductTitle(int index) {
        return getTextByIndex(PRODUCT_TITLE, index);
    }

    /**
     * Get the price text of a product by its zero-based display index.
     * 
     * @param index Zero-based product index
     * @return Raw price string (e.g. "$9.99")
     */
    @Step("Get product price at index {index}")
    public String getProductPrice(int index) {
        return getTextByIndex(PRODUCT_PRICE, index);
    }

    /**
     * Parse a price string to a double (strips the leading "$").
     *
     * @param priceText e.g. "$9.99"
     * @return Numeric price value
     */
    public double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText.replace("$", "").trim());
        } catch (NumberFormatException e) {
            logger.error("Cannot parse price: {}", priceText, e);
            throw new RuntimeException("Invalid price format: " + priceText, e);
        }
    }

    /**
     * Get the total number of products currently listed on the page.
     *
     * @return Product count
     */
    @Step("Count products on page")
    public int getProductCount() {
        return countElements(PRODUCT_TITLE);
    }

    // -------------------------------------------------------------------------
    // Cart interactions
    // -------------------------------------------------------------------------

    /**
     * Add a product to the cart by its one-based display index.
     * 
     * @param oneBasedIndex 1-based product index (1 = first product)
     */
    @Step("Add product at index {oneBasedIndex} to cart")
    public void addProductToCart(int oneBasedIndex) {
        int zeroIndex = oneBasedIndex - 1;
        page.locator(ADD_TO_CART_BUTTON).nth(zeroIndex).click();
        logger.info("Added product at index {} to cart", oneBasedIndex);
    }

    /**
     * Get the current cart item count from the badge.
     *
     * @return Cart count, or 0 when cart is empty (badge not present)
     */
    @Step("Get cart item count from badge")
    public int getCartCount() {
        if (!isElementVisible(CART_BADGE)) {
            return 0;
        }
        try {
            return Integer.parseInt(getText(CART_BADGE).trim());
        } catch (NumberFormatException e) {
            logger.debug("Cart badge text is not a number; returning 0", e);
            return 0;
        }
    }

    // -------------------------------------------------------------------------
    // Sorting
    // -------------------------------------------------------------------------

    /**
     * Sort products using the dropdown.
     *
     * @param sortOption Display label (use {@link com.qa.utils.TestConstants.SortOptions})
     */
    @Step("Sort products by: {sortOption}")
    public void sortProducts(String sortOption) {
        selectDropdownOption(SORT_DROPDOWN, sortOption);
        logger.info("Products sorted by: {}", sortOption);
    }

    // -------------------------------------------------------------------------
    // Remove from cart
    // -------------------------------------------------------------------------

    /**
     * Remove a specific product from the cart via its remove button.
     *
     * @param productDataTestName The data-test attribute suffix for the product
     *                            (e.g. "sauce-labs-backpack")
     */
    @Step("Remove product '{productDataTestName}' from cart")
    public void removeProductFromCart(String productDataTestName) {
        String locator = "button[data-test='remove-" + productDataTestName + "']";
        click(locator);
        logger.info("Removed product '{}' from cart", productDataTestName);
    }
}
