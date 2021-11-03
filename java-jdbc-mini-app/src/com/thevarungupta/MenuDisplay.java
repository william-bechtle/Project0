package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuDisplay {

    //----------------------------------------------GET LOGIN SCREEN----------------------------------------------------
    public void getLoginScreen() {
        System.out.println("1 - LOGIN");
        System.out.println("2 - REGISTER");
        System.out.println("3 - QUIT");
        System.out.println("Please Enter Option: ");
    }

    //------------------------------------------------GET CREDENTIALS SCREEN--------------------------------------------
    public ArrayList<String> getCredentials(){
        ArrayList<String> credentials = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        //GET USER INPUT FOR USERNAME
        System.out.println("Enter Username: ");
        String userName = scan.nextLine();
        credentials.add(userName);

        //GET USER INPUT FOR PASSWORD
        System.out.println("Enter Password: ");
        String password = scan.nextLine();
        credentials.add(password);
        return credentials;
    }

    //-----------------------------------------GET REGISTRATION SCREEN--------------------------------------------------
    public void getRegisterMenu() throws SQLException {
        Scanner scan = new Scanner(System.in);
        String password = " ";
        System.out.println("------------New Customer Sign Up---------------");

        //GET USER INPUT FOR USERNAME
        System.out.println("Enter Desired Username: ");
        String userName = scan.nextLine();

        //GET USER INPUT FOR PASSWORD, KEEP DOING UNTIL THEY MATCH
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

        //GET USER INPUT FOR FIRST NAME
        System.out.println("Enter First Name: ");
        String firstName = scan.nextLine();

        //GET USER INPUT FOR LAST NAME
        System.out.println("Enter Last Name: ");
        String lastName = scan.nextLine();

        //GET USER INPUT FOR EMAIL
        System.out.println("Enter Email: ");
        String email = scan.nextLine();

        //ATTEMPT TO ADD ACCOUNT
        User new_account = new User(userName,password,firstName,lastName,email);
        UserDao dao = UserDaoFactory.getUserDao();
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

    //--------------------------------------GET ADD EMPLOYEE SCREEN FOR CEO'S-------------------------------------------
    public Worker addEmployeeMenu(){
        Scanner scan = new Scanner(System.in);
        String password = "";

        //GET USERNAME
        System.out.println("Enter Username: ");
        String userName = scan.nextLine();

        //GET PASSWORD UNTIL THEY MATCH
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

        //GET FIRST NAME
        System.out.println("Enter First Name: ");
        String firstName = scan.nextLine();

        //GET LAST NAME
        System.out.println("Enter Last Name: ");
        String lastName = scan.nextLine();

        //GET EMAIL
        System.out.println("Enter eMail: ");
        String email = scan.nextLine();

        return new Worker(userName,password,firstName,lastName,email);
    }

    //---------------------------------------------GET BASE ACCOUNT MENU------------------------------------------------
    public void getBaseMenu(){
        System.out.println("Select an option from the menu below:");
        System.out.println("1 - Account Application");
        System.out.println("2 - Change Password");
        System.out.println("3 - Delete Account");
        System.out.println("4 - Logout");
        System.out.println("Please Enter Option: ");
    }

    //-------------------------------------------GET ADD BANK ACCOUNT MENU----------------------------------------------
    public Accounts addAccountMenu(User user){
        Scanner scan = new Scanner(System.in);

        //GET ACCOUNT TYPE FROM USER
        boolean correct = false;
        String account_type = "Checking";
        do {
            System.out.println("Choose Account Type: ");
            System.out.println("1 - Checking");
            System.out.println("2 - Savings");
            System.out.println("Please Enter Option: ");
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

        //GET STARTING BALANCE FROM USER

        correct = false;
        double balance = 0.00;
        do {
            scan = new Scanner(System.in);
            System.out.println("Enter Starting Balance: ");
            try {
                balance = scan.nextDouble();

                //THROW ERROR IF NEGATIVE
                if (balance < 0) {
                    throw new Exceptions.depositNegative();
                }

                //ROUND
                balance = Math.round(balance*100.0)/100.0;
                correct = true;
            }
            catch(Exception e){
                System.out.println(e);
            }
        }while(!correct);

        return new Accounts(user.getId(),account_type,balance,0.00,0.00,true);
    }

    //-------------------------------------------GET CUSTOMER ACCOUNT MENU----------------------------------------------
    public void getCustomerMenu(){
        System.out.println("Select an option from the menu below:");
        System.out.println("1 - View Accounts");
        System.out.println("2 - Apply for new account");
        System.out.println("3 - Delete User Account and Close All Accounts");
        System.out.println("4 - Change Password");
        System.out.println("5 - Quit");
        System.out.println("Please Enter Option: ");
    }

    //------------------------------------------GET MANAGER ACCOUNT MENU------------------------------------------------
    public void getManagerMenu() {
        System.out.println("----------------Manager Menu-------------------");
        System.out.println("1 - Add Employee");
        System.out.println("2 - Delete user");
        System.out.println("3 - QUIT");
        System.out.println("Please Enter Option: ");
    }

    //------------------------------------------GET EMPLOYEE ACCOUNT MENU-----------------------------------------------
    public void getEmployeeMenu(){
        System.out.println("----------------Employee Menu-------------------");
        System.out.println("1 - Approval List");
        System.out.println("2 - View a customer Account");
        System.out.println("3 - View all transactions");
        System.out.println("4 - Quit");
        System.out.println("Please Enter Option: ");
    }

    //------------------------------------------GET EMPLOYEE VIEW ACCOUNT MENU------------------------------------------
    public void getEmployeeAccountMenu(Accounts account) {
        System.out.println(account);
        System.out.println("1 - View Transactions");
        System.out.println("2 - Quit");
        System.out.println("Please Enter Option: ");
    }

    //------------------------------------------GET CUSTOMER VIEW ACCOUNT MENU------------------------------------------
    public void getCustomerAccountMenu(Accounts account) {
        System.out.println(account);
        System.out.println("1 - View Transactions");
        System.out.println("2 - Deposit");
        System.out.println("3 - Withdrawal");
        System.out.println("4 - Send Money");
        System.out.println("5 - View Transfers Sent To You");
        System.out.println("6 - Quit");
        System.out.println("Please Enter Option:  ");
    }

    //------------------------------------------CHECK IF VALID CREDENTIAL ENTERED---------------------------------------
    public boolean isValidAccount(User account, boolean logged_in){
        //INVALID?
        if (account.getUserName() == null) {
            System.out.println("INVALID CREDENTIALS. Try again or register.");
        }

        //IF NOT, GET ACCOUNT INFO
        else {
            System.out.println(account.getUserName() + " logged in.");
            logged_in = true;
        }
        return logged_in;
    }





}