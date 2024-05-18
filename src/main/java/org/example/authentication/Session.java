package org.example.authentication;

import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private List<User> users;
    private User currentUser;

    public Session() {
        users = new ArrayList<>();
        currentUser = new User(null, null, UserTypeEnum.ANONYMOUS);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentUserAnonymous() {
        this.currentUser = new User(null, null, UserTypeEnum.ANONYMOUS);
    }

    public void register(String username, String password) {
        if (currentUser.getUserTypeEnum() != UserTypeEnum.ANONYMOUS) {
            System.out.println("You must logout first");
            return;
        }

        if (users.isEmpty()) {
            users.add(new User(username, password, UserTypeEnum.ADMIN));
            System.out.println("User 1st ADMIN registered successfully");

            setCurrentUser(users.get(0)); // auto login
            return;
        }

        for (User user : users) {
            if (user.getName().equals(username)) {
                throw new IllegalArgumentException("User already exists");
            }
        }

        users.add(new User(username, password, UserTypeEnum.USER));
        System.out.println("User registered successfully");
    }

    public void login(String username, String password) {
        if (currentUser.getUserTypeEnum() != UserTypeEnum.ANONYMOUS) {
            System.out.println("You must logout first");
            return;
        }

        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
//                user.setUserTypeEnum(UserTypeEnum.USER);
                System.out.println("Login successful as " + user.getName());

                setCurrentUser(user);
                return;
            }
        }

        throw new IllegalArgumentException("Invalid username or password");
    }

    public void logout() {
//        currentUser.setUserTypeEnum(UserTypeEnum.ANONYMOUS);
        setCurrentUserAnonymous();
        System.out.println("Logout successful");
    }

    public void promote(String username) {
        if (currentUser.getUserTypeEnum() != UserTypeEnum.ADMIN) {
            System.out.println("You must be an admin to promote");
            return;
        }

        for (User userToPromote : users) {
            if (userToPromote.getName().equals(username)) {
                userToPromote.setUserTypeEnum(UserTypeEnum.ADMIN);
                System.out.println("User promoted to admin");
                return;
            }
        }

        throw new IllegalArgumentException("User not found");
    }
}