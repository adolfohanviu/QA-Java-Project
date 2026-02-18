package com.qa.stepdefs;

import com.qa.api.APIClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * APIStepDefinitions - Step definitions for API testing scenarios
 * Provides comprehensive API testing with validation and error messages
 */
public class APIStepDefinitions {
    private APIClient apiClient;
    private Response response;
    private Map<String, Object> requestBody;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(APIStepDefinitions.class);

    @Given("User initializes API client")
    @Step("Initialize API client")
    public void user_initializes_api_client() {
        apiClient = new APIClient();
        Assert.assertNotNull("Failed to initialize API client", apiClient);
        logger.info("API client initialized successfully");
    }

    @When("I make a GET request to {string}")
    @Step("Make GET request to {endpoint}")
    public void i_make_get_request(String endpoint) {
        Assert.assertNotNull("API client must be initialized before making requests", apiClient);
        
        try {
            response = apiClient.get(endpoint);
            Assert.assertNotNull(
                    String.format("Failed to get response from endpoint: %s", endpoint),
                    response);
            logger.info(String.format("GET request to %s completed with status: %d", endpoint, response.getStatusCode()));
        } catch (Exception e) {
            logger.error(String.format("Failed to make GET request to %s", endpoint), e);
            throw new RuntimeException(String.format("GET request failed for endpoint: %s", endpoint), e);
        }
    }

    @When("I make a POST request to {string} with body:")
    @Step("Make POST request to {endpoint} with body")
    public void i_make_post_request_with_body(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Assert.assertNotNull("API client must be initialized before making requests", apiClient);
        
        try {
            requestBody = new HashMap<>(dataTable.asMap());
            apiClient.setBody(requestBody);
            response = apiClient.post(endpoint);
            Assert.assertNotNull(
                    String.format("Failed to get response from POST to %s", endpoint),
                    response);
            logger.info(String.format("POST request to %s completed with status: %d", endpoint, response.getStatusCode()));
        } catch (Exception e) {
            logger.error(String.format("Failed to make POST request to %s", endpoint), e);
            throw new RuntimeException(String.format("POST request failed for endpoint: %s", endpoint), e);
        }
    }

    @When("I make a POST request to {string} with fixture {string}")
    @Step("Make POST request to {endpoint} with fixture {fixtureName}")
    public void i_make_post_request_with_fixture(String endpoint, String fixtureName) {
        Assert.assertNotNull("API client must be initialized before making requests", apiClient);
        
        try {
            requestBody = loadFixture(fixtureName);
            apiClient.setBody(requestBody);
            response = apiClient.post(endpoint);
            Assert.assertNotNull(
                    String.format("Failed to get response from POST to %s", endpoint),
                    response);
            logger.info(String.format("POST request to %s with fixture %s completed with status: %d",
                    endpoint, fixtureName, response.getStatusCode()));
        } catch (Exception e) {
            logger.error(String.format("Failed to make POST request to %s with fixture %s", endpoint, fixtureName), e);
            throw new RuntimeException(
                    String.format("POST request failed for endpoint: %s with fixture: %s", endpoint, fixtureName), e);
        }
    }

    @When("I make a PUT request to {string} with body:")
    @Step("Make PUT request to {endpoint} with body")
    public void i_make_put_request_with_body(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Assert.assertNotNull("API client must be initialized before making requests", apiClient);
        
        try {
            requestBody = new HashMap<>(dataTable.asMap());
            apiClient.setBody(requestBody);
            response = apiClient.put(endpoint);
            Assert.assertNotNull(
                    String.format("Failed to get response from PUT to %s", endpoint),
                    response);
            logger.info(String.format("PUT request to %s completed with status: %d", endpoint, response.getStatusCode()));
        } catch (Exception e) {
            logger.error(String.format("Failed to make PUT request to %s", endpoint), e);
            throw new RuntimeException(String.format("PUT request failed for endpoint: %s", endpoint), e);
        }
    }

    @When("I make a PUT request to {string} with fixture {string}")
    @Step("Make PUT request to {endpoint} with fixture {fixtureName}")
    public void i_make_put_request_with_fixture(String endpoint, String fixtureName) {
        Assert.assertNotNull("API client must be initialized before making requests", apiClient);
        
        try {
            requestBody = loadFixture(fixtureName);
            apiClient.setBody(requestBody);
            response = apiClient.put(endpoint);
            Assert.assertNotNull(
                    String.format("Failed to get response from PUT to %s", endpoint),
                    response);
            logger.info(String.format("PUT request to %s with fixture %s completed with status: %d",
                    endpoint, fixtureName, response.getStatusCode()));
        } catch (Exception e) {
            logger.error(String.format("Failed to make PUT request to %s with fixture %s", endpoint, fixtureName), e);
            throw new RuntimeException(
                    String.format("PUT request failed for endpoint: %s with fixture: %s", endpoint, fixtureName), e);
        }
    }

    @When("I make a DELETE request to {string}")
    @Step("Make DELETE request to {endpoint}")
    public void i_make_delete_request(String endpoint) {
        Assert.assertNotNull("API client must be initialized before making requests", apiClient);
        
        try {
            response = apiClient.delete(endpoint);
            Assert.assertNotNull(
                    String.format("Failed to get response from DELETE to %s", endpoint),
                    response);
            logger.info(String.format("DELETE request to %s completed with status: %d", endpoint, response.getStatusCode()));
        } catch (Exception e) {
            logger.error(String.format("Failed to make DELETE request to %s", endpoint), e);
            throw new RuntimeException(String.format("DELETE request failed for endpoint: %s", endpoint), e);
        }
    }

    @When("I add header {string} with value {string}")
    @Step("Add header {headerName}: {headerValue}")
    public void i_add_header(String headerName, String headerValue) {
        Assert.assertNotNull("API client should be initialized", apiClient);
        Assert.assertNotNull(String.format("Header name cannot be null"), headerName);
        Assert.assertNotNull(String.format("Header value cannot be null"), headerValue);
        
        try {
            apiClient.addHeader(headerName, headerValue);
            logger.info(String.format("Added header: %s = %s", headerName, headerValue));
        } catch (Exception e) {
            logger.error(String.format("Failed to add header %s", headerName), e);
            throw new RuntimeException(String.format("Failed to add header: %s", headerName), e);
        }
    }

    @When("I add query parameter {string} with value {string}")
    @Step("Add query parameter {paramName}={paramValue}")
    public void i_add_query_parameter(String paramName, String paramValue) {
        Assert.assertNotNull("API client should be initialized", apiClient);
        Assert.assertNotNull("Query parameter name cannot be null", paramName);
        Assert.assertNotNull("Query parameter value cannot be null", paramValue);
        
        try {
            apiClient.addQueryParam(paramName, paramValue);
            logger.info(String.format("Added query parameter: %s = %s", paramName, paramValue));
        } catch (Exception e) {
            logger.error(String.format("Failed to add query parameter %s", paramName), e);
            throw new RuntimeException(String.format("Failed to add query parameter: %s", paramName), e);
        }
    }

    @Then("The response status code should be {int}")
    @Step("Verify response status code is {expectedStatus}")
    public void response_status_code_should_be(int expectedStatus) {
        Assert.assertNotNull("Response should not be null", response);
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(
                String.format("Expected status code %d but got %d. Response: %s",
                        expectedStatus, actualStatus, response.getBody().asString()),
                expectedStatus,
                actualStatus);
        logger.info(String.format("Status code verification passed: %d", actualStatus));
    }

    @Then("The response should contain {string}")
    @Step("Verify response contains text: {expectedContent}")
    public void response_should_contain(String expectedContent) {
        Assert.assertNotNull("Response should not be null", response);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(
                String.format("Response body should contain '%s' but was: %s", expectedContent, responseBody),
                responseBody.contains(expectedContent));
        logger.info(String.format("Response contains expected text: %s", expectedContent));
    }

    @Then("The response body should have {string} field")
    @Step("Verify response has field: {fieldName}")
    public void response_body_should_have_field(String fieldName) {
        Assert.assertNotNull("Response should not be null", response);
        try {
            String jsonValue = response.jsonPath().getString(fieldName);
            Assert.assertNotNull(
                    String.format("Response should have field '%s'", fieldName),
                    jsonValue);
            logger.info(String.format("Response has field '%s' with value: %s", fieldName, jsonValue));
        } catch (Exception e) {
            logger.error(String.format("Failed to find field '%s' in response", fieldName), e);
            throw new RuntimeException(String.format("Field '%s' not found in response", fieldName), e);
        }
    }

    @Then("The response body {string} should be {string}")
    @Step("Verify response field {fieldName} equals {expectedValue}")
    public void response_body_field_should_be(String fieldName, String expectedValue) {
        Assert.assertNotNull("Response should not be null", response);
        try {
            String actualValue = response.jsonPath().getString(fieldName);
            Assert.assertEquals(
                    String.format("Field '%s' should be '%s' but was '%s'", fieldName, expectedValue, actualValue),
                    expectedValue,
                    actualValue);
            logger.info(String.format("Field '%s' verification passed: %s", fieldName, actualValue));
        } catch (Exception e) {
            logger.error(String.format("Failed to verify field '%s'", fieldName), e);
            throw new RuntimeException(
                    String.format("Failed to verify field '%s' with expected value '%s'", fieldName, expectedValue), e);
        }
    }

    /**
     * Load test data fixture from JSON file
     * @param fixtureName Name of the fixture in api-users.json
     * @return Map containing fixture data
     */
    private Map<String, Object> loadFixture(String fixtureName) {
        try (InputStream stream = getClass().getResourceAsStream("/data/api-users.json")) {
            Assert.assertNotNull(
                    "Fixture file '/data/api-users.json' not found in resources",
                    stream);

            Map<String, Map<String, Object>> payloads = mapper.readValue(
                    stream, new TypeReference<Map<String, Map<String, Object>>>() {}
            );

            Map<String, Object> fixture = payloads.get(fixtureName);
            Assert.assertNotNull(
                    String.format("Fixture '%s' not found in api-users.json. Available fixtures: %s",
                            fixtureName, payloads.keySet()),
                    fixture);

            logger.info(String.format("Loaded fixture '%s' successfully", fixtureName));
            return new HashMap<>(fixture);
        } catch (Exception e) {
            logger.error(String.format("Failed to load fixture: %s", fixtureName), e);
            throw new RuntimeException(
                    String.format("Failed to load fixture '%s': %s", fixtureName, e.getMessage()), e);
        }
    }
}