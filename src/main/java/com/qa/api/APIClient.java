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
 * Implements builder pattern for fluent API request construction
 */
public class APIClient {
    private static final Logger logger = LogManager.getLogger(APIClient.class);
    private static final String BASE_URL = ConfigManager.getAPIBaseURL();

    private RequestSpecification requestSpec;

    /**
     * Initialize APIClient with base configuration
     * Sets up RestAssured with JSON content type
     */
    public APIClient() {
        requestSpec = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    /**
     * Add single header to API request
     * Enables fluent API chaining (.addHeader(...).addHeader(...))
     * 
     * @param key Header name (e.g., "Authorization", "X-API-Key")
     * @param value Header value (e.g., "Bearer token123")
     * @return This APIClient instance for method chaining
     */
    public APIClient addHeader(String key, String value) {
        requestSpec = requestSpec.header(key, value);
        logger.debug(String.format("Added header: %s", key));
        return this;
    }

    /**
     * Add multiple headers to API request in bulk
     * 
     * @param headers Map of header names to values
     * @return This APIClient instance for method chaining
     */
    public APIClient addHeaders(java.util.Map<String, String> headers) {
        requestSpec = requestSpec.headers(headers);
        logger.debug(String.format("Added %d headers", headers.size()));
        return this;
    }

    /**
     * Set request body as JSON
     * Automatically serialized to JSON format
     * 
     * @param body Object to send as request body (will be serialized to JSON)
     * @return This APIClient instance for method chaining
     */
    public APIClient setBody(Object body) {
        requestSpec = requestSpec.body(body);
        logger.debug("Request body set");
        return this;
    }

    /**
     * Add path parameter to request URL
     * Example: /users/{id} with pathParam("id", 123)
     * 
     * @param key Parameter name (without braces)
     * @param value Parameter value
     * @return This APIClient instance for method chaining
     */
    public APIClient addPathParam(String key, Object value) {
        requestSpec = requestSpec.pathParam(key, value);
        logger.debug(String.format("Added path parameter: %s=%s", key, value));
        return this;
    }

    /**
     * Add query parameter to request URL
     * Example: /users?status=active with queryParam("status", "active")
     * 
     * @param key Parameter name
     * @param value Parameter value
     * @return This APIClient instance for method chaining
     */
    public APIClient addQueryParam(String key, Object value) {
        requestSpec = requestSpec.queryParam(key, value);
        logger.debug(String.format("Added query parameter: %s=%s", key, value));
        return this;
    }

    /**
     * Perform HTTP GET request
     * 
     * @param endpoint API endpoint path (relative to base URL)
     * @return Response object containing status code, body, headers
     * @throws RuntimeException if request fails
     */
    public Response get(String endpoint) {
        return performRequest("GET", endpoint, null);
    }

    /**
     * Perform HTTP POST request
     * Body should be set before calling this method
     * 
     * @param endpoint API endpoint path (relative to base URL)
     * @return Response object containing status code, body, headers
     * @throws RuntimeException if request fails
     */
    public Response post(String endpoint) {
        return performRequest("POST", endpoint, null);
    }

    /**
     * Perform HTTP PUT request
     * Body should be set before calling this method
     * 
     * @param endpoint API endpoint path (relative to base URL)
     * @return Response object containing status code, body, headers
     * @throws RuntimeException if request fails
     */
    public Response put(String endpoint) {
        return performRequest("PUT", endpoint, null);
    }

    /**
     * Perform HTTP DELETE request
     * 
     * @param endpoint API endpoint path (relative to base URL)
     * @return Response object containing status code, body, headers
     * @throws RuntimeException if request fails
     */
    public Response delete(String endpoint) {
        return performRequest("DELETE", endpoint, null);
    }

    /**
     * Perform HTTP PATCH request
     * Body should be set before calling this method
     * 
     * @param endpoint API endpoint path (relative to base URL)
     * @return Response object containing status code, body, headers
     * @throws RuntimeException if request fails
     */
    public Response patch(String endpoint) {
        return performRequest("PATCH", endpoint, null);
    }

    /**
     * Generic method to perform HTTP requests
     * Provides centralized error handling and logging
     * 
     * @param method HTTP method (GET, POST, PUT, DELETE, PATCH)
     * @param endpoint API endpoint path
     * @param body Request body (not used in current implementation)
     * @return Response object
     * @throws RuntimeException if request fails or unsupported method
     */
    private Response performRequest(String method, String endpoint, Object body) {
        try {
            logger.debug(String.format("Sending %s request to: %s", method, endpoint));
            
            Response response = switch (method.toUpperCase()) {
                case "GET" -> requestSpec.get(endpoint);
                case "POST" -> requestSpec.post(endpoint);
                case "PUT" -> requestSpec.put(endpoint);
                case "DELETE" -> requestSpec.delete(endpoint);
                case "PATCH" -> requestSpec.patch(endpoint);
                default -> throw new IllegalArgumentException(String.format("Unsupported HTTP method: %s", method));
            };

            logger.info(String.format("%s request to %s completed - Status: %d", 
                    method, endpoint, response.getStatusCode()));
            return response;
        } catch (Exception e) {
            logger.error(String.format("API request failed: %s %s", method, endpoint), e);
            throw new RuntimeException(
                    String.format("API request failed for %s %s: %s", method, endpoint, e.getMessage()), e);
        }
    }

    /**
     * Reset request specification for next request
     * Clears headers, parameters, and body
     * Useful when reusing same APIClient instance for multiple requests
     */
    public void reset() {
        requestSpec = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        logger.debug("Request specification reset");
    }
}