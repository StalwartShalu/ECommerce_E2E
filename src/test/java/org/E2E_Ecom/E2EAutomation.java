package org.E2E_Ecom;
import static io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.E2E_Ecom.Resources.loginResource;
import org.testng.annotations.Test;

public class E2EAutomation {
    @Test
    public void login() {

        loginResource lr = new loginResource();
        lr.setUserEmail("iyal1@gmail.com");
        lr.setUserPassword("Shalini15");

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type","application/json")
                .build();
        ResponseSpecification res = new ResponseSpecBuilder().expectContentType("application/json")
                .expectStatusCode(200)
                .build();

        String loginResponse = given().spec(req).body(lr)
                .when().post("/api/ecom/auth/login")
                .then().assertThat().log().body().extract().asString();

        JsonPath jsp = new JsonPath(loginResponse);
        jsp.getString("userId");
        jsp.getString("token");
        System.out.println(jsp.getString("message"));
    }
}
