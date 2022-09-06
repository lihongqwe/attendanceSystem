package com.attendance.domain;

public class Register {
    private  String username;
    private String password;
    private String determinePassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeterminePassword() {
        return determinePassword;
    }

    public void setDeterminePassword(String determinePassword) {
        this.determinePassword = determinePassword;
    }
}
