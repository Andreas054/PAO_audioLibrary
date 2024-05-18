package org.example.bkp.authentication;

import org.example.user.User;
import org.example.user.UserTypeEnum;

public class Session {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentUserAnonymous() {
        this.currentUser = new User(null, null, UserTypeEnum.ANONYMOUS);
    }
}
