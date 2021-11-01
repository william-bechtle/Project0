package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
	// write your code here
        //INIT
        MenuDisplay menu = new MenuDisplay();
        Scanner scan = new Scanner(System.in);
        UserDao dao = UserDaoFactory.getEmployeeDao();
        User account = null;
        boolean register = false;
        boolean logged_in = false;
        boolean quit = false;


        while(!quit) {
            System.out.println("----------Welcome To Revature Bank-------------");
            menu.print_login();
            String input = scan.nextLine();

            switch (input){
                case "1":
                    ArrayList<String> credentials = new ArrayList<>();
                    credentials = menu.get_credentials();
                    account = dao.checkLogin(credentials.get(0),credentials.get(1));
                    if (account.getUserName() == null){
                        System.out.println("INVALID CREDENTIALS. Try again or register.");
                    }
                    else {
                        System.out.println(account.getUserName() + " logged in.");
                        logged_in = true;
                        System.out.println(account);
                    }
                    break;
                case "2":
                    menu.register_menu();
                    break;
                case "3":
                    quit = true;
                    System.out.println("......Quitting..............");
                    break;
                default:
                    System.out.println("INVALID OPTION... Read menu and try again.");
                    break;
            }


            while(logged_in){
                String option;
                switch (account.getAccount_type()) {
                    case 0:
                    case 1:
                        menu.base_account_menu();
                        option = scan.nextLine();
                        logged_in = account.options(option);
                        break;
                    case 2:
                        Customer customer = new Customer(account);
                        menu.customer_menu();
                        option = scan.nextLine();
                        logged_in = customer.menu_options(option);
                        break;
                    case 3:
                        Worker worker = new Worker(account);
                        menu.employee_menu();
                        option = scan.nextLine();
                        logged_in = worker.menu_options(option);
                        break;
                    case 4:
                        Manager manager = new Manager(account);
                        menu.manager_menu();
                        option = scan.nextLine();
                        logged_in = manager.menu_options(option);
                        break;
                }
            }



        }
    }
}

