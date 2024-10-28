import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import models.Login;
import models.Order;

import java.security.SecureRandom;

import static io.restassured.RestAssured.given;

/**
 * Класс помощник для тестов
 */
public class Helper {

    public static final String COURIER_METHOD = "/api/v1/courier";
    public static final String COURIER_LOGIN_METHOD = "/api/v1/courier/login";
    public static final String ORDER_METHOD = "/api/v1/orders";

    public String SAME_LOGIN_ERROR_MESSAGE = "Этот логин уже используется. Попробуйте другой.";
    public String LACK_DATA_TO_CREATE_COURIER = "Недостаточно данных для создания учетной записи";
    public String NO_SUCH_ACCOUNT = "Учетная запись не найдена";
    public String LACK_DATA_TO_LOGIN_COURIER = "Недостаточно данных для входа";

    public Helper() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    /**
     * Создает модель рандомного курьера
     *
     * @return Модель курьера
     */
    public Courier generateRandomCourier() {
        // You can customize the characters that you want to add into
        // the random strings
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER;
        SecureRandom random = new SecureRandom();
        int length = 5;
        StringBuilder sbLogin = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sbLogin.append(rndChar);
        }

        StringBuilder sbPassword = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(NUMBER.length());
            char rndChar = NUMBER.charAt(rndCharAt);

            sbPassword.append(rndChar);
        }

        StringBuilder sbFirstName = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sbFirstName.append(rndChar);
        }

        return new Courier(sbLogin.toString(), sbPassword.toString(), sbFirstName.toString());
    }

    /**
     * Метод для удаления курьера по его ID
     *
     * @param courierId ID курьера
     */
    @Step("Удаление курьера c ID: {courierId}")
    public void deleteCourier(int courierId) {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete(COURIER_METHOD + "/" + courierId)
                .then()
                .statusCode(200); // Ожидаем успешное удаление курьера
    }

    /**
     * Создание курьера
     *
     * @param courier Модель создаваемого курьера
     * @return Ответ
     */
    @Step("Создание курьера {courier}")
    public Response createCourier(Courier courier) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_METHOD);
    }

    /**
     * Метод вызова создания заказа
     * @param order Модель Заказа
     * @return Ответ
     */
    @Step("Создание заказа")
    public Response createOrder(Order order)
    {
        return  RestAssured.given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post(ORDER_METHOD);
    }

    /**
     * Получение всех заказов
     * @return Ответ
     */
    @Step("Получение списка заказов")
    public Response getOrdersList()
    {
        return RestAssured.given()
                .get(ORDER_METHOD);
    }

    /**
     * Логин курьера
     * @param loginModel модель логина
     * @return Ответ
     */
    @Step("Вход (логин) курьера")
    public Response loginCourier(Login loginModel){
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginModel)
                .when()
                .post(COURIER_LOGIN_METHOD);
    }

    /**
     * Метод для получения ID курьера по логину и паролю
     *
     * @param loginModel Модель запроса для логина
     * @return ID курьера
     */
    public int getCourierId(Login loginModel) {
        Response response = loginCourier(loginModel);

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getInt("id");
        } else {
            return -1; // Если авторизация не удалась
        }
    }
}
