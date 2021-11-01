package com.thevarungupta;

import java.sql.*;
import java.util.ArrayList;

public class TransactionsDaoImpl implements TransactionsDao {

    Connection connection;

    public TransactionsDaoImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void addTransaction(Transactions transaction) throws SQLException {
        String sql = "insert into transactions (user_id, account_id, type, amount, balance_before, balance_after, sent_to) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transaction.getUser_id());
        preparedStatement.setInt(2, transaction.getAccount_id());
        preparedStatement.setString(3, transaction.getType());
        preparedStatement.setDouble(4, transaction.getAmount());
        preparedStatement.setDouble(5, transaction.getBalance_before());
        preparedStatement.setDouble(6, transaction.getBalance_after());
        preparedStatement.setInt(7, transaction.getSent_to());
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Transaction saved");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public void acceptTransaction(int id) throws SQLException {
        String sql = String.format("update transactions set sent_to = %s where id = %s;",0,id);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Transaction accepted.");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public void rejectTransaction(int id) throws SQLException {
        String sql = String.format("update transactions set sent_to = %s where id = %s;",0,id);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Transaction rejected.");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public ArrayList<Transactions> getUserTransactions(int acc_id) throws SQLException {
        ArrayList<Transactions> transactions = new ArrayList<>();
        Statement stm = connection.createStatement();

        String sql = String.format("select * from transactions where account_id = %s;", acc_id);
        ResultSet rst = stm.executeQuery(sql);
        while(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            int account_id = rst.getInt("account_id");
            String type = rst.getString("type");
            double amount = rst.getDouble("amount");
            double balance_before = rst.getDouble("balance_before");
            double balance_after = rst.getDouble("balance_after");
            int sent_to = rst.getInt("sent_to");
            Transactions account1 = new Transactions(id,user_id, account_id, type, amount,balance_before,balance_after, sent_to);
            transactions.add(account1);
        }
        return transactions;
    }

    @Override
    public ArrayList<Transactions> getTransactions() throws SQLException {
        ArrayList<Transactions> transactions = new ArrayList<>();
        Statement stm = connection.createStatement();

        String sql = "select * from transactions;";
        ResultSet rst = stm.executeQuery(sql);
        while(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            int account_id = rst.getInt("account_id");
            String type = rst.getString("type");
            double amount = rst.getDouble("amount");
            double balance_before = rst.getDouble("balance_before");
            double balance_after = rst.getDouble("balance_after");
            int sent_to = rst.getInt("sent_to");
            Transactions account1 = new Transactions(id,user_id, account_id, type, amount,balance_before,balance_after, sent_to);
            transactions.add(account1);
        }
        return transactions;
    }

    @Override
    public ArrayList<Transactions> getUserTransfers(int acc_id) throws SQLException {
        ArrayList<Transactions> transactions = new ArrayList<>();
        Statement stm = connection.createStatement();

        String sql = String.format("select * from transactions where sent_to = %s;", acc_id);
        ResultSet rst = stm.executeQuery(sql);
        while(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            int account_id = rst.getInt("account_id");
            String type = rst.getString("type");
            double amount = rst.getDouble("amount");
            double balance_before = rst.getDouble("balance_before");
            double balance_after = rst.getDouble("balance_after");
            int sent_to = rst.getInt("sent_to");
            Transactions account1 = new Transactions(id,user_id, account_id, type, amount,balance_before,balance_after, sent_to);
            transactions.add(account1);
        }
        return transactions;
    }

}