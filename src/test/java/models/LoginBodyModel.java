package models;

import lombok.Data;

@Data
public class LoginBodyModel {

    String userName, password;

    public LoginBodyModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
