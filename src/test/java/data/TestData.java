package data;

import models.LoginBodyModel;

import java.util.List;

public class TestData {
    public static final LoginBodyModel
            AUTH_CORRECT_DATA = new LoginBodyModel("AlexTerrible", "Qwer!1234"),
            AUTH_INCORRECT_DATA = new LoginBodyModel("AlexTerrible", "Qwer!"),
            EMPTY_DATA = new LoginBodyModel("", ""),
            NEW_USER_DATA = new LoginBodyModel("newUser", "Qwer!1234");

    public static final List<String> BOOKS = List.of(
            "Git Pocket Guide",
            "Learning JavaScript Design Patterns",
            "Designing Evolvable Web APIs with ASP.NET",
            "Speaking JavaScript",
            "You Don't Know JS",
            "Programming JavaScript Applications",
            "Eloquent JavaScript, Second Edition",
            "Understanding ECMAScript 6");

    public static final String
            GIT_BOOK_ISBN = "?ISBN=9781449325862",
            GIT_BOOK_TITLE = "Git Pocket Guide",
            GIT_BOOK_SUB_TITLE = "A Working Introduction",
            GIT_BOOK_AUTHOR = "Richard E. Silverman",
            GIT_BOOK_PUBLISH_DATE = "2020-06-04T08:48:39.000Z",
            GIT_BOOK_PUBLISHER = "O'Reilly Media",
            INCORRECT_ISBN = "?ISBN=978144932586";

    public static final int GIT_BOOK_PAGES = 234;
}
