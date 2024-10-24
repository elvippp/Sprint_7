import Models.Order;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Check the track of the created order with one color of the scooter")
    public void createOrder() {
        List<String> colors = new ArrayList<>();
        colors.add("BLACK");
        Order order = new Order("ninka", "nika",
                "Mens,12 apt.", "4", "+7 800 355 35 35",
                "5", "2024-09-12", "please wait",
                colors);

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");


        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }

    @Test
    @Step("Check the track of the created order with two color of the scooter")
    public void createTwoColorOrder() {
        List<String> colors = new ArrayList<>();
        colors.add("BLACK");
        colors.add("GREY");
        Order order = new Order("nina", "nik", "Mens,12 apt.", "4",
                "+7 800 355 35 35", "5", "2024-09-12", "please wait",
                colors);

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");


        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }

    @Test
    @Step("Check the track of the created order without the color of the scooter")
    public void createNotColorOrder() {
        List<String> colors = new ArrayList<String>();
        Order order = new Order("nina", "nik", "Mens,12 apt.", "4",
                "+7 800 355 35 35", "5", "2024-09-12", "please wait",
                colors);

        Response response =
                (Response) given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");


        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }

}