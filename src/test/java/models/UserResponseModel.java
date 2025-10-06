package models;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseModel {
    String userID, username;
    List<BookResponseModel> books;
}
