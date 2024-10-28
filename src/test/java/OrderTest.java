import io.qameta.allure.junit4.DisplayName;
import models.Order;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {

    private Helper helper;

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String deliveryDate;
    private String comment;
    private List<String> colors;
    private int rentTime;


    public OrderTest(String firstName, String lastName, String address, String metroStation,
                     String phone, int rentTime, String deliveryDate,
                     String comment, List<String> colors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][]{
                {"Elvira", "Kek", "KZN, 1 apt.", "22", "+7 800 333 23 23", 3, "2025-07-09", "Call please", new ArrayList<String>(List.of("GRAY"))},
                {"Elvira", "Kek", "KZN, 1 apt.", "22", "+7 800 333 23 23", 3, "2025-07-09", "Call please", new ArrayList<String>(List.of("GRAY", "BLACK"))},
                {"Elvira", "Kek", "KZN, 1 apt.", "22", "+7 800 333 23 23", 3, "2025-07-09", "Call please", new ArrayList<String>()},
                {"Elvira", "Kek", "KZN, 1 apt.", "22", "+7 800 333 23 23", 3, "2025-07-09", "Call please", new ArrayList<String>(List.of("BLACK"))},
        };
    }

    @Before
    public void setUp() {
        helper = new Helper();
    }

    /**
     * Тест на успешное создание заказа с одним цветом/двумя/без цвета
     */
    @Test
    @DisplayName("Check the track of the created order with different input parameters of color  the scooter")
    public void createOrderTest() {
        Order order = new Order(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, colors);

        Response response = helper.createOrder(order);

        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}