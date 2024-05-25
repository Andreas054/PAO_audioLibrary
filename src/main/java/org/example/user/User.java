package org.example.user;

import org.example.music.Playlist;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String password;
    private UserTypeEnum userTypeEnum;

    public static User currentUser;

    public User(String name, String password, UserTypeEnum userTypeEnum) {
        this.name = name;
        this.password = password;
        this.userTypeEnum = userTypeEnum;
    }

    public User(int id, String name, String password, UserTypeEnum userTypeEnum) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userTypeEnum = userTypeEnum;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserTypeEnum getUserTypeEnum() {
        return userTypeEnum;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", userTypeEnum=" + userTypeEnum +
                '}';
    }
}
