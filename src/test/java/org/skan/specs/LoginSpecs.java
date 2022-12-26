package org.skan.specs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.skan.api.AuthorizationApi;

import static org.skan.api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static org.skan.helpers.CustomApiListener.withCustomTemplates;
import static org.skan.tests.BaseTest.credentialsConfig;

public class LoginSpecs {

    public final static String USERNAME = credentialsConfig.username();
    public final static String PASSWORD = credentialsConfig.password();
    public final static String TOKEN = credentialsConfig.token();

    public static RequestSpecification getRequestSpec() {

        AuthorizationApi authorizationApi = new AuthorizationApi();
        String xsrfToken = authorizationApi.getXsrfToken(TOKEN);
        String authorizationCookie = authorizationApi
                .getAuthorizationCookie(TOKEN, USERNAME, PASSWORD);

        return RestAssured
                .given()
                .log().all()
                .filter(withCustomTemplates())
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authorizationCookie)
                .contentType(ContentType.JSON);

    }

}
