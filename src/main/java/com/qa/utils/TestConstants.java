package com.qa.utils;

/**
 * TestConstants — Centralized test constants and configuration values.
 *
 * All other test-wide constants (timeouts, selectors, assertion messages,
 * URL paths, sort options) remain centralized here to avoid scattered
 * magic strings across the codebase.
 */
public final class TestConstants {

    private TestConstants() {
        // Utility class — do not instantiate
    }

    // =========================================================================
    // TIMEOUT CONSTANTS
    // =========================================================================

    /** Page/navigation load timeout in milliseconds (30 s). */
    public static final int PAGE_LOAD_TIMEOUT_MS = 30_000;

    /** Default element wait timeout in milliseconds (10 s). */
    public static final int ELEMENT_WAIT_TIMEOUT_MS = 10_000;

    /** Maximum retry attempts for flaky element interactions. */
    public static final int MAX_RETRY_ATTEMPTS = 3;

    /** Delay between retry attempts in milliseconds. */
    public static final int RETRY_DELAY_MS = 500;

    // =========================================================================
    // TEST USER CREDENTIALS
    //
    //   TEST_STANDARD_USER      (default: standard_user)
    //   TEST_STANDARD_PASSWORD  (default: secret_sauce — only for demo site)
    //   TEST_LOCKED_USER        (default: locked_out_user)
    //   TEST_PROBLEM_USER       (default: problem_user)
    //   TEST_INVALID_USER       (default: invalid_user)
    //   TEST_INVALID_PASSWORD   (default: wrong_password)
    // =========================================================================

    public static final class TestUsers {

        private TestUsers() {}

        public static final String STANDARD_USER =
                getEnvOrDefault("TEST_STANDARD_USER", "standard_user");

        /** WARNING: only use the default for public demo sites. */
        public static final String STANDARD_PASSWORD =
                getEnvOrDefault("TEST_STANDARD_PASSWORD", "secret_sauce");

        public static final String LOCKED_USER =
                getEnvOrDefault("TEST_LOCKED_USER", "locked_out_user");

        public static final String PROBLEM_USER =
                getEnvOrDefault("TEST_PROBLEM_USER", "problem_user");

        public static final String INVALID_USER =
                getEnvOrDefault("TEST_INVALID_USER", "invalid_user");

        public static final String INVALID_PASSWORD =
                getEnvOrDefault("TEST_INVALID_PASSWORD", "wrong_password");
    }

    // =========================================================================
    // URL PATHS (relative to base URL)
    // =========================================================================

    public static final class URLPaths {

        private URLPaths() {}

        public static final String LOGIN_PAGE    = "/";
        public static final String PRODUCTS_PAGE = "/inventory.html";
        public static final String CART_PAGE     = "/cart.html";
        public static final String CHECKOUT_PAGE = "/checkout-step-one.html";
    }

    // =========================================================================
    // CSS SELECTORS (shared across page objects and step definitions)
    // =========================================================================

    public static final class Selectors {

        private Selectors() {}

        public static final String PRODUCTS_CONTAINER = ".inventory_container";
        public static final String CART_BADGE         = ".shopping_cart_badge";
        public static final String CART_LINK          = ".shopping_cart_link";
        public static final String ERROR_MESSAGE      = "[data-test='error']";
        public static final String SORT_DROPDOWN      = "[data-test='product_sort_container']";
        public static final String PRODUCT_TITLE      = ".inventory_item_name";
        public static final String PRODUCT_PRICE      = ".inventory_item_price";
        public static final String ADD_TO_CART_BUTTON = "button[data-test*='add-to-cart']";
        public static final String CART_ITEMS         = ".cart_item";
        public static final String BURGER_MENU        = "#react-burger-menu-btn";
        public static final String LOGOUT_LINK        = "#logout_sidebar_link";
    }

    // =========================================================================
    // SORT OPTIONS
    // =========================================================================

    public static final class SortOptions {

        private SortOptions() {}

        public static final String PRICE_LOW_TO_HIGH = "Price (low to high)";
        public static final String PRICE_HIGH_TO_LOW = "Price (high to low)";
        public static final String NAME_A_TO_Z       = "Name (A to Z)";
        public static final String NAME_Z_TO_A       = "Name (Z to A)";
    }

    // =========================================================================
    // ASSERTION MESSAGES
    // =========================================================================

    public static final class AssertionMessages {

        private AssertionMessages() {}

        public static final String WRONG_PAGE           = "User is on wrong page";
        public static final String CART_COUNT_MISMATCH  = "Cart item count mismatch";
        public static final String ERROR_NOT_DISPLAYED  = "Error message was not displayed";
        public static final String LOGIN_FAILED         = "Login did not complete successfully";
        public static final String ITEM_NOT_IN_CART     = "Expected item was not found in cart";
        public static final String SORT_ORDER_WRONG     = "Product sort order is incorrect";
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private static String getEnvOrDefault(String envKey, String defaultValue) {
        String value = System.getenv(envKey);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
