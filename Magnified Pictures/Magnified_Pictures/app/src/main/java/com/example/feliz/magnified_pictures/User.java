package com.example.feliz.magnified_pictures;

/**
 * Created by Feliz on 2018/01/31.
 */

public class User
{


    private int id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phone;


    public User(int id, String name, String surname, String password, String email, String phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}
