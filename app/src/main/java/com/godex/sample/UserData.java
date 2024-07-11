package com.godex.sample;

public class UserData {
    private String password;
    private String confirm_password;
    private String email;
    private String phone;

    public UserData(String password, String confirm_password, String email, String phone) {
        this.password = password;
        this.confirm_password = confirm_password;
        this.email = email;
        this.phone = phone;
    }

    // Getters and setters for all fields
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
