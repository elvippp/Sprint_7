import Models.Courier;
import Models.Login;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Check status code of valid login courier")
    public void LoginCourier() {
        String loginName = "likaaaap";
        String logonPassword = "123333";
        String loginFirstName = "liksp";
        Courier courier = new Courier(loginName, logonPassword, loginFirstName);
        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat().statusCode(201);


        Login login = new Login(loginName, logonPassword);

        Response response1 =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");

        response1.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @Step("Check status code of invalid login courier")
    public void invalidLoginCourier() {
        Login login = new Login("liio", "1234");

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat().statusCode(404);
    }

    @Test
    @Step("Check status code of empty field login courier")
    public void emptyFieldLoginCourier() {
        Login login = new Login(null, "1223");

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().assertThat().statusCode(400);
    }
}