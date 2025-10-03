import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@DisplayName("API тесты на DEMOQA")
public class ApiTests extends TestBase{

    @Test
    @DisplayName("Тест на успешную авторизацию")
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
    @DisplayName("Тест на неуспешную авторизацию")
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
    @DisplayName("Тест на отсутствие пользователя")
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
    @DisplayName("Авторизация с пустыми полями")
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
    @DisplayName("Повторная регистрация уже зарегистрированного пользователя")
    void userReRegistrationTest() {
        given()
                .body(authCorrectData).
                contentType(JSON)
                .log().uri()

                .when()
                .post("/Account/v1/User")

                .then()
                .log().status()
                .log().body()
                .statusCode(406)
                .body("code", is("1204"),
                        "message",is("User exists!"));
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
}
