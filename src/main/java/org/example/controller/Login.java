package org.example.controller;

import org.example.dao.AuthenticationDAO;
import org.example.models.entities.User;
import org.example.services.PersonelService;
import org.example.services.UserService;
import org.example.views.PersonelView;
import org.example.views.UserView;

public class Login {
    PersonelView view = new PersonelView();
    PersonelService service = new PersonelService();
    UserView userView = new UserView();
    UserService userService = new UserService();

    public User userLogin(){
        String email;
        String password;
        Boolean valid;
        do{
            email = view.getEmail();
            valid = service.isEmailValid(email);
            if(!valid){
                System.out.println("Invalid email: Please enter a valid email.");
            }

        } while(!valid);

        do{
            password = userView.getUserPassword();
            valid = userService.isPasswordValid(password);
            if(valid){
                System.out.println("Invalid password: Enter the user password at least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special char");
            }

        } while(!valid);

        return new AuthenticationDAO().authenticateUser(email, password);
    }
}
