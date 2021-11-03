package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDao {
    User checkLogin(String username, String password) throws SQLException;

    void addUser(User employee) throws SQLException;

    void updateAccountPassword(User user, String password) throws SQLException;

    void deleteUser(int id) throws SQLException;

    void updateAccountType(int id) throws SQLException;

    User getUserById(int id) throws SQLException;
}

