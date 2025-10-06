package tests;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@Owner("sergeyglukhov")
@DisplayName("API тесты с данными книг на DEMOQA")
public class ApiBookTests extends BaseTest {

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

    @Test
    @DisplayName("Проверка отсутствия книги по ISBN")
    void checkingBookNotFoundByIsbnTest() {
        get("/BookStore/v1/Book?ISBN=978144932586")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("code", is("1205"),
                        "message", is("ISBN supplied is not available in Books Collection!"));
    }
}
