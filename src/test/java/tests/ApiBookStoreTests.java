package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import models.BookResponseModel;
import models.BooksResponseModel;
import models.CodeMessageResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static data.TestData.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BookStoreSpec.*;

@Owner("sergeyglukhov")
@Tag("api")
@Tag("bookStore")
@Feature("Работа с данными книг")
@DisplayName("API тесты с данными книг на DEMOQA")
public class ApiBookStoreTests extends BaseTest {

    @Test
    @Story("Проверка по названию")
    @DisplayName("Проверка библиотеки книг по названиям")
    void getUserAccountID() {
        BooksResponseModel response = step("Отправить запрос на получение списка книг", () ->
                given(getBooksSpec)
                        .get()
                        .then()
                        .spec(booksResponseSpec)
                        .extract().as(BooksResponseModel.class));

        step("Проверить ответ, сравнить полученный список книг с заданным списком", () -> {
            List<String> actualTitles = response.getBooks()
                    .stream()
                    .map(BookResponseModel::getTitle)
                    .collect(Collectors.toList());

            assertEquals(BOOKS.size(), response.getBooks().size());
            assertEquals(BOOKS, actualTitles);
        });
    }

    @Test
    @Story("Проверка данных книги")
    @DisplayName("Проверка характеристик книги по ISBN")
    void checkingBookCharacteristicsByIsbnTest() {
        BookResponseModel response = step("Отправить запрос на получение книги «Git Pocket Guide»", () ->
                given(getBookSpec)
                        .get(GIT_BOOK_ISBN)
                        .then()
                        .spec(booksResponseSpec)
                        .extract().as(BookResponseModel.class));

        step("Проверить характеристики книги", () -> {
            assertEquals(GIT_BOOK_TITLE, response.getTitle());
            assertEquals(GIT_BOOK_SUB_TITLE, response.getSubTitle());
            assertEquals(GIT_BOOK_AUTHOR, response.getAuthor());
            assertEquals(GIT_BOOK_PUBLISH_DATE, response.getPublish_date());
            assertEquals(GIT_BOOK_PUBLISHER, response.getPublisher());
            assertEquals(GIT_BOOK_PAGES, response.getPages());
        });
    }

    @Test
    @Story("Если книги нет в базе")
    @DisplayName("Проверка отсутствия книги по ISBN")
    void checkingBookNotFoundByIsbnTest() {
        CodeMessageResponseModel response = step("Отправить запрос на получение книги", () ->
                given(getBookSpec)
                        .get(INCORRECT_ISBN)
                        .then()
                        .spec(bookNotFoundResponseSpec)
                        .extract().as(CodeMessageResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("1205", response.getCode());
            assertEquals("ISBN supplied is not available in Books Collection!", response.getMessage());
        });
    }
}
