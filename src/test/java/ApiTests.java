import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

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
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    @DisplayName("Тест на неуспешную авторизацию")
    void unsuccessfulLoginWithTokenTest() {

        given()
                .body(authUncorrectData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/Account/v1/GenerateToken")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Failed"))
                .body("result", is("User authorization failed."));
    }

    @Test
    @DisplayName("Тест на отсутствие пользователя")
    void userNotFoundTest() {

        given()
                .body(authUncorrectData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/Account/v1/Authorized")

                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body("message", is("User not found!"));
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
                .body("message", is("UserName and Password required."));
    }

    @Test
    @DisplayName("Проверка библиотеки книг по названиям")
    void getUserAccountID() {
        get("/BookStore/v1/Books")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("books.title", hasItems(books.toArray(new String[0])));
    }
}
