package com.thevarungupta;

public class UserDaoFactory {
    private static UserDao dao;

    private UserDaoFactory() {
    }

    public  static UserDao getEmployeeDao(){
        if(dao == null){
            dao = new UserDaoImpl();
        }
        return dao;
    }
}
