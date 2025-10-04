import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class TestBase {
    String
            authCorrectData = "{\"userName\": \"AlexTerrible\", \"password\": \"Qwer!1234\"}",
            authIncorrectData = "{\"userName\": \"AlexTerrible\", \"password\": \"Qwer!\"}",
            newUser = "newUser",
            newUserData = "{\"userName\": \""+newUser+"\", \"password\": \"Qwer!1234\"}",
            userId,
            emptyData = "";

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
