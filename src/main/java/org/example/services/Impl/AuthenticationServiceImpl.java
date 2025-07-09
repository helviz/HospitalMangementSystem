package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.AuthenticationDAO;
import org.example.models.entities.User;
import org.example.services.AuthenticationService;
import org.example.services.ServiceException;


@ApplicationScoped
public class AuthenticationServiceImpl extends BaseService implements AuthenticationService {

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
}
