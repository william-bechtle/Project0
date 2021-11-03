package com.thevarungupta;

import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
	// write your code here
        //INIT
        Logger theLogger = User.theLogger;
        MenuDisplay menu = new MenuDisplay();
        Scanner scan = new Scanner(System.in);
        UserDao dao = UserDaoFactory.getUserDao();
        User account = null;
        boolean logged_in = false;
        boolean quit = false;

        //OUTER MOST LOGIN MENU, GO THROUGH UNTIL QUIT
        while(!quit) {
            System.out.println("----------Welcome To Revature Bank-------------");
            menu.getLoginScreen();
            String input = scan.nextLine();

            //MENU FOR LOGIN/REGISTER
            switch (input) {

                //LOGIN
                case "1" -> {
                    ArrayList<String> credentials = new ArrayList<>();
                    credentials = menu.getCredentials();
                    account = dao.checkLogin(credentials.get(0), credentials.get(1));
                    logged_in = menu.isValidAccount(account, false);
                    if (logged_in) {
                        theLogger.debug("User " + account.getUserName() + " logged in.");
                    }
                }

                //REGISTER
                case "2" -> menu.getRegisterMenu();

                //QUIT
                case "3" -> {
                    quit = true;
                    System.out.println("......Quitting..............");
                }

                //ERROR
                default -> System.out.println("INVALID OPTION... Read menu and try again.");
            }

            //ACCOUNT MENU WHILE LOGGED IN
            while(logged_in){
                String option;


                System.out.println(account);

                //GET ACCOUNT TYPE FOR FEATURES, HANDLE TYPE AND DISPLAY CORRECT MENU
                switch (account.getAccount_type()) {

                    //BASE ACCOUNT
                    case 0, 1 -> {
                        menu.getBaseMenu();
                        option = scan.nextLine();
                        logged_in = account.handleBaseOptions(option);
                    }

                    //CUSTOMER ACCOUNT
                    case 2 -> {
                        Customer customer = new Customer(account);
                        menu.getCustomerMenu();
                        option = scan.nextLine();
                        logged_in = customer.customerMenuOptions(option);
                    }

                    //EMPLOYEE ACCOUNT
                    case 3 -> {
                        Worker worker = new Worker(account);
                        menu.getEmployeeMenu();
                        option = scan.nextLine();
                        logged_in = worker.workerMenuOptions(option);
                    }

                    //CEO ACCOUNT
                    case 4 -> {
                        Manager manager = new Manager(account);
                        menu.getManagerMenu();
                        option = scan.nextLine();
                        logged_in = manager.managerMenuOptions(option);
                    }
                }
                if (!logged_in) {
                    theLogger.debug("User " + account.getUserName() + " logged out.");
                }
            }



        }
    }
}

