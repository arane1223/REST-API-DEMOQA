import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@DisplayName("API тесты на DEMOQA")
public class ApiTests extends TestBase {

    @Test
    @DisplayName("Успешная авторизация и получение токена")
    void successfulLoginWithTokenTest() {
        given()
                .body(authCorrectData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success"),
                        "result", is("User authorized successfully."));
    }

    @Test
    @DisplayName("Неуспешная авторизация")
    void unsuccessfulLoginWithTokenTest() {
        given()
                .body(authIncorrectData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Failed"),
                        "result", is("User authorization failed."));
    }

    @Test
    @DisplayName("Неуспешная авторизация отсутствующего пользователя")
    void userNotFoundTest() {
        given()
                .body(authIncorrectData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/Authorized")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body("code", is("1207"),
                        "message", is("User not found!"));
    }

    @Test
    @DisplayName("Неуспешная авторизация с пустыми полями")
    void loginWithEmptyDataTest() {
        given()
                .body(emptyData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/Authorized")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("code", is("1200"),
                        "message", is("UserName and Password required."));
    }

    @Test
    @DisplayName("Неуспешная повторная регистрация уже зарегистрированного пользователя")
    void userReRegistrationTest() {
        given()
                .body(authCorrectData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/User")
                .then()
                .log().status()
                .log().body()
                .statusCode(406)
                .body("code", is("1204"),
                        "message", is("User exists!"));
    }

    @Test
    @DisplayName("Успешное добавление и удаление нового пользователя")
    void addAndDeleteUserTest() {
        Response createResponse = given()
                .body(newUserData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/User")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("username", is(newUser))
                .extract().response();

        userId = createResponse.path("userID");

        Response tokenResponse = given()
                .body(newUserData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success"))
                .extract().response();

        String token = tokenResponse.path("token");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(JSON)
                .log().uri()
                .when()
                .delete("/Account/v1/User/" + userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

    @Test
    @DisplayName("Проверка библиотеки книг по названиям")
    void getUserAccountID() {
        get("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("books.title", hasSize(books.size()),
                        "books.title", hasItems(books.toArray(new String[0])));
    }

    @Test
    @DisplayName("Проверка характеристик книги по ISBN")
    void checkingBookCharacteristicsByIsbnTest() {
        get("/BookStore/v1/Book?ISBN=9781449325862")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("title", is("Git Pocket Guide"),
                        "subTitle", is("A Working Introduction"),
                        "author", is("Richard E. Silverman"),
                        "publish_date", is("2020-06-04T08:48:39.000Z"),
                        "publisher", is("O'Reilly Media"),
                        "pages", is(234));
    }
}
