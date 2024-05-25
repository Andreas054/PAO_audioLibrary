package org.example.authentication;

import org.example.database.MySQLDatabase;
import org.example.exceptions.PermissionException;
import org.example.repository.UserRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.List;

public class Session {
    private final UserRepository userRepository;

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

    /**
     * Registers a new user with the given username and password.
     * If the user is already logged in or the username already exists the method will return false.
     * The first user to register will be an admin.
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the user was successfully registered, false otherwise
     */
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
        System.out.println("User registered successfully");
        // auto login after registration
        return this.login(username, password);
    }

    /**
     * Logs in the user with the given username and password.
     * If the user is already logged in or the username or password is invalid the method will return false.
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the user was successfully logged in, false otherwise
     */
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

    /**
     * Logs out the current user.
     * @return true if the user was successfully logged out, false if the user is not logged in
     */
    public boolean logout() {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            System.out.println("You are not logged in");
            return false;
        }
        setCurrentUserAnonymous();
        System.out.println("Successfully logged out.");
        return true;
    }

    /**
     * Promotes the user with the given username to an administrator.
     * If the current user is not an administrator or the user does not exist the method will return false.
     * @param username the username of the user to promote
     * @return true if the user was successfully promoted, false otherwise
     */
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