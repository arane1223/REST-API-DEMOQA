package models;

import lombok.Data;

@Data
public class LoginResponseModel {
    String token, expires, status, result;
}
