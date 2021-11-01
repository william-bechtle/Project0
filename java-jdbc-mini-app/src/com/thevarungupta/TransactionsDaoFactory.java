package com.thevarungupta;

public class TransactionsDaoFactory {
    private static TransactionsDao dao;

    private TransactionsDaoFactory() {
    }

    public  static TransactionsDao getEmployeeDao(){
        if(dao == null){
            dao = new TransactionsDaoImpl();
        }
        return dao;
    }
}