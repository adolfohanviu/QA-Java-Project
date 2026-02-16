package com.qa.stepdefs;

import com.qa.api.APIClient;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;

/**
 * APIStepDefinitions - Step definitions for API testing scenarios
 */
public class APIStepDefinitions {
    private APIClient apiClient;
    private Response response;
    private Map<String, Object> requestBody;

    @Given("User initializes API client")
    public void user_initializes_api_client() {
        apiClient = new APIClient();
    }

    @When("I make a GET request to {string}")
    public void i_make_get_request(String endpoint) {
        apiClient = new APIClient();
        response = apiClient.get(endpoint);
    }

    @When("I make a POST request to {string} with body:")
    public void i_make_post_request_with_body(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        apiClient = new APIClient();
        requestBody = new HashMap<>(dataTable.asMap());
        apiClient.setBody(requestBody);
        response = apiClient.post(endpoint);
    }

    @When("I make a PUT request to {string} with body:")
    public void i_make_put_request_with_body(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        apiClient = new APIClient();
        requestBody = new HashMap<>(dataTable.asMap());
        apiClient.setBody(requestBody);
        response = apiClient.put(endpoint);
    }

    @When("I make a DELETE request to {string}")
    public void i_make_delete_request(String endpoint) {
        apiClient = new APIClient();
        response = apiClient.delete(endpoint);
    }

    @When("I add header {string} with value {string}")
    public void i_add_header(String headerName, String headerValue) {
        apiClient.addHeader(headerName, headerValue);
    }

    @When("I add query parameter {string} with value {string}")
    public void i_add_query_parameter(String paramName, String paramValue) {
        apiClient.addQueryParam(paramName, paramValue);
    }

    @Then("The response status code should be {int}")
    public void response_status_code_should_be(int expectedStatus) {
        Assert.assertEquals("Response status should be " + expectedStatus, 
                expectedStatus, response.getStatusCode());
    }

    @Then("The response should contain {string}")
    public void response_should_contain(String expectedContent) {
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Response should contain: " + expectedContent, 
                responseBody.contains(expectedContent));
    }

    @Then("The response body should have {string} field")
    public void response_body_should_have_field(String fieldName) {
        String jsonPath = response.jsonPath().getString(fieldName);
        Assert.assertNotNull("Response should have field: " + fieldName, jsonPath);
    }

    @Then("The response body {string} should be {string}")
    public void response_body_field_should_be(String fieldName, String expectedValue) {
        String actualValue = response.jsonPath().getString(fieldName);
        Assert.assertEquals("Field " + fieldName + " should be " + expectedValue, 
                expectedValue, actualValue);
    }
}
