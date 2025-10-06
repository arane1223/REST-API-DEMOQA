package tests;

import io.qameta.allure.Owner;
import models.BookResponseModel;
import models.BooksResponseModel;
import models.CodeMessageResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BookStoreSpec.*;

@Owner("sergeyglukhov")
@DisplayName("API тесты с данными книг на DEMOQA")
public class ApiBookStoreTests extends BaseTest {

    @Test
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

            assertEquals(books.size(), response.getBooks().size());
            assertEquals(books, actualTitles);
        });
    }

    @Test
    @DisplayName("Проверка характеристик книги по ISBN")
    void checkingBookCharacteristicsByIsbnTest() {
        BookResponseModel response = step("Отправить запрос на получение книги «Git Pocket Guide»", () ->
                given(getBookSpec)
                        .get("?ISBN=9781449325862")
                        .then()
                        .spec(booksResponseSpec)
                        .extract().as(BookResponseModel.class));

        step("Проверить характеристики книги", () -> {
            assertEquals("Git Pocket Guide", response.getTitle());
            assertEquals("A Working Introduction", response.getSubTitle());
            assertEquals("Richard E. Silverman", response.getAuthor());
            assertEquals("2020-06-04T08:48:39.000Z", response.getPublish_date());
            assertEquals("O'Reilly Media", response.getPublisher());
            assertEquals(234, response.getPages());
        });
    }

    @Test
    @DisplayName("Проверка отсутствия книги по ISBN")
    void checkingBookNotFoundByIsbnTest() {
        CodeMessageResponseModel response = step("Отправить запрос на получение книги", () ->
                given(getBookSpec)
                        .get("?ISBN=978144932586")
                        .then()
                        .spec(bookNotFoundResponseSpec)
                        .extract().as(CodeMessageResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("1205", response.getCode());
            assertEquals("ISBN supplied is not available in Books Collection!", response.getMessage());
        });
    }
}
