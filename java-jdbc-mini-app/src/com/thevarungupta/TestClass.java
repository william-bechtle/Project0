package com.thevarungupta;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class TestClass {
    @Test
    public void test_login() throws SQLException {
        User user = new User("wjbechtle","password","will","b","ejb");
        User user_check;
        UserDao dao = UserDaoFactory.getUserDao();
        String a = "wjbechtle";
        String b = "password";
        user_check = dao.checkLogin(a,b);
        assertEquals(user_check.getUserName(),user.getUserName());

    }
}