package com.qa.api;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.utils.ConfigManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * APIClient — fluent builder for REST-Assured HTTP operations.
 *
 * Supports GET, POST, PUT, PATCH, DELETE with header, body, path-param,
 * and query-param configuration. All public methods return {@code this}
 * for chaining except the terminal HTTP methods which return a Response.
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

    // -------------------------------------------------------------------------
    // Builder methods
    // -------------------------------------------------------------------------

    public APIClient addHeader(String key, String value) {
        requestSpec = requestSpec.header(key, value);
        logger.debug("Added header: {}", key);
        return this;
    }

    public APIClient addHeaders(Map<String, String> headers) {
        requestSpec = requestSpec.headers(headers);
        logger.debug("Added {} headers", headers.size());
        return this;
    }

    public APIClient setBody(Object body) {
        requestSpec = requestSpec.body(body);
        logger.debug("Request body set");
        return this;
    }

    public APIClient addPathParam(String key, Object value) {
        requestSpec = requestSpec.pathParam(key, value);
        logger.debug("Added path param: {}={}", key, value);
        return this;
    }

    public APIClient addQueryParam(String key, Object value) {
        requestSpec = requestSpec.queryParam(key, value);
        logger.debug("Added query param: {}={}", key, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Terminal HTTP methods
    // -------------------------------------------------------------------------

    public Response get(String endpoint) {
        logger.info("GET {}", endpoint);
        Response response = requestSpec.get(endpoint);
        logResponse(response);
        return response;
    }

    public Response post(String endpoint) {
        logger.info("POST {}", endpoint);
        Response response = requestSpec.post(endpoint);
        logResponse(response);
        return response;
    }

    public Response put(String endpoint) {
        logger.info("PUT {}", endpoint);
        Response response = requestSpec.put(endpoint);
        logResponse(response);
        return response;
    }

    public Response patch(String endpoint) {
        logger.info("PATCH {}", endpoint);
        Response response = requestSpec.patch(endpoint);
        logResponse(response);
        return response;
    }

    public Response delete(String endpoint) {
        logger.info("DELETE {}", endpoint);
        Response response = requestSpec.delete(endpoint);
        logResponse(response);
        return response;
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private void logResponse(Response response) {
        logger.info("Response status: {}", response.getStatusCode());
        logger.debug("Response body: {}", response.getBody().asString());
    }
}
