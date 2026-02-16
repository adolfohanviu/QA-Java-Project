package com.qa.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.utils.ConfigManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * APIClient provides methods for API testing
 * Supports GET, POST, PUT, DELETE, PATCH operations
 */
public class APIClient {
    private static final Logger logger = LogManager.getLogger(APIClient.class);
    private static final String BASE_URL = ConfigManager.getAPIBaseURL();

    private RequestSpecification requestSpec;

    public APIClient() {
        requestSpec = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    /**
     * Add header to request
     */
    public APIClient addHeader(String key, String value) {
        requestSpec = requestSpec.header(key, value);
        return this;
    }

    /**
     * Add multiple headers
     */
    public APIClient addHeaders(java.util.Map<String, String> headers) {
        requestSpec = requestSpec.headers(headers);
        return this;
    }

    /**
     * Add request body (JSON)
     */
    public APIClient setBody(Object body) {
        requestSpec = requestSpec.body(body);
        return this;
    }

    /**
     * Add path parameter
     */
    public APIClient addPathParam(String key, Object value) {
        requestSpec = requestSpec.pathParam(key, value);
        return this;
    }

    /**
     * Add query parameter
     */
    public APIClient addQueryParam(String key, Object value) {
        requestSpec = requestSpec.queryParam(key, value);
        return this;
    }

    /**
     * Perform GET request
     */
    public Response get(String endpoint) {
        return performRequest("GET", endpoint, null);
    }

    /**
     * Perform POST request
     */
    public Response post(String endpoint) {
        return performRequest("POST", endpoint, null);
    }

    /**
     * Perform PUT request
     */
    public Response put(String endpoint) {
        return performRequest("PUT", endpoint, null);
    }

    /**
     * Perform DELETE request
     */
    public Response delete(String endpoint) {
        return performRequest("DELETE", endpoint, null);
    }

    /**
     * Perform PATCH request
     */
    public Response patch(String endpoint) {
        return performRequest("PATCH", endpoint, null);
    }

    /**
     * Generic method to perform requests
     */
    private Response performRequest(String method, String endpoint, Object body) {
        try {
            Response response = switch (method.toUpperCase()) {
                case "GET" -> requestSpec.get(endpoint);
                case "POST" -> requestSpec.post(endpoint);
                case "PUT" -> requestSpec.put(endpoint);
                case "DELETE" -> requestSpec.delete(endpoint);
                case "PATCH" -> requestSpec.patch(endpoint);
                default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            };

            logger.info(method + " request to " + endpoint + " - Status: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            logger.error("API request failed: " + method + " " + endpoint, e);
            throw new RuntimeException("API request failed", e);
        }
    }

    /**
     * Reset request specification for next request
     */
    public void reset() {
        requestSpec = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
