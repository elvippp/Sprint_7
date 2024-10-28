import io.qameta.allure.Step;
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
        response.then().statusCode(200);
    }
}
