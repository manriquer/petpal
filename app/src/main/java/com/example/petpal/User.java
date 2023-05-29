package com.example.petpal;

public class User {
    private String userName;

    public User() {
        // Constructor vacío requerido para Firebase Realtime Database
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
