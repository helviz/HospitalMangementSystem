package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.models.entities.Staff;
import org.example.models.entities.User;

import java.io.Serializable;
@Model
public class UserDAO  extends BaseDAO<User> implements Serializable {
    public UserDAO(){
        super(User.class);
    }
}
