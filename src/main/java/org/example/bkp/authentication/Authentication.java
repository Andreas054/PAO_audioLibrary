package org.example.bkp.authentication;

import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.List;

public class Authentication {
    private Session session;
    private List<User> users;

    public Authentication(List<User> users) {
        this.users = users;
        this.session = new Session();
    }

    public void register(String username, String password) {
        if (users.isEmpty()) {
            users.add(new User(username, password, UserTypeEnum.ADMIN));
            System.out.println("User 1st ADMIN registered successfully");

            session.setCurrentUser(users.get(0));
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
        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                user.setUserTypeEnum(UserTypeEnum.USER);
                System.out.println("Login successful as " + user.getName());

                session.setCurrentUser(user);
                return;
            }
        }

        throw new IllegalArgumentException("Invalid username or password");
    }

    public void logout(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                user.setUserTypeEnum(UserTypeEnum.ANONYMOUS);

                session.setCurrentUserAnonymous();
                System.out.println("Logout successful");
                return;
            }
        }

        throw new IllegalArgumentException("User not found");
    }

    public void promote(String usernameRequesting, String username) {
        for (User userRequesting : users) {
            if (userRequesting.getName().equals(usernameRequesting) && userRequesting.getUserTypeEnum() == UserTypeEnum.ADMIN) {
                for (User userToPromote : users) {
                    if (userToPromote.getName().equals(username)) {
                        userToPromote.setUserTypeEnum(UserTypeEnum.ADMIN);
                        System.out.println("User promoted to admin");
                        return;
                    }
                }

                throw new IllegalArgumentException("User to promote not found");
            }

            throw new IllegalArgumentException("User is not admin");
        }
    }
}
