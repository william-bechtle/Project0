package com.thevarungupta;

import java.sql.*;
import java.util.ArrayList;

public class AccountsDaoImpl implements AccountsDao {

    Connection connection;

    public AccountsDaoImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public ArrayList<Accounts> getUserAccounts(User account) throws SQLException{
        ArrayList<Accounts> accounts = new ArrayList<>();
        Statement stm = connection.createStatement();

        String sql = String.format("select * from accounts where user_id = '%s';", account.getId());
        ResultSet rst = stm.executeQuery(sql);
        while(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            String account_type = rst.getString("account_type");
            double balance = rst.getDouble("balance");
            double pending_transfer = rst.getDouble("pending_transfer");
            double pending_receive = rst.getDouble("pending_recieve");
            boolean pending_activation = rst.getBoolean("pending_activation");
            Accounts account1 = new Accounts(id,user_id,account_type,balance,pending_receive,pending_transfer,pending_activation);
            accounts.add(account1);
        }
        return accounts;

    }

    @Override
    public ArrayList<Accounts> getUserAccountsByID(int use_id) throws SQLException{
        ArrayList<Accounts> accounts = new ArrayList<>();
        Statement stm = connection.createStatement();

        String sql = String.format("select * from accounts where user_id = '%s';", use_id);
        ResultSet rst = stm.executeQuery(sql);
        while(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            String account_type = rst.getString("account_type");
            double balance = rst.getDouble("balance");
            double pending_transfer = rst.getDouble("pending_transfer");
            double pending_receive = rst.getDouble("pending_recieve");
            boolean pending_activation = rst.getBoolean("pending_activation");
            Accounts account1 = new Accounts(id,user_id,account_type,balance,pending_receive,pending_transfer,pending_activation);
            accounts.add(account1);
        }
        return accounts;

    }

    @Override
    public Accounts getAccountsByID(int account_id) throws SQLException{
        Accounts account1 = null;
        Statement stm = connection.createStatement();

        String sql = String.format("select * from accounts where id = '%s';", account_id);
        ResultSet rst = stm.executeQuery(sql);
        if(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            String account_type = rst.getString("account_type");
            double balance = rst.getDouble("balance");
            double pending_transfer = rst.getDouble("pending_transfer");
            double pending_receive = rst.getDouble("pending_recieve");
            boolean pending_activation = rst.getBoolean("pending_activation");
            account1 = new Accounts(id,user_id,account_type,balance,pending_receive,pending_transfer,pending_activation);
        }
        return account1;

    }

    @Override
    public void addAccount(Accounts account) throws SQLException {
        String sql = "insert into accounts (user_id, account_type, balance, pending_transfer, pending_recieve, pending_activation) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account.getUser_id());
        preparedStatement.setString(2, account.get_account_type());
        preparedStatement.setDouble(3, account.getBalance());
        preparedStatement.setDouble(4, account.getPending_transfer());
        preparedStatement.setDouble(5, account.getPending_receive());
        preparedStatement.setBoolean(6, account.isPending_activation());
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account saved");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public ArrayList<Accounts> getApprovalList() throws SQLException {
        ArrayList<Accounts> accounts = new ArrayList<>();
        Statement stm = connection.createStatement();

        String sql = "CALL getApproval()";
        ResultSet rst = stm.executeQuery(sql);
        while(rst.next()){
            int id = rst.getInt("id");
            int user_id = rst.getInt("user_id");
            String account_type = rst.getString("account_type");
            double balance = rst.getDouble("balance");
            double pending_transfer = rst.getDouble("pending_transfer");
            double pending_receive = rst.getDouble("pending_recieve");
            boolean pending_activation = rst.getBoolean("pending_activation");
            Accounts account1 = new Accounts(id,user_id,account_type,balance,pending_receive,pending_transfer,pending_activation);
            accounts.add(account1);
        }
        return accounts;

    }

    @Override
    public void updateAccount(Accounts account) throws SQLException {
        String sql = String.format("CALL updateAccount(%s,%s,%s,%s,%s);",
                account.getBalance(),account.getPending_transfer(), account.getPending_receive(), account.isPending_activation(), account.get_id());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account updated");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public void deleteAccount(int id) throws SQLException {
        String sql = String.format("CALL deleteAccount(%s);",id);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account updated");
        else
            System.out.println("Oops! something went wrong");
    }


}