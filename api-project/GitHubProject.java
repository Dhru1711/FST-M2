package Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class GitHubProject {

    Map<String, String> reqHeaders = new HashMap<>();

    String baseURI = "https://api.github.com";
    RequestSpecification requestSpec;
    ResponseSpecification  responseSpec;

    int id;

    @BeforeClass
    public void setUp(){
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token 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")
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000l), TimeUnit.MILLISECONDS)
                .build();
    }

    @Test(priority = 1)
    public void postRequest(){
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC7kXSDbzXbUEqMRgzTcqwhIk6J4Ey8h/u3UB2gpxFuSndTAmk1SGovKNnolJatg9GPAmOuTb2M5Xv0GylRjcpR49SRqc8nGBjknfS+34Ag0sT9FIVnSQyilRDqbAYZ9mB96DSTGPD95/bzuW7H+7FEa+N++E1lFuH5bfpGCn92QnJs1r01mbxGyOr/HR4KhSPzvyoIou3y/40Lq8koOcyILajdhevzMN1kspFv8REadK6fXjX7WM22yX954FaXbk3ne9kc7xX2HeimPqVndF/bjo9AbvFAPgmfwDIBZOR1bA4BjScDCdDDxz9wOXMhi4bHOXXYjdSzMhwKWdirZeeV \"}";
        Response res = given().spec(requestSpec).body(reqBody).when().post("user/keys");
        id = res.then().extract().path("id");
        System.out.println(res.getBody().asString());
        res.then().statusCode(201);
    }

    @Test(priority = 2)
    public void getRequest() {

        Response res = given().spec(requestSpec).when().pathParam("keyId",id).get("/user/keys/{keyId}");
        System.out.println(res.getBody().asString());
        res.then().statusCode(200);
    }

    @Test(priority = 3)
    public void deleteRequest() {

        Response res = given().spec(requestSpec).when().pathParam("keyId",id).delete(" /user/keys/{keyId}");
        System.out.println(res.getBody().asString());
        res.then().statusCode(204);
    }

}
