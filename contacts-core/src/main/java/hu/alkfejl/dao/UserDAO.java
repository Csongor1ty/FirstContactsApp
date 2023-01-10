package hu.alkfejl.dao;

import hu.alkfejl.model.User;

public interface UserDAO {

    User getUserById(int id);
    User save(User user);
    User login(String username, String password);
}
