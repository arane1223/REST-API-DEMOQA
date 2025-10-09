package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import models.CodeMessageResponseModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpec.*;

@Owner("sergeyglukhov")
@Tag("api")
@Tag("account")
@Feature("Работа с данными пользователей")
@DisplayName("API тесты с данными пользователей на DEMOQA")
public class ApiAccountTests extends BaseTest {

    @Test
    @Story("Авторизация и получение токена")
    @DisplayName("Успешная авторизация и получение токена с корректными данными")
    void successfulLoginWithTokenTest() {
        LoginResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(genTokenSpec)
                        .body(AUTH_CORRECT_DATA)
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
    @Story("Авторизация с некорретными данными")
    @DisplayName("Неуспешная авторизация с некорректными данными")
    void unsuccessfulLoginWithTokenTest() {
        LoginResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(genTokenSpec)
                        .body(AUTH_INCORRECT_DATA)
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
    @Story("Авторизация пользователя, которого нет в базе")
    @DisplayName("Неуспешная авторизация отсутствующего пользователя в базе")
    void userNotFoundTest() {
        CodeMessageResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(authSpec)
                        .body(AUTH_INCORRECT_DATA)
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
    @Story("Авторизация с пустыми полями")
    @DisplayName("Неуспешная авторизация с отправкой пустых полей")
    void loginWithEmptyDataTest() {
        CodeMessageResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(authSpec)
                        .body(EMPTY_DATA)
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
    @Story("Повторная регистрация")
    @DisplayName("Неуспешная повторная регистрация уже зарегистрированного пользователя")
    void userReRegistrationTest() {
        CodeMessageResponseModel response = step("Отправить запрос на авторизацию", () ->
                given(userSpec)
                        .body(AUTH_CORRECT_DATA)
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
    @Story("Добавление и удаление")
    @DisplayName("Успешное добавление и удаление нового пользователя")
    void addAndDeleteUserTest() {
        UserResponseModel regResponse = step("Отправить запрос на регистрацию нового пользователя", () ->
                given(userSpec)
                        .body(NEW_USER_DATA)
                        .when()
                        .post()
                        .then()
                        .spec(successfulRegResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Проверить регистрацию нового пользователя", () ->
                assertEquals(NEW_USER_DATA.getUserName(), regResponse.getUsername()));

        LoginResponseModel genTokenResponse = step("Отправить запрос на авторизацию", () ->
                given(genTokenSpec)
                        .body(NEW_USER_DATA)
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
                        .body(NEW_USER_DATA)
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
