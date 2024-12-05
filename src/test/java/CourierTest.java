import io.qameta.allure.junit4.DisplayName;
import models.Courier;
import io.restassured.response.Response;
import models.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Класс тестов курьера
 */
public class CourierTest {

    private int courierId = -1;
    private Helper helper;

    @After
    public void tearDown() {
        // Удаление курьера после каждого теста, если Iн
        if (courierId != -1) {
            helper.deleteCourier(courierId);
        }
    }

    @Before
    public void setUp() {
        helper = new Helper();
    }

    /**
     * Тест на создание курьера
     */
    @Test
    @DisplayName("Check status code of valid create courier")
    public void createCourier() {
        Courier courier = helper.generateRandomCourier();

        Response response = helper.createCourier(courier);

        // Проверяем код ответа
        assertThat(response.getStatusCode(), is(201));
        // Проверяем, что тело ответа содержит "ok: true"
        assertThat(response.jsonPath().get("ok"), is(true));
        // Авторизуемся, чтобы получить ID курьера
        courierId = helper.getCourierId(new Login(courier.getLogin(), courier.getPassword())); // Сохраняем ID курьера
        // Проверяем ID курьера получен корректно
        assertThat(courierId, is(not(-1))); // Убедитесь, что ID не -1
    }


    /**
     *  Тест на ошибку при создании двух одинаковых курьеров и с созданным уже логином ранее
     */
    @Test
    @DisplayName("Check the status code for creating identical couriers")
    public void duplicateCourier() {
        Courier courier = helper.generateRandomCourier();

        Response firstCourierresponse = helper.createCourier(courier);
        // Проверяем код ответа первого запроса
        assertThat(firstCourierresponse.getStatusCode(), is(201));

        Response duplicateCourierResponse = helper.createCourier(courier);

        // Код ответа второго запроса
        assertThat(duplicateCourierResponse.getStatusCode(), is(409)); // Ожидаем ошибку 409


        // Проверяем правильное сообщение об ошибке
        assertThat(duplicateCourierResponse.jsonPath().getString("message"),
                is(Helper.SAME_LOGIN_ERROR_MESSAGE));
        courierId = helper.getCourierId(new Login(courier.getLogin(), courier.getPassword()));
        assertThat(courierId, is(not(-1)));
    }

    // Тест с возвращаем ошибки, если одного из полей нет
    @Test
    @DisplayName("Check the courier creation status code without the login field")
    public void emptyLoginFieldCourier() {
        Courier courier = new Courier(null, "1234", "lil");

        Response response = helper.createCourier(courier);

        response.then().assertThat().statusCode(400);
        assertThat(response.jsonPath().getString("message"),
                is(Helper.LACK_DATA_TO_CREATE_COURIER));
    }

    // Тест с возвращаем ошибки, если одного из полей нет
    @Test
    @DisplayName("Check the courier creation status code without the password field")
    public void emptyPasswordFieldCourier() {
        Courier courier = new Courier("abcd", null, "lil");

        Response response = helper.createCourier(courier);

        response.then().assertThat().statusCode(400);
        assertThat(response.jsonPath().getString("message"),
                is(Helper.LACK_DATA_TO_CREATE_COURIER));
    }
}
