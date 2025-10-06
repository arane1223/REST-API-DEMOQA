package tests;

import io.qameta.allure.Owner;
import io.restassured.response.Response;
import models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Owner("sergeyglukhov")
@DisplayName("API тесты с данными пользователей на DEMOQA")
public class ApiUserTests extends BaseTest {

    @Test
    @DisplayName("Успешная авторизация и получение токена")
    void successfulLoginWithTokenTest() {
        LoginResponseModel response = given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .body(authCorrectData)
                .contentType(JSON)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);

        assertEquals("Success", response.getStatus());
        assertEquals("User authorized successfully.", response.getResult());
    }

    @Test
    @DisplayName("Неуспешная авторизация")
    void unsuccessfulLoginWithTokenTest() {
        LoginResponseModel response = given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .body(authIncorrectData)
                .contentType(JSON)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);

        assertEquals("Failed", response.getStatus());
        assertEquals("User authorization failed.", response.getResult());
    }

    @Test
    @DisplayName("Неуспешная авторизация отсутствующего пользователя")
    void userNotFoundTest() {
        given()
                .log().uri()
                .log().body()
                .log().headers()
                .body(authIncorrectData)
                .contentType(JSON)
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
                .log().uri()
                .log().body()
                .log().headers()
                .body(emptyData)
                .contentType(JSON)
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
                .log().uri()
                .log().body()
                .log().headers()
                .body(authCorrectData)
                .contentType(JSON)
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
                .log().uri()
                .log().body()
                .log().headers()
                .body(newUserData)
                .contentType(JSON)
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
                .log().uri()
                .log().body()
                .log().headers()
                .body(newUserData)
                .contentType(JSON)
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
                .log().uri()
                .log().body()
                .log().headers()
                .header("Authorization", "Bearer " + token)
                .contentType(JSON)
                .when()
                .delete("/Account/v1/User/" + userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
