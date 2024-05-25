package org.example.authentication;

import org.example.database.MySQLDatabase;
import org.example.exceptions.PermissionException;
import org.example.repository.UserRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.List;

public class Session {
    private UserRepository userRepository;

    public Session() {
        setCurrentUserAnonymous();
        this.userRepository = UserRepository.getInstance(MySQLDatabase.getInstance());
    }

    public User getUserByName(String username) {
        return userRepository.getUserByName(username);
    }

    public void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public void setCurrentUserAnonymous() {
        User.currentUser = new User(null, null, UserTypeEnum.ANONYMOUS);
    }

    public boolean register(String username, String password) {
        if (User.currentUser.getUserTypeEnum() != UserTypeEnum.ANONYMOUS) {
            System.out.println("You must logout first!");
            return false;
        }

        List<User> users = userRepository.getAllUsers();

        for (User user : users) {
            if (user.getName().equalsIgnoreCase(username)) {
                System.out.println("User with given username already exists! Please try again!");
                return false;
            }
        }

        User userToAdd;
        if (users.isEmpty()) {
            userToAdd = new User(username, password, UserTypeEnum.ADMIN);
        }
        else {
            userToAdd = new User(username, password, UserTypeEnum.USER);
        }

        userRepository.addUser(userToAdd);
        setCurrentUser(userToAdd); // auto login
        System.out.println("User registered successfully");
        return true;
    }

    public boolean login(String username, String password) {
        if (User.currentUser.getUserTypeEnum() != UserTypeEnum.ANONYMOUS) {
            System.out.println("You must logout first!");
            return false;
        }

        for (User user : userRepository.getAllUsers()) {
            if (user.getName().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                System.out.println("You are now authenticated as " + user.getName());

                setCurrentUser(user);
                return true;
            }
        }

        System.out.println("Username or password is invalid. Please try again!");
        return false;
    }

    public boolean logout() {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            System.out.println("You are not logged in");
            return false;
        }
        setCurrentUserAnonymous();
        System.out.println("Successfully logged out.");
        return true;
    }

    public boolean promote(String username) {
        if (User.currentUser.getUserTypeEnum() != UserTypeEnum.ADMIN) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        for (User userToPromote : userRepository.getAllUsers()) {
            if (userToPromote.getName().equalsIgnoreCase(username)) {
                userRepository.promoteUser(userToPromote);
                System.out.println(userToPromote.getName() + " is now an administrator!");
                return true;
            }
        }

        System.out.println("Specified user does not exist!");
        return false;
    }
}