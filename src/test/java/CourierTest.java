import Models.Courier;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourierTest {


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Check status code of valid create courier")
    public void createCourier() {
        Courier courier = new Courier("lolp", "12345", "lalp");

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat().statusCode(201);
    }

    @Test
    @Step("Check the status code for creating identical couriers")
    public void duplicateCourier() {
        Courier courier = new Courier("lolp", "12345", "lalp");

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat().statusCode(409);
    }

    @Test
    @Step("Check the courier creation status code without the field")
    public void emptyFieldCourier() {
        Courier courier = new Courier(null, "1234", "lil");

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then().assertThat().statusCode(400);
    }

}
