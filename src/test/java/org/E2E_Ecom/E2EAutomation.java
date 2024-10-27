package org.E2E_Ecom;

import static io.restassured.RestAssured.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.E2E_Ecom.Resources.addToCart;
import org.E2E_Ecom.Resources.loginResource;
import org.E2E_Ecom.Resources.product;
import org.testng.annotations.Test;

import java.io.File;

public class E2EAutomation {
    private String token;
    private String userId;
    private String productId;

    //Login
    @Test (priority = 1)
    public void login() {

        loginResource lr = new loginResource();
        lr.setUserEmail("iyal1@gmail.com");
        lr.setUserPassword("Shalini15");

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .build();
        ResponseSpecification res = new ResponseSpecBuilder()
                .expectContentType("application/json")
                .expectStatusCode(200)
                .build();

        String loginResponse = given().spec(req).body(lr)
                .when().post("/api/ecom/auth/login")
                .then().spec(res).assertThat().extract().asString();

        JsonPath jsp = new JsonPath(loginResponse);
        String userId = jsp.getString("userId");
        String token = jsp.getString("token");
        this.token = token;
        this.userId = userId;
        System.out.println(jsp.getString("message"));
    }
    // Create a new product
    @Test(priority = 2)
    public void createProduct() {

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addFormParam("productName", "pullOver")
                .addFormParam("productAddedBy", userId)
                .addFormParam("productCategory", "fashion")
                .addFormParam("productSubCategory", "shirts")
                .addFormParam("productPrice", "1250")
                .addFormParam("productDescription", "Women pullover")
                .addFormParam("productFor", "women")
                .addHeader("Authorization", token)
                .addMultiPart("productImage", new File("C:\\Users\\Shali\\Downloads\\Shopping.jpg"))
                .build();

        ResponseSpecification res = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType("application/json")
                .build();

        String productResponse = given().spec(req).when().post("api/ecom/product/add-product")
                .then().spec(res).extract().asString();

        JsonPath jsp = new JsonPath(productResponse);
        String productId = jsp.getString("productId");
        System.out.println(jsp.getString("message"));
        this.productId = productId;

    }

    //Add created product to cart
    @Test(priority = 3)
    public void addToCart() {

        product p = new product();
        p.set_id(productId);
        p.setProductName("pullOver");
        p.setProductCategory("fashion");
        p.setProductSubCategory("shirts");
        p.setProductPrice("1250");
        p.setProductDescription("Women pullover");
        p.setProductImage("https://rahulshettyacademy.com/api/ecom/uploads/productImage_1730015935268.jpg");
        p.setProductRating("0");
        p.setProductStatus("0");
        p.setProductStatus("true");
        p.setProductFor("women");
        p.setProductAddedBy(userId);
        p.set__v("0");

        addToCart ac = new addToCart();
        ac.set_id(userId);
        ac.setProduct(p);

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        ResponseSpecification res = new ResponseSpecBuilder()
                .expectContentType("application/json")
                .expectStatusCode(200)
                .build();

        String cartResponse = given().spec(req).body(ac).when().post("api/ecom/user/add-to-cart")
                .then().spec(res).extract().asString();

        JsonPath jsp = new JsonPath(cartResponse);
        System.out.println(jsp.getString("message"));
    }

    //Delete product
    @Test(priority = 4)
    public void deleteProduct(){
        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token)
                .addPathParam("productId", productId)
                .build();

        ResponseSpecification res = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        String cartResponse = given().spec(req).when().delete("/api/ecom/product/delete-product/{productId}")
                .then().spec(res).extract().asString();

        JsonPath jsp = new JsonPath(cartResponse);
        System.out.println(jsp.getString("message"));
    }

}
