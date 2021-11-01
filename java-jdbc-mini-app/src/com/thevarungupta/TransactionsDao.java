package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface TransactionsDao {

    void addTransaction(Transactions transaction) throws SQLException;

    void acceptTransaction(int id) throws SQLException;

    void rejectTransaction(int id) throws SQLException;

    ArrayList<Transactions> getUserTransactions(int user_id) throws SQLException;

    ArrayList<Transactions> getTransactions() throws SQLException;


}