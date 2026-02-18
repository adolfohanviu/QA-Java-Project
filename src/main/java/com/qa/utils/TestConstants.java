package com.qa.utils;

/**
 * TestConstants - Centralized test constants and configuration values
 * Avoid hardcoding values in tests and page objects
 * Update these values to match your test environment
 */
public class TestConstants {
    
    // ============ TIMEOUT CONSTANTS ============
    /** Page load timeout in milliseconds (30 seconds) */
    public static final int PAGE_LOAD_TIMEOUT_MS = 30000;
    
    /** Element wait timeout in milliseconds (10 seconds) */
    public static final int ELEMENT_WAIT_TIMEOUT_MS = 10000;
    
    // ============ RETRY CONSTANTS ============
    /** Maximum number of retry attempts for flaky element interactions */
    public static final int MAX_RETRY_ATTEMPTS = 3;
    
    /** Delay between retry attempts in milliseconds (500ms) */
    public static final int RETRY_DELAY_MS = 500;
    
    // ============ TEST USER CREDENTIALS ============
    /** Test users for various scenarios */
    public static final class TestUsers {
        /** Standard user with all permissions */
        public static final String STANDARD_USER = "standard_user";
        
        /** Standard user password */
        public static final String STANDARD_PASSWORD = "secret_sauce";
        
        /** Locked user that cannot log in */
        public static final String LOCKED_USER = "locked_out_user";
        
        /** User that experiences UI glitches (problem_user) */
        public static final String PROBLEM_USER = "problem_user";
        
        /** Invalid user for negative testing */
        public static final String INVALID_USER = "invalid_user";
        
        /** Invalid password for negative testing */
        public static final String INVALID_PASSWORD = "wrong_password";
    }
    
    // ============ URL PATHS ============
    /** URL paths relative to base URL */
    public static final class URLPaths {
        /** Login page path */
        public static final String LOGIN_PAGE_PATH = "/";
        
        /** Products listing page path */
        public static final String PRODUCTS_PAGE_PATH = "/inventory.html";
        
        /** Shopping cart page path */
        public static final String CART_PAGE_PATH = "/cart.html";
        
        /** Checkout page path */
        public static final String CHECKOUT_PAGE_PATH = "/checkout-step-one.html";
    }
    
    // ============ COMMON SELECTORS ============
    /** Common CSS selectors used across multiple page objects */
    public static final class Selectors {
        /** Container for products list */
        public static final String PRODUCTS_CONTAINER = ".inventory_container";
        
        /** Shopping cart badge with item count */
        public static final String CART_BADGE = ".shopping_cart_badge";
        
        /** Error message element on login form */
        public static final String ERROR_MESSAGE = "[data-test='error']";
        
        /** Shopping cart link in header */
        public static final String CART_LINK = ".shopping_cart_link";
        
        /** Menu button for hamburger menu */
        public static final String MENU_BUTTON = "#react-burger-menu-btn";
    }
    
    // ============ WAIT MESSAGES ============
    /** Custom messages for wait operations */
    public static final class WaitMessages {
        /** Products page did not load within timeout */
        public static final String PRODUCTS_NOT_LOADED = "Products page did not load within timeout";
        
        /** Cart was not updated within timeout */
        public static final String CART_NOT_UPDATED = "Cart was not updated within timeout";
        
        /** Element was not visible within timeout */
        public static final String ELEMENT_NOT_VISIBLE = "Element was not visible within timeout";
        
        /** Dropdown menu did not appear */
        public static final String MENU_NOT_APPEARED = "Dropdown menu did not appear within timeout";
    }
    
    // ============ ASSERTION MESSAGES ============
    /** Standard assertion failure message templates */
    public static final class AssertionMessages {
        /** Response status code doesn't match expected */
        public static final String INVALID_STATUS_CODE = "Response status code does not match expected value";
        
        /** Response missing required field */
        public static final String MISSING_RESPONSE_FIELD = "Response is missing required field";
        
        /** Response field value doesn't match expected */
        public static final String INVALID_FIELD_VALUE = "Response field value does not match expected";
        
        /** Cart count is incorrect */
        public static final String CART_COUNT_MISMATCH = "Cart item count does not match expected";
        
        /** User not on expected page */
        public static final String WRONG_PAGE = "Expected to be on different page";
    }
    
    // ============ TEST DATA ============
    /** Test data values used across scenarios */
    public static final class TestData {
        /** Default product name for shopping tests */
        public static final String PRODUCT_SAUCE_LABS_BACKPACK = "Sauce Labs Backpack";
        
        /** Default product price for assertions */
        public static final String PRODUCT_PRICE = "$29.99";
        
        /** Standard sort option */
        public static final String SORT_PRICE_LOW_TO_HIGH = "Price (low to high)";
        
        /** Standard sort option */
        public static final String SORT_PRICE_HIGH_TO_LOW = "Price (high to low)";
    }
    
    // ============ LOG MESSAGES ============
    /** Pre-formatted log messages for consistency */
    public static final class LogMessages {
        /** Message format: "Navigated to {url}" */
        public static final String NAVIGATED_TO = "Navigated to %s";
        
        /** Message format: "Login performed with username: {username}" */
        public static final String LOGIN_PERFORMED = "Login performed with username: %s";
        
        /** Message format: "Added {count} items to cart" */
        public static final String ITEMS_ADDED = "Added %d items to cart";
    }
}