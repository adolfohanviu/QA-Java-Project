package com.qa.pages;

import com.qa.utils.TestConstants;

import io.qameta.allure.Step;

/**
 * {ClassName}Page — Page Object for {the real SauceDemo screen/flow}.
 *
 * Every selector below must have been verified against the live DOM at
 * https://www.saucedemo.com/ before being added to TestConstants.Selectors.
 */
public class {ClassName}Page extends BasePage {

    // Pull selectors from TestConstants — never inline a literal here.
    private static final String SOME_ELEMENT = TestConstants.Selectors.SOME_ELEMENT;

    @Step("{Human-readable action description}")
    public void someAction() {
        click(SOME_ELEMENT);
        logger.info("Performed someAction on {ClassName}Page");
    }

    @Step("Get {field} text")
    public String getSomeText() {
        return getText(SOME_ELEMENT);
    }

    @Step("Check if {element} is visible")
    public boolean isSomeElementVisible() {
        return isElementVisible(SOME_ELEMENT);
    }
}
