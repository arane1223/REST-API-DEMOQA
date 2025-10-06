package tests;

import io.qameta.allure.Owner;
import models.CodeMessageResponseModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpec.*;

@Owner("sergeyglukhov")
@DisplayName("API тесты с данными пользователей на DEMOQA")
public class ApiUserTests extends BaseTest {

    @Test
    @DisplayName("Успешная авторизация и получение токена")
    void successfulLoginWithTokenTest() {
        LoginResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(genTokenSpec)
                        .body(authCorrectData)
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("Success", response.getStatus());
            assertEquals("User authorized successfully.", response.getResult());
        });
    }

    @Test
    @DisplayName("Неуспешная авторизация")
    void unsuccessfulLoginWithTokenTest() {
        LoginResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(genTokenSpec)
                        .body(authIncorrectData)
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("Failed", response.getStatus());
            assertEquals("User authorization failed.", response.getResult());
        });
    }

    @Test
    @DisplayName("Неуспешная авторизация отсутствующего пользователя")
    void userNotFoundTest() {
        CodeMessageResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(authSpec)
                        .body(authIncorrectData)
                        .when()
                        .post()
                        .then()
                        .spec(notFoundResponseSpec)
                        .extract().as(CodeMessageResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("1207", response.getCode());
            assertEquals("User not found!", response.getMessage());
        });
    }

    @Test
    @DisplayName("Неуспешная авторизация с пустыми полями")
    void loginWithEmptyDataTest() {
        CodeMessageResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(authSpec)
                        .body(emptyData)
                        .when()
                        .post()
                        .then()
                        .spec(emptyDataResponseSpec)
                        .extract().as(CodeMessageResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("1200", response.getCode());
            assertEquals("UserName and Password required.", response.getMessage());
        });
    }

    @Test
    @DisplayName("Неуспешная повторная регистрация уже зарегистрированного пользователя")
    void userReRegistrationTest() {
        CodeMessageResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(userSpec)
                        .body(authCorrectData)
                        .when()
                        .post()
                        .then()
                        .spec(reRegDataResponseSpec)
                        .extract().as(CodeMessageResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("1204", response.getCode());
            assertEquals("User exists!", response.getMessage());
        });
    }

    @Test
    @DisplayName("Успешное добавление и удаление нового пользователя")
    void addAndDeleteUserTest() {
        UserResponseModel regResponse = step("Отправить запрос на регистрацию нового пользователя", () ->
                given(userSpec)
                        .body(newUserData)
                        .when()
                        .post()
                        .then()
                        .spec(successfulRegResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Проверить регистрацию нового пользователя", () ->
                assertEquals(newUserData.getUserName(), regResponse.getUsername()));

        LoginResponseModel genTokenResponse = step("Отправить запрос на авторизацию", () ->
                given(genTokenSpec)
                        .body(newUserData)
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Проверить ответ на авторизацию", () -> {
            assertEquals("Success", genTokenResponse.getStatus());
            assertEquals("User authorized successfully.", genTokenResponse.getResult());
        });

        step("Отправить запрос на удаление", () ->
                given(userSpec)
                        .header("Authorization", "Bearer " + genTokenResponse.getToken())
                        .when()
                        .delete(regResponse.getUserID())
                        .then()
                        .spec(deleteUserResponseSpec));

        CodeMessageResponseModel finalResponse = step("Отправить запрос на авторизацию удаленного пользователя", () ->
                given(authSpec)
                        .body(newUserData)
                        .when()
                        .post()
                        .then()
                        .spec(notFoundResponseSpec)
                        .extract()
                        .as(CodeMessageResponseModel.class));

        step("Проверить ответ, что пользователь не найден", () -> {
            assertEquals("1207", finalResponse.getCode());
            assertEquals("User not found!", finalResponse.getMessage());
        });
    }
}
