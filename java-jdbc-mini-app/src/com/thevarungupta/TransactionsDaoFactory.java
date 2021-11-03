package com.thevarungupta;

public class TransactionsDaoFactory {
    private static TransactionsDao dao;

    private TransactionsDaoFactory() {
    }

    public  static TransactionsDao getTransactionDao(){
        if(dao == null){
            dao = new TransactionsDaoImpl();
        }
        return dao;
    }
}