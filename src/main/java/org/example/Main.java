package org.example;

import org.example.authentication.Session;
import org.example.database.MySQLDatabase;
import org.example.user.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Session session = new Session();
        session.register("sysadmin", "password1");
        session.logout();
        session.register("user2", "password2");

        session.login("user2", "password2");
        session.promote("user2");
        session.logout();
        session.login("sysadmin", "password1");
        session.logout();


    }
}