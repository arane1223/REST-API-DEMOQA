package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static data.TestData.*;
import static data.TestData.GIT_BOOK_AUTHOR;
import static data.TestData.GIT_BOOK_PAGES;
import static data.TestData.GIT_BOOK_PUBLISHER;
import static data.TestData.GIT_BOOK_PUBLISH_DATE;
import static data.TestData.GIT_BOOK_SUB_TITLE;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BookStoreSpec.*;

@Owner("sergeyglukhov")
@Tag("api")
@Tag("bookStore")
@Feature("Работа с данными книг")
@DisplayName("API тесты с данными книг на DEMOQA используя Groovy")
public class ApiBookStoreGroovyTests extends TestBase {

    @Test
    @Story("Проверка по названию")
    @DisplayName("Проверка библиотеки книг по названиям используя Groovy")
    void checkingBookListWithGroovyTest() {
        Response response = step("Отправить запрос на получение списка книг", () ->
                given(getBooksSpec)
                        .get()
                        .then()
                        .spec(booksResponseSpec)
                        .extract().response());

        step("Проверить ответ, сравнить полученный список книг с заданным списком", () -> {
            List<String> actualTitles = response.path("books.title");
            assertEquals(BOOKS, actualTitles);
        });
    }

    @Test
    @Story("Проверка данных книги")
    @DisplayName("Проверка характеристик книги по ISBN с Groovy")
    void checkingBookCharacteristicsByIsbnWithGroovyTest() {
        Response response = step("Отправить запрос на получение книги «Git Pocket Guide»", () ->
                given(getBookSpec)
                        .get(GIT_BOOK_ISBN)
                        .then()
                        .spec(booksResponseSpec)
                        .extract().response());

        step("Проверить характеристики книги", () -> {
            assertEquals(GIT_BOOK_TITLE, response.path("title"));
            assertEquals(GIT_BOOK_SUB_TITLE, response.path("subTitle"));
            assertEquals(GIT_BOOK_AUTHOR, response.path("author"));
            assertEquals(GIT_BOOK_PUBLISH_DATE, response.path("publish_date"));
            assertEquals(GIT_BOOK_PUBLISHER, response.path("publisher"));
            assertEquals(GIT_BOOK_PAGES, (int) response.path("pages"));
        });
    }

    @Test
    @Story("Если книги нет в базе")
    @DisplayName("Проверка отсутствия книги по ISBN с Groovy")
    void checkingBookNotFoundByIsbnTest() {
        Response response = step("Отправить запрос на получение книги", () ->
                given(getBookSpec)
                        .get(INCORRECT_ISBN)
                        .then()
                        .spec(bookNotFoundResponseSpec)
                        .extract().response());

        step("Проверить ответ", () -> {
            assertEquals("1205", response.path("code"));
            assertEquals("ISBN supplied is not available in Books Collection!", response.path("message"));
        });
    }
}
