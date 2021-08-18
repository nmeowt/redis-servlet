package org.nmt.service;

import org.nmt.dao.UserDAO;
import org.nmt.model.User;

import java.util.List;

public class UserService {
    private static final UserDAO userDAO = new UserDAO();

    public User addUser(User user){
        return userDAO.create(user);
    }

    public List<User> list(){
        return userDAO.list();
    }
}
