package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class MenuDisplay {
    //PRINT HOME SCREEN
    public void print_login() {
        System.out.println("1 - LOGIN");
        System.out.println("2 - REGISTER");
        System.out.println("3 - QUIT");
        System.out.println("PLEASE ENTER OPTION:");
    }

    //GET CREDENTIALS
    public ArrayList<String> get_credentials(){
        ArrayList<String> credentials = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter Username: ");
        String userName = scan.nextLine();
        credentials.add(userName);

        System.out.println("Enter Password: ");
        String password = scan.nextLine();
        credentials.add(password);
        return credentials;
    }

    public void register_menu() throws SQLException {
        Scanner scan = new Scanner(System.in);
        String password = " ";
        System.out.println("------------New Customer Sign Up---------------");
        System.out.println("Enter Desired Username: ");
        String userName = scan.nextLine();

        boolean password_check = false;
        while(!password_check) {
            System.out.println("Enter Desired Password: ");
            password = scan.nextLine();

            System.out.println("Reenter Desired Password: ");
            String password2 = scan.nextLine();

            if (password.equals(password2)) {
                password_check = true;
            }
            else {
                System.out.println("PASSWORDS DO NOT MATCH, try again.");
            }
        }

        System.out.println("Enter First Name: ");
        String firstName = scan.nextLine();

        System.out.println("Enter Last Name: ");
        String lastName = scan.nextLine();

        System.out.println("Enter Email: ");
        String email = scan.nextLine();

        User new_account = new User(userName,password,firstName,lastName,email);
        UserDao dao = UserDaoFactory.getEmployeeDao();
        try {
            dao.addUser(new_account);
            System.out.println("SUCCESS! Please login.");
        }
        catch (java.sql.SQLIntegrityConstraintViolationException e){
            System.out.println("USERNAME ALREADY IN USE. Please try registering again.");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void user_menu(){
        System.out.println("Please select a menu option: ");
        System.out.println("1 - Apply for banking account.");
        System.out.println("2 - ");
    }

    public Worker add_employee_menu(){
        Scanner scan = new Scanner(System.in);
        String password = "";

        System.out.println("Enter Username: ");
        String userName = scan.nextLine();

        boolean password_check = false;
        while(!password_check) {
            System.out.println("Enter Password: ");
            password = scan.nextLine();

            System.out.println("Reenter Password: ");
            String password2 = scan.nextLine();

            if (password.equals(password2)) {
                password_check = true;
            }
            else {
                System.out.println("Passwords do not match, try again.");
            }
        }

        System.out.println("Enter First Name: ");
        String firstName = scan.nextLine();

        System.out.println("Enter Last Name: ");
        String lastName = scan.nextLine();

        System.out.println("Enter eMail: ");
        String email = scan.nextLine();

        return new Worker(userName,password,firstName,lastName,email);
    }

    public void base_account_menu(){
        System.out.println("Select an option from the menu below:");
        System.out.println("1- Account Application");
        System.out.println("2- Change Password");
        System.out.println("3- Delete Account");
        System.out.println("4- Logout");
        System.out.println("PLEASE ENTER OPTION: ");
    }

    public Accounts add_account_menu(User user){
        Scanner scan = new Scanner(System.in);

        //GET ACCOUNT TYPE FROM USER
        boolean correct = false;
        String account_type = "Checking";
        do {
            System.out.println("Choose Account Type: ");
            System.out.println("1 - Checking");
            System.out.println("2 - Savings");
            System.out.println("Enter Option: ");
            String option = scan.nextLine();
            switch (option) {
                case "1":
                    account_type = "Checking";
                    correct = true;
                    break;
                case "2":
                    account_type = "Savings";
                    correct = true;
                    break;
                default:
                    System.out.println("INCORRECT OPTION. Review menu and try again.");
                    break;
            }
        }while(!correct);

        System.out.println("Enter Starting Balance: ");
        correct = false;
        double balance = 0.00;
        do {
            try {
                balance = scan.nextDouble();
                balance = Math.round(balance*100.0)/100.0;
                correct = true;
            }
            catch(Exception e){
                System.out.println(e);
            }
        }while(!correct);

        return new Accounts(user.getId(),account_type,balance,0.00,0.00,true);
    }

    public void customer_menu(){
        System.out.println("Select an option from the menu below:");
        System.out.println("1- View Accounts");
        System.out.println("2- Apply for new account");
        System.out.println("3- Delete User Account and Close All Accounts");
        System.out.println("4- Change Password");
        System.out.println("5- Quit");
        System.out.println("PLEASE ENTER OPTION: ");
    }

    public void manager_menu() {
        System.out.println("----------------Manager Menu-------------------");
        System.out.println("1 - Add Employee");
        System.out.println("2 - Delete user");
        System.out.println("3 - QUIT");
        System.out.println("Please enter option:");
    }

    public void employee_menu(){
        System.out.println("----------------Employee Menu-------------------");
        System.out.println("1 - Approval List");
        System.out.println("2 - View a customer Account");
        System.out.println("3 - View all transactions");
        System.out.println("4 - Quit");
        System.out.println("Please enter option:");
    }



}