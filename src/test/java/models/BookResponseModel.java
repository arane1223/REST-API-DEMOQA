package models;

import lombok.Data;

@Data
public class BookResponseModel {
    String isbn, title, subTitle, author, publish_date, publisher, description, website;
    int pages;
}