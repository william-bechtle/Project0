package com.thevarungupta;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


//------------------------------------------------------------------------------BASE USER CLASS------------------------------------------------------------------------------
public class User {
    private int id;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    protected int account_type;
    public static Logger theLogger = Logger.getLogger(User.class.getName());

    //NULL CONSTRUCTOR
    User(){

    }

    //Constructor for loading with object instance
    User(User account){
        this.id = account.id;
        this.username = account.username;
        this.password = account.password;
        this.first_name = account.first_name;
        this.last_name = account.last_name;
        this.email = account.email;
        this.account_type = account.account_type;
    }

    //Constructor for adding new registration
    User(String username, String password, String first_name, String last_name, String email){
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.account_type = 0;
    }

    //Constructor for loading from database
    User(int id, String username, String password, String first_name, String last_name, int account_type, String email){
        this.id = id;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.account_type = account_type;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getUserName() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstName() {return first_name;}
    public String getLastName() {return last_name;}
    public String getEmail() {
        return email;
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

    //Print object
    @Override
    public String toString(){
        return("--------------User------------\nName: " + first_name + " " + last_name + "\nEmail: " + email + "\nAccount Type: " + getAccountType(this.account_type));
    }

    //BASE ACCOUNT OPTIONS
    public boolean handleBaseOptions(String option) throws SQLException {
        boolean logged_in = true;
        int options = 0;
        switch (option) {
            //VIEW ACCOUNT OR APPLY
            case "1" -> getAccounts(options);
            //UPDATE PASSWORD
            case "2" -> updatePassword();
            //DELETE ACCOUNT
            case "3" -> logged_in = deleteAccount();
            //LOGOUT
            case "4" -> logged_in = false;
            default -> System.out.println("INVALID OPTION. Review menu options and try again.");
        }
        return logged_in;
    }

    //RETRIEVE USER ACCOUNTS
    public void getAccounts(int options) throws SQLException {
        ArrayList<Accounts> accounts = new ArrayList<>();
        //GET ACCOUNTS
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        accounts = dao.getUserAccounts(this);

        //IF HAS ACCOUNTS PRINT
        if (accounts.size() > 0)
            while (options != accounts.size()+1) {
                options = viewAccounts(accounts);
            }

        //IF NOT SUBMIT APP
        else
            submitApp();
    }

    //DELETE ACCOUNT FOR USER
    public boolean deleteAccount() throws SQLException {
        System.out.println("Are you sure you want to delete? Type 'y' if yes, anything else to cancel:");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine();
        UserDao dao1 = UserDaoFactory.getUserDao();

        //IF YES THEN DELETE
        if (Objects.equals(choice, "y")) {
            dao1.deleteUser(this.id);
            theLogger.debug("User " + this.id + " deleted account.");
            return false;
        }
        else{
        return true;}
    }

    //SUBMIT BANKING ACCOUNT APP FOR USER
    public void submitApp() throws SQLException {
        System.out.println("--------SUBMITTING NEW BANKING ACCOUNT APPLICATION------------");

        //APPLY FOR ACCOUNT MENU DISPLAY
        MenuDisplay menu = new MenuDisplay();
        Accounts account = menu.addAccountMenu(this);

        //ADD ACCOUNT
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        try {
            dao.addAccount(account);
            this.account_type = 1;
            String bal = String.format("%.2f", account.getBalance());
            String log = String.format("USER %s APPLIED FOR NEW ACCOUNT %s WITH STARTING BALANCE $%s",id,account.get_id(),bal);
            theLogger.info(log);
            System.out.println("Successfully applied for account!");
        }
        catch (Exception e){
            throw e;
        }

    }

    //VIEW ALL ACCOUNTS FOR ACCOUNT
    public int viewAccounts(ArrayList<Accounts> accounts) {
        boolean account_view = true;
        int i = 1;
        int option = 0;

        //GET "ACCOUNTS" MENU LISTING ALL ACCOUNTS
        System.out.println("---------------ACCOUNTS----------------");
            for (Accounts account : accounts) {
                printAccounts(account, i);
                i++;
            }
            System.out.println(i + "- Quit");
            System.out.println("Select by number:");

            //GET ACCOUNT CHOSEN
            option = handleAccountChoice(accounts);
            return option;
    }

    //MAKE SURE ACCOUNT CHOICE IS VALID
    public int handleAccountChoice(ArrayList<Accounts> accounts) {
        int option = 0;
        Scanner scan = new Scanner(System.in);

        //MAKE SURE INT
        try {
            option = scan.nextInt();
        } catch (Exception e) {
            throw e;
        }

        //WRONG OPTION FOR CUSTOMERS
        if (((option < 1) || (option > accounts.size() + 1)) && ((account_type != 0) && (account_type != 1))){
            return 0;
        }
        //QUIT OPTION
        else if (option == accounts.size() + 1) {
            return option;
        }
        //CORRECT OPTION
        else {
            //HANDLE BASE ACCOUNT MENU... (IF BASE ACCOUNT, IT DOES NOTHING WITH OPTION VALUE)
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

    //UPDATE PASSWORD
    public void updatePassword() throws SQLException {
        Scanner scan = new Scanner(System.in);
        UserDao dao1 = UserDaoFactory.getUserDao();

        String password1 = "";
        boolean password_check = false;

        //DO UNTIL THEY MATCH
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
        theLogger.debug("User " + this.id + " updated password to: " + password1);
    }

    //ITERATE THROUGH USER ACCOUNTS AND PRINT
    public void printAccounts(Accounts account, int i) {
        if (account.isPending_activation()) {
            System.out.println(i + "- Account " + account.get_id() + " is pending approval from employee.");
        } else {
            String balance = String.format("%.2f", account.getBalance());
            System.out.println(i + "- Account " + account.get_id() + ", " + account.get_account_type() + " --- Balance: $" + balance);
        }
    }
}


//------------------------------------------------------------------------------WORKER CLASS------------------------------------------------------------------------------
class Worker extends User {

    //CONSTRUCTOR TO LOAD FROM DB
    Worker(User account){
        super(account);
    }

    //CONSTRUCTOR FOR CEO TO ADD
    Worker(String username, String password, String first_name, String last_name, String email) {
        super(username,password,first_name,last_name,email);
        this.account_type = 3;
    }

    //WORKER MENU OPTIONS
    boolean workerMenuOptions(String option) throws SQLException {
        boolean logged_in = true;
        switch (option) {
            //HANDLE APPLICATIONS
            case "1" -> handleApprovals();
            //VIEW CUSTOMER ACCOUNT
            case "2" -> viewCustomer();
            //VIEW ALL TRANSACTIONS
            case "3" -> viewAllTransactions();
            //QUIT
            case "4" -> {
                logged_in = false;
                System.out.println("Logging out............");
            }
            //WRONG OPTION
            default -> {
                System.out.println("INVALID OPTION.");
            }
        }
        return logged_in;
    }

    //VIEW ENTIRE TRANSACTION DATABASE
    private void viewAllTransactions() throws SQLException {
        TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
        ArrayList<Transactions> transactions = new ArrayList<>();
        transactions = dao.getTransactions();

        //IF NONE
        if (transactions.size() == 0){
            System.out.println("NO TRANSACTIONS");
        }
        //IF THERE IS
        else {
            for (Transactions transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    //VIEW CUSTOMER ACCOUNT
    private void viewCustomer() throws SQLException {
        int customer_id = 0;
        boolean exit = false;
        ArrayList<Accounts> accounts = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        System.out.println("Enter Customer ID: ");

        //MAKE SURE INT
        try {
            customer_id = scan.nextInt();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        //NO CUSTOMER OR CUSTOMER NOT REGISTERED FOR BANKING ACCOUNT
        accounts = dao.getUserAccountsByID(customer_id);
        if (accounts.size() == 0) {
            System.out.println("NO CUSTOMER OR CUSTOMER NOT REGISTERED FOR BANKING ACCOUNT");
        }

        else {
            //CUSTOMER ACCOUNT MENU
            while (!exit) {
                int options = viewAccounts(accounts);
                if (options == accounts.size() + 1) {
                    exit = true;
                } else if (options == 0) {
                    System.out.println("INVALID CHOICE, TRY AGAIN.");
                } else {
                    Accounts account = accounts.get(options - 1);
                    account.accountMenuForEmployee(account);
                }
            }
        }
    }

    //REJECT OR ACCEPT PENDING APPS
    private void handleApprovals() throws SQLException {
        Scanner scan = new Scanner(System.in);
        ArrayList<Accounts> accounts;

        //GET ACCOUNT PENDING APPROVAL
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        accounts = dao.getApprovalList();

        //IF NONE
        if (accounts.size() == 0) {
            System.out.println("NO ACCOUNTS AWAITING APPROVAL.");
        }

        //IF SOME, GO THROUGH LIST AND GET ACTION
        for(Accounts account : accounts) {
            String bal = String.format("%.2f", account.getBalance());
            System.out.println("-----------Approve?------------: \nAccount: " + account.get_id()
            + "\nUser: " + account.getUser_id() + "\nStarting Balance: " + bal
            + "\n 'Y' or 'N'? Please Enter:");
            String choice = scan.nextLine();

            //IF Y, ACCEPT ACCOUNT
            if (Objects.equals(choice.toLowerCase(),"y")){
                acceptAccount(account,dao);
                theLogger.debug("Employee " + this.getId() + " approved Account " + account.get_id() + " for User " + account.getUser_id());
            }

            //IF N, DELETE ACCOUNT
            else {
                dao.deleteAccount(account.get_id());
            }
        }
    }

    //ACCEPT A PENDING ACCOUNT
    private void acceptAccount(Accounts account, AccountsDao dao) throws SQLException {
        UserDao dao1 = UserDaoFactory.getUserDao();

        //DESIGNATES ACCOUNT NO LONGER NEEDS ACTIVATION
        account.setPending_activation(false);
        dao.updateAccount(account);

        //GET USER FOR ACCOUNT
        User user = dao1.getUserById(account.getUser_id());

        //IF USER'S FIRST ACCOUNT, UPDATE ACCOUNT TO CUSTOMER
        if (user.account_type == 1) {
            dao1.updateAccountType(account.getUser_id());
            System.out.println("NEW CUSTOMER ADDED with First Account_ID: " + account.get_id());
        }

        //IF NOT JUST APPROVE ACCOUNT
        else {
            System.out.println("Account " + account.get_id() + " added for User " + account.getUser_id());
        }
    }
}

//------------------------------------------------------------------------------Customer CLASS------------------------------------------------------------------------------
class Customer extends User {

    //Constructor for customer, same fields as account but account_type is different.
    Customer(User account) {
        super(account);
    }

    //CUSTOMER MENU
    public boolean customerMenuOptions(String option) throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
        Scanner scan = new Scanner(System.in);
        boolean exit = true;
        switch (option) {
            //VIEW ALL ACCOUNTS
            case "1" -> viewCustomerAccounts();
            //OPEN NEW ACCOUNT
            case "2" -> {
                submitApp();
                this.account_type = 2;
            }
            //DELETE ACCOUNT
            case "3" -> deleteAccount();
            //UPDATE PASSWORD
            case "4" -> updatePassword();
            //QUIT
            case "5" -> exit = false;
            //WRONG CHOICE
            default -> {
                System.out.println("INVALID CHOICE. Read menu options and try again.");
            }
        }
        return exit;
    }

    //GET ALL ACCOUNTS FOR USER, DOESN'T HANDLE BASE CASE
    private void viewCustomerAccounts() throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
        boolean exit = false;
        ArrayList<Accounts> accounts;
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();

        //ACCOUNT OPTIONS
        while (!exit) {
            accounts = dao.getUserAccountsByID(this.getId());
            int options = viewAccounts(accounts);
            if (options == accounts.size()+1) {
                exit = true;
            }
            else if (options == 0){
                System.out.println("INVALID CHOICE, TRY AGAIN.");
            }
            else {
                Accounts account = accounts.get(options-1);
                if (!account.isPending_activation()) {
                    account.accountMenuForCustomer(account);
                } else {
                    System.out.println("Account " + account.get_id() + " is PENDING ACTIVATION....");
                }
            }
        }
    }
}


//------------------------------------------------------------------------------CEO CLASS------------------------------------------------------------------------------
class Manager extends User{
    //LOAD MANAGER FROM DB (CREATED BEFORE FIRST RUN)
    Manager(User a) {
        super(a);
    }

    //ADD AN EMPLOYEE
    private void addEmployee() throws SQLException {
        MenuDisplay menu = new MenuDisplay();
        Worker worker = menu.addEmployeeMenu();
        UserDao dao = UserDaoFactory.getUserDao();
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

    //DELETE ANYONE
    private void ceoDelete() throws SQLException {
        int id = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter ID of User to delete: ");
        try {
            id = scan.nextInt();
        } catch (Exception e) {
            throw e;
        }
        System.out.println("Are you sure you want to delete? Type 'y' if yes, anything else to cancel:");
        String choice = scan.next();
        UserDao dao1 = UserDaoFactory.getUserDao();
        if (Objects.equals(choice, "y")) {
            dao1.deleteUser(id);
        }
    }

    //CEO MENU OPTIONS
     boolean managerMenuOptions(String option) throws SQLException {
        boolean logged_in = true;
         switch (option) {
             case "1" -> this.addEmployee();
             case "2" -> {
                 ceoDelete();
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

