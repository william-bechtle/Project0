package com.thevarungupta;

import java.sql.*;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao {

    Connection connection;

    public UserDaoImpl(){
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public User checkLogin(String username, String password) throws SQLException {
        Statement stm = connection.createStatement();
        User account;
        int account_type;
        String sql = String.format("CALL login('%s','%s');",username,password);
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()){
            int id = rst.getInt("id");
            String userName = rst.getString("username");
            String password1 = rst.getString("password");
            String firstName = rst.getString("first_name");
            String lastName = rst.getString("last_name");
            String account_desig = rst.getString("account_type");
            account_type = switch (account_desig) {
                case "registered" -> 1;
                case "customer" -> 2;
                case "employee" -> 3;
                case "manager" -> 4;
                default -> 0;
            };
            String email = rst.getString("email");
            account = new User(id,userName,password1,firstName,lastName,account_type,email);
        }
        else {
            account = new User();
        }
        return account;
    }


    @Override
    public void addUser(User employee) throws SQLException {
        String sql = "insert into users (username, password, first_name, last_name, account_type, email) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        String s = String.valueOf(employee.getAccount_type());
        if (s.equals("0")){
            s = "1";
        }
        preparedStatement.setString(1, employee.getUserName());
        preparedStatement.setString(2, employee.getPassword());
        preparedStatement.setString(3, employee.getFirstName());
        preparedStatement.setString(4, employee.getLastName());
        preparedStatement.setString(5, s);
        preparedStatement.setString(6, employee.getEmail());
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("User saved");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public void updateAccountPassword(User user, String password) throws SQLException {
        String sql = String.format("update users set password = '%s' where id = %s;", password,user.getId());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Password updated");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public void updateAccountType(int id) throws SQLException {
        String sql = String.format("update users set account_type = 'customer' where id = %s;", id);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account_type updated");
        else
            System.out.println("Oops! something went wrong");
    }

    @Override
    public void deleteUser(int id) throws SQLException{
        String sql = String.format("delete from transactions where user_id = %s;", id);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Transactions for user " + id + " deleted");
        else
            System.out.println("User has no transactions.");
        sql = String.format("delete from accounts where user_id = %s;", id);
        preparedStatement = connection.prepareStatement(sql);
        count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Accounts for user " + id + " deleted");
        else
            System.out.println("User has no accounts.");
        sql = String.format("delete from users where id = %s;", id);
        preparedStatement = connection.prepareStatement(sql);
        count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("User " + id + ", deleted");
        else
            System.out.println("There is no user for that ID.");
    }


    @Override
    public User getUserById(int id) throws SQLException {
        User user = null;
        ResultSet rst;
        int id_conv = 0;
        Statement stm = connection.createStatement();
        String sql = String.format("select * from users where id = %s;",id);
        rst = stm.executeQuery(sql);
        if(rst.next()){
            String ID = rst.getString("id");
            try {
                id_conv = Integer.parseInt(ID);
            }
            catch (NumberFormatException e)
            {
                System.out.println("ERROR: " + e);
            }
            String account_desig = rst.getString("account_type");
            int account_type = switch (account_desig) {
                case "registered" -> 1;
                case "customer" -> 2;
                case "employee" -> 3;
                case "manager" -> 4;
                default -> 0;
            };
            user = new User(id_conv,rst.getString("username"),
                    rst.getString("password"), rst.getString("first_name"),
                    rst.getString("last_name"), account_type, rst.getString("email"));

        }
        return user;
    }
}
