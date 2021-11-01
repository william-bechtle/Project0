package com.thevarungupta;

public class AccountsDaoFactory {
    private static AccountsDao dao;

    private AccountsDaoFactory() {
    }

    public  static AccountsDao getAccountsDao(){
        if(dao == null){
            dao = new AccountsDaoImpl() {
            };
        }
        return dao;
    }
}