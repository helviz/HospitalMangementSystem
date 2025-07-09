package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.AuthenticationDAO;
import org.example.dao.UserDAO;
import org.example.models.entities.User;
import org.example.enums.Role;
import org.example.services.Impl.BaseService;
import org.example.services.ServiceException;
import org.example.services.UserService;

import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl extends BaseService implements UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private AuthenticationDAO authenticationDAO;

    public User authenticate(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            throw new ServiceException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("Password cannot be null or empty");
        }


        User user = authenticationDAO.authenticateUser(email, password);


        if (user == null) {
            throw new ServiceException("Invalid email or password");
        }

        return user;
    }



    public User registerUser(String email, String password) {
        isEmailValid(email);
        if (password == null || password.trim().length() < 6) {
            throw new ServiceException("Password must be at least 6 characters long");
        }




        User user = new User(password.trim());
        if (!userDAO.saveToDB(user)) {
            throw new ServiceException("Failed to register user");
        }
        return user;
    }


    public User getUserById(Long userId) {
        isIdValid(userId);
        return userDAO.getById(userId)
                .orElseThrow(() -> new ServiceException("User not found"));
    }


    public void updateUserEmail(Long userId, String newEmail) {
        isIdValid(userId);
        isEmailValid(newEmail);
        userDAO.updateEmailField(userId, newEmail.trim().toLowerCase());
    }

    public void updateUserPassword(Long userId, String newPassword) {
        isIdValid(userId);
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new ServiceException("Password must be at least 6 characters long");
        }
        userDAO.updatePasswordField(userId, newPassword.trim());
    }




    public void deleteUser(Long userId) {
        isIdValid(userId);
        userDAO.deleteByID(userId);
    }



}
