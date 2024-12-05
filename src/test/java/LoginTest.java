import io.qameta.allure.junit4.DisplayName;
import models.Courier;
import models.Login;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.hamcrest.Matchers.not;

/**
 * Тестирование Логина Курьера
 */
public class LoginTest {

    private int courierId = -1;
    private Helper helper;

    @After
    public void tearDown() {
        // Удаление курьера после каждого теста, если ID был получен
        if (courierId != -1) {
            helper.deleteCourier(courierId);
        }
    }

    @Before
    public void setUp() {
        helper = new Helper();
    }


    /**
     *  Тест на успешную авторизацию курьера
     */
    @Test
    @DisplayName("Check status code of valid login courier")
    public void loginCourier() {

        Courier courier = helper.generateRandomCourier();

        Response response = helper.createCourier(courier);

        // Проверяем код ответа
        assertThat(response.getStatusCode(), is(201));
        // Проверяем, что тело ответа содержит "ok: true"
        assertThat(response.jsonPath().get("ok"), is(true));

        //Создаем модель логина
        Login loginModel = new Login(courier.getLogin(), courier.getPassword());

        // Запрашиваем логин(вход) курьера
        Response loginResponse = helper.loginCourier(loginModel);

        // Проверяем успешный вход
        loginResponse.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);

        courierId = helper.getCourierId(loginModel);

        assertThat(courierId, is(not(-1)));
    }

    /**
     * Тест, что придет ошибка, если неправильно указать логин
     */
    @Test
    @Step("Check status code of invalid login courier")
    public void invalidLoginCourier() {
        Courier courier = helper.generateRandomCourier();

        Response createCourierResponse = helper.createCourier(courier);

        // Проверяем код ответа на создание курьера
        assertThat(createCourierResponse.getStatusCode(), is(201));
        // Проверяем, что тело ответа содержит "ok: true"
        assertThat(createCourierResponse.jsonPath().get("ok"), is(true));

        Login loginModel = new Login("noSuchCourier", courier.getPassword());

        Response invalidLoginresponse = helper.loginCourier(loginModel);

        invalidLoginresponse.then().assertThat().statusCode(404);
        assertThat(invalidLoginresponse.jsonPath().getString("message"),
                is(Helper.NO_SUCH_ACCOUNT));

        courierId = helper.getCourierId(new Login(courier.getLogin(), courier.getPassword()));

        assertThat(courierId, is(not(-1)));
    }

    /**
     * Тест, что придет ошибка, если неправильно указать логин
     */
    @Test
    @Step("Check status code of invalid password courier")
    public void invalidPasswordCourier() {
        Courier courier = helper.generateRandomCourier();

        Response createCourierResponse = helper.createCourier(courier);

        // Проверяем код ответа на создание курьера
        assertThat(createCourierResponse.getStatusCode(), is(201));
        // Проверяем, что тело ответа содержит "ok: true"
        assertThat(createCourierResponse.jsonPath().get("ok"), is(true));

        Login loginModel = new Login(courier.getLogin(), "000000");

        Response invalidLoginresponse = helper.loginCourier(loginModel);

        invalidLoginresponse.then().assertThat().statusCode(404);
        assertThat(invalidLoginresponse.jsonPath().getString("message"),
                is(Helper.NO_SUCH_ACCOUNT));

        courierId = helper.getCourierId(new Login(courier.getLogin(), courier.getPassword()));

        assertThat(courierId, is(not(-1)));
    }

    /**
     * Тест, что вернется ошибка, если какого-то поля нет
     */
    @Test
    @Step("Check status code of empty field login courier")
    public void emptyFieldLoginCourier() {
        Courier courier = helper.generateRandomCourier();

        Response createCourierResponse = helper.createCourier(courier);

        // Проверяем код ответа
        assertThat(createCourierResponse.getStatusCode(), is(201));
        // Проверяем, что тело ответа содержит "ok: true"
        assertThat(createCourierResponse.jsonPath().get("ok"), is(true));

        Login loginModel = new Login(null, courier.getPassword());

        Response response = helper.loginCourier(loginModel);

        response.then().assertThat().statusCode(400);
        assertThat(response.jsonPath().getString("message"),
                is(Helper.LACK_DATA_TO_LOGIN_COURIER));
    }
}