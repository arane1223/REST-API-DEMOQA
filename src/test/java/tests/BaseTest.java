package tests;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import models.LoginBodyModel;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class BaseTest {

    LoginBodyModel
            authCorrectData = new LoginBodyModel("AlexTerrible", "Qwer!1234"),
            authIncorrectData = new LoginBodyModel("AlexTerrible", "Qwer!"),
            emptyData = new LoginBodyModel("", ""),
            newUserData = new LoginBodyModel("newUser", "Qwer!1234");

    List<String> books = List.of(
            "Git Pocket Guide",
            "Learning JavaScript Design Patterns",
            "Designing Evolvable Web APIs with ASP.NET",
            "Speaking JavaScript",
            "You Don't Know JS",
            "Programming JavaScript Applications",
            "Eloquent JavaScript, Second Edition",
            "Understanding ECMAScript 6");

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = System.getProperty("baseUri");
    }
}
