package tests;

import io.restassured.response.Response;
import models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("API тесты с данными пользователей на DEMOQA")
public class ApiUserTests extends BaseTest {

    @Test
    @DisplayName("Успешная авторизация и получение токена")
    void successfulLoginWithTokenTest() {
        LoginResponseModel response = given()
                .body(authCorrectData)
                .contentType(JSON)
                .log().uri()
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
                .body(authIncorrectData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200).extract()
                .as(LoginResponseModel.class);

        assertEquals("Failed", response.getStatus());
        assertEquals("User authorization failed.", response.getResult());
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
}
