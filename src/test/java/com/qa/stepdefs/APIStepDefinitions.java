package com.qa.stepdefs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.api.APIClient;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * APIStepDefinitions — Cucumber step implementations for API test scenarios.
 */
public class APIStepDefinitions {

    private static final Logger logger = LogManager.getLogger(APIStepDefinitions.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private APIClient apiClient;
    private Response response;
    private Map<String, Object> requestBody;

    // -------------------------------------------------------------------------
    // Given
    // -------------------------------------------------------------------------

    @Given("User initializes API client")
    @Step("Initialize API client")
    public void user_initializes_api_client() {
        apiClient = new APIClient();
        Assert.assertNotNull("API client failed to initialize", apiClient);
        logger.info("API client initialized");
    }

    // -------------------------------------------------------------------------
    // When — HTTP verbs
    // -------------------------------------------------------------------------

    @When("I make a GET request to {string}")
    @Step("GET {endpoint}")
    public void i_make_get_request(String endpoint) {
        ensureClientInitialized();
        try {
            response = apiClient.get(endpoint);
            Assert.assertNotNull("No response from GET " + endpoint, response);
            logger.info("GET {} → {}", endpoint, response.getStatusCode());
        } catch (Exception e) {
            logger.error("GET request failed: {}", endpoint, e);
            throw new RuntimeException("GET request failed for: " + endpoint, e);
        }
    }

    @When("I make a POST request to {string} with body:")
    @Step("POST {endpoint} with body")
    public void i_make_post_request_with_body(String endpoint,
            io.cucumber.datatable.DataTable dataTable) {
        ensureClientInitialized();
        try {
            requestBody = new HashMap<>(dataTable.asMap());
            response = apiClient.setBody(requestBody).post(endpoint);
            Assert.assertNotNull("No response from POST " + endpoint, response);
            logger.info("POST {} → {}", endpoint, response.getStatusCode());
        } catch (Exception e) {
            logger.error("POST request failed: {}", endpoint, e);
            throw new RuntimeException("POST request failed for: " + endpoint, e);
        }
    }

    @When("I make a POST request to {string} with fixture {string}")
    @Step("POST {endpoint} using fixture {fixtureName}")
    public void i_make_post_request_with_fixture(String endpoint, String fixtureName) {
        ensureClientInitialized();
        requestBody = loadFixture(fixtureName);
        response = apiClient.setBody(requestBody).post(endpoint);
        Assert.assertNotNull("No response from POST " + endpoint, response);
        logger.info("POST {} (fixture: {}) → {}", endpoint, fixtureName, response.getStatusCode());
    }

    @When("I make a PUT request to {string} with body:")
    @Step("PUT {endpoint} with body")
    public void i_make_put_request_with_body(String endpoint,
            io.cucumber.datatable.DataTable dataTable) {
        ensureClientInitialized();
        try {
            requestBody = new HashMap<>(dataTable.asMap());
            response = apiClient.setBody(requestBody).put(endpoint);
            Assert.assertNotNull("No response from PUT " + endpoint, response);
            logger.info("PUT {} → {}", endpoint, response.getStatusCode());
        } catch (Exception e) {
            logger.error("PUT request failed: {}", endpoint, e);
            throw new RuntimeException("PUT request failed for: " + endpoint, e);
        }
    }

    @When("I make a DELETE request to {string}")
    @Step("DELETE {endpoint}")
    public void i_make_delete_request(String endpoint) {
        ensureClientInitialized();
        try {
            response = apiClient.delete(endpoint);
            Assert.assertNotNull("No response from DELETE " + endpoint, response);
            logger.info("DELETE {} → {}", endpoint, response.getStatusCode());
        } catch (Exception e) {
            logger.error("DELETE request failed: {}", endpoint, e);
            throw new RuntimeException("DELETE request failed for: " + endpoint, e);
        }
    }

    // -------------------------------------------------------------------------
    // When — header / param configuration
    // -------------------------------------------------------------------------

    @When("I add header {string} with value {string}")
    @Step("Add header {headerName}")
    public void i_add_header(String headerName, String headerValue) {
        ensureClientInitialized();
        apiClient.addHeader(headerName, headerValue);
    }

    @When("I add query parameter {string} with value {string}")
    @Step("Add query parameter {paramName}")
    public void i_add_query_parameter(String paramName, String paramValue) {
        ensureClientInitialized();
        apiClient.addQueryParam(paramName, paramValue);
    }

    // -------------------------------------------------------------------------
    // Then — assertions
    // -------------------------------------------------------------------------

    @Then("The response status code should be {int}")
    @Step("Verify response status code is {expectedStatus}")
    public void response_status_code_should_be(int expectedStatus) {
        Assert.assertNotNull("No response to assert; was the HTTP step executed?", response);
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(
                "Expected status " + expectedStatus + " but got " + actualStatus
                        + ". Body: " + response.getBody().asString(),
                expectedStatus, actualStatus);
        logger.info("Verified response status: {}", actualStatus);
    }

    @Then("The response body should not be empty")
    @Step("Verify response body is not empty")
    public void response_body_should_not_be_empty() {
        Assert.assertNotNull("Response is null", response);
        String body = response.getBody().asString();
        Assert.assertNotNull("Response body is null", body);
        Assert.assertFalse("Response body is empty", body.isBlank());
        logger.info("Verified response body is not empty ({} chars)", body.length());
    }

    @Then("The response body field {string} should not be null")
    @Step("Verify response field {field} is not null")
    public void response_field_should_not_be_null(String field) {
        Assert.assertNotNull("Response is null", response);
        Object value = response.jsonPath().get(field);
        Assert.assertNotNull("Response body field '" + field + "' should not be null", value);
        logger.info("Verified response field '{}' = {}", field, value);
    }

    @Then("The response body field {string} should equal {string}")
    @Step("Verify response field {field} equals {expectedValue}")
    public void response_field_should_equal(String field, String expectedValue) {
        Assert.assertNotNull("Response is null", response);
        Object actual = response.jsonPath().get(field);
        Assert.assertEquals(
                "Response field '" + field + "' mismatch",
                expectedValue, String.valueOf(actual));
        logger.info("Verified response field '{}' = '{}'", field, actual);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void ensureClientInitialized() {
        if (apiClient == null) {
            apiClient = new APIClient();
            logger.debug("APIClient auto-initialized (no explicit 'User initializes API client' step)");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadFixture(String fixtureName) {
        String resourcePath = "/data/" + fixtureName + ".json";
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Fixture not found on classpath: " + resourcePath);
            }
            return MAPPER.readValue(is, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            logger.error("Failed to load fixture: {}", fixtureName, e);
            throw new RuntimeException("Cannot load fixture: " + fixtureName, e);
        }
    }
}
