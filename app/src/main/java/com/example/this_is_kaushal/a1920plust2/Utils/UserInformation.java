package com.example.this_is_kaushal.a1920plust2.Utils;

/**
 * Created by this_is_kaushal on 7/21/2017.
 */

public class UserInformation {

    private String name;
    private String email;
    private String photo;

    public UserInformation(String name, String email, String photo) {
        this.name = name;
        this.email = email;
        this.photo = photo;
    }
    public UserInformation() {

    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
