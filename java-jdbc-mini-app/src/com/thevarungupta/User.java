package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class User {
    private int id;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    protected int account_type;

    User(){

    }


    User(User account){
        this.id = account.id;
        this.username = account.username;
        this.password = account.password;
        this.first_name = account.first_name;
        this.last_name = account.last_name;
        this.email = account.email;
        this.account_type = account.account_type;
    }

    User(String username, String password, String first_name, String last_name, String email){
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.account_type = 0;
    }

    User(int id, String username, String password, String first_name, String last_name, int account_type, String email){
        this.id = id;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.account_type = account_type;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String first_name) {
        this.first_name= first_name;
    }

    public String getFirstName() {return first_name;}

    public void setLastName(String last_name) {
        this.last_name= last_name;
    }

    public String getLastName() {return last_name;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccount_type() { return this.account_type; }

    public String getAccountType(int a){
        return switch (a) {
            case 0 -> "Apply for account.";
            case 1 -> "Registered, awaiting approval.";
            case 2 -> "Customer";
            case 3 -> "Employee";
            case 4 -> "Manager";
            default -> "No account type? ERROR.";
        };
    }

    @Override
    public String toString(){
        return("--------------User------------\nName: " + first_name + " " + last_name + "\nEmail: " + email + "\nAccount Type: " + getAccountType(this.account_type));
    }

    public boolean options(String option) throws SQLException {
        boolean logged_in = true;
        int options = 0;
        switch (option){
            case "1":
                ArrayList<Accounts> accounts = new ArrayList<>();
                AccountsDao dao = AccountsDaoFactory.getAccountsDao();
                accounts = dao.getUserAccounts(this);
                if (accounts.size() > 0)
                    while (options != accounts.size()+1) {
                       options = view_accounts(accounts);
                    }
                else
                    submit_app();
                break;
            case "2":
                update_password();
                break;
            case "3":
                System.out.println("Are you sure you want to delete? Type 'y' if yes, anything else to cancel:");
                Scanner scan = new Scanner(System.in);
                String choice = scan.nextLine();
                UserDao dao1 = UserDaoFactory.getEmployeeDao();
                if (Objects.equals(choice, "y")) {
                    dao1.deleteEmployee(this.id);
                    logged_in = false;
                }
                break;
            case "4":
                logged_in = false;
                break;
            default:
                System.out.println("INVALID OPTION. Review menu options and try again.");
                break;
        }
        return logged_in;
    }

    public void submit_app() throws SQLException {
        System.out.println("--------SUBMITTING NEW BANKING ACCOUNT APPLICATION------------");
        MenuDisplay menu = new MenuDisplay();
        Accounts account = menu.add_account_menu(this);
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        try {
            dao.addAccount(account);
            this.account_type = 1;
            System.out.println("Successfully applied for account!");

        }
        catch (Exception e){
            throw e;
        }

    }

    public int view_accounts(ArrayList<Accounts> accounts) {
        boolean account_view = true;
        int i = 1;
        int option = 0;
        System.out.println("---------------ACCOUNTS----------------");
            for (Accounts account : accounts) {
                if (account.isPending_activation()) {
                    System.out.println(i + "- Account " + account.get_id() + " is pending approval from employee.");
                } else {
                    System.out.println(i + "- Account " + account.get_id() + ", " + account.get_account_type() + " --- Balance: $" + account.getBalance());
                }
                i++;
            }
            System.out.println(i + "- Quit");
            System.out.println("Select by number:");
            Scanner scan = new Scanner(System.in);
            try {
                option = scan.nextInt();
            } catch (Exception e) {
                throw e;
            }
            if (((option < 1) || (option > accounts.size() + 1)) && ((account_type != 0) && (account_type != 1))){
                return 0;
            }
            else if (option == accounts.size() + 1) {
                return option;
            } else {
                if ((option < 1) || (option > accounts.size() + 1)) {
                    System.out.println("INVALID OPTION. Try again.");
                }
                else if ((account_type == 0) || (account_type == 1)) {
                    Accounts account = accounts.get(option - 1);
                    if (!account.isPending_activation()) {
                        System.out.println(account);
                    } else {
                        System.out.println("Account " + account.get_id() + " is PENDING ACTIVATION....");
                    }
                }
            }
            return option;
    }

    public void update_password() throws SQLException {
        Scanner scan = new Scanner(System.in);
        UserDao dao1 = UserDaoFactory.getEmployeeDao();

        String password1 = "";
        boolean password_check = false;
        while(!password_check) {
            System.out.println("Enter Desired Password: ");
            password1 = scan.nextLine();

            System.out.println("Reenter Desired Password: ");
            String password2 = scan.nextLine();

            if (password1.equals(password2)) {
                password_check = true;
            }
            else {
                System.out.println("PASSWORDS DO NOT MATCH, try again.");
            }
        }
        dao1.updateAccountPassword(this,password1);
    }
}

class Worker extends User {
    Worker(User account){
        super(account);
    }
    Worker(String username, String password, String first_name, String last_name, String email) {
        super(username,password,first_name,last_name,email);
        this.account_type = 3;
    }
    boolean menu_options(String option) throws SQLException {
        boolean logged_in = true;
        switch (option) {
            case "1" -> handle_approvals();
            case "2" -> view_customer();
            case "3" -> view_all_transactions();
            case "4" -> {
                logged_in = false;
                System.out.println("Logging out............");
            }
            default -> {
            }
        }
        return logged_in;
    }

    private void view_all_transactions() throws SQLException {
        TransactionsDao dao = TransactionsDaoFactory.getEmployeeDao();
        ArrayList<Transactions> transactions = new ArrayList<>();
        transactions = dao.getTransactions();
        if (transactions.size() == 0){
            System.out.println("NO TRANSACTIONS");
        }
        else {
            for (Transactions transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private void view_customer() throws SQLException {
        int customer_id = 0;
        boolean exit = false;
        ArrayList<Accounts> accounts = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        System.out.println("Enter Customer ID: ");
        try {
            customer_id = scan.nextInt();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        accounts = dao.getUserAccountsByID(customer_id);
        while (!exit) {
            int options = view_accounts(accounts);
            if (options == accounts.size()+1) {
                exit = true;
            }
            else if (options == 0){
                System.out.println("INVALID CHOICE, TRY AGAIN.");
            }
            else {
                Accounts account = accounts.get(options-1);
                account.account_menu_for_employee(account);
            }
        }




    }

    private void handle_approvals() throws SQLException {
        Scanner scan = new Scanner(System.in);
        ArrayList<Accounts> accounts;
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        UserDao dao1 = UserDaoFactory.getEmployeeDao();
        accounts = dao.getApprovalList();
        if (accounts.size() == 0) {
            System.out.println("NO ACCOUNTS AWAITING APPROVAL.");
        }
        for(Accounts account : accounts) {
            System.out.println("-----------Approve?------------: \nAccount: " + account.get_id()
            + "\nUser: " + account.getUser_id() + "\nStarting Balance: " + account.getBalance()
            + "\n 'Y' or 'N'? Please Enter:");
            String choice = scan.nextLine();
            if (Objects.equals(choice.toLowerCase(),"y")){
                account.setPending_activation(false);
                dao.updateAccount(account);
                User user = dao1.getUserById(account.getUser_id());
                if (user.account_type == 1) {
                    dao1.updateAccountType(account.getUser_id());
                    System.out.println("NEW CUSTOMER ADDED with First Account_ID: " + account.get_id());
                }
                else {
                    System.out.println("Account " + account.get_id() + " added for User " + account.getUser_id());
                }
            }
        }
    }
}

class Customer extends User {
    Customer(User account) {
        super(account);
    }

    public boolean menu_options(String option) throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
        Scanner scan = new Scanner(System.in);
        boolean exit = true;
        switch (option) {
            case "1":
                view_accounts();
                break;
            case "2":
                submit_app();
                this.account_type = 2;
                break;
            case "3":
                System.out.println("Are you sure you want to delete? Type 'y' if yes, anything else to cancel:");
                String choice = scan.next();
                UserDao dao1 = UserDaoFactory.getEmployeeDao();
                if (Objects.equals(choice, "y")) {
                    dao1.deleteEmployee(this.getId());
                    exit = false;
                }
                break;
            case "4":
                update_password();
                break;
            case "5":
                exit = false;
                break;
            default:
                break;
        }
        return exit;
    }



    public void view_accounts() throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
        boolean exit = false;
        ArrayList<Accounts> accounts;
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        while (!exit) {
            accounts = dao.getUserAccountsByID(this.getId());
            int options = view_accounts(accounts);
            if (options == accounts.size()+1) {
                exit = true;
            }
            else if (options == 0){
                System.out.println("INVALID CHOICE, TRY AGAIN.");
            }
            else {
                Accounts account = accounts.get(options-1);
                if (!account.isPending_activation()) {
                    account.account_menu_for_customer(account);
                } else {
                    System.out.println("Account " + account.get_id() + " is PENDING ACTIVATION....");
                }
            }
        }
    }
}

class Manager extends User{
    Manager(User a) {
        super(a);
    }
    private void addEmployee() throws SQLException {
        MenuDisplay menu = new MenuDisplay();
        Worker worker = menu.add_employee_menu();
        UserDao dao = UserDaoFactory.getEmployeeDao();
        try {
            dao.addUser(worker);
        }
        catch (java.sql.SQLIntegrityConstraintViolationException e){
            System.out.println("USERNAME ALREADY IN USE. Please try registering again.");
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
     boolean menu_options(String option) throws SQLException {
        boolean logged_in = true;
        int id = 0;
         switch (option) {
             case "1" -> this.addEmployee();
             case "2" -> {
                 Scanner scan = new Scanner(System.in);
                 System.out.println("Enter ID of User to delete: ");
                 try {
                     id = scan.nextInt();
                 } catch (Exception e) {
                     throw e;
                 }
                 System.out.println("Are you sure you want to delete? Type 'y' if yes, anything else to cancel:");
                 String choice = scan.next();
                 UserDao dao1 = UserDaoFactory.getEmployeeDao();
                 if (Objects.equals(choice, "y")) {
                     dao1.deleteEmployee(id);
                 }
             }
             case "3" -> {
                 System.out.println("Logging out...........");
                 logged_in = false;
             }
             default -> {
             }
         }
         return logged_in;
    }
}

