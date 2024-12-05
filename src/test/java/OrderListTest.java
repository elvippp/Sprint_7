import io.qameta.allure.Step;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

/**
 * Класс тестирования получения списка заказов
 */
public class OrderListTest {
    private Helper helper;

    @Before
    public void setUp() {
        helper = new Helper();
    }

    @Test
    @Step("Check status code of all order list")
    public void allOrderListTest() {
        Response response = helper.getOrdersList();
        response.then().assertThat()
                .body("orders", hasSize(greaterThan(0)))
                .and()
                .statusCode(200);
    }
}
