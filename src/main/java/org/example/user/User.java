package org.example.user;

import org.example.music.Playlist;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private UserTypeEnum userTypeEnum;
    private List<Playlist> playlists;

    public User(String name, String password, UserTypeEnum userTypeEnum) {
        this.name = name;
        this.password = password;
        this.userTypeEnum = userTypeEnum;
        this.playlists = new ArrayList<>();
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

    public void setUserTypeEnum(UserTypeEnum userTypeEnum) {
        this.userTypeEnum = userTypeEnum;
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
