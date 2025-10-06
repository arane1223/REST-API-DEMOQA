package tests;

import io.restassured.RestAssured;
import models.LoginBodyModel;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class BaseTest {
    String
//            authIncorrectData = "{\"userName\": \"AlexTerrible\", \"password\": \"Qwer!\"}",
            newUser = "newUser",
            newUserData = "{\"userName\": \"" + newUser + "\", \"password\": \"Qwer!1234\"}",
            userId,
            emptyData = "";

    LoginBodyModel
            authCorrectData = new LoginBodyModel("AlexTerrible","Qwer!1234"),
            authIncorrectData = new LoginBodyModel("AlexTerrible","Qwer!");

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
        RestAssured.baseURI = "https://demoqa.com/";
    }
}
