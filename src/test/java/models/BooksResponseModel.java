package models;

import lombok.Data;

import java.util.List;

@Data
public class BooksResponseModel {
    List<BookResponseModel> books;
}
