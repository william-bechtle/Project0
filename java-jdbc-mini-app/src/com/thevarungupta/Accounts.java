package com.thevarungupta;

import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Accounts {
    private int id;
    private int user_id;
    private String account_type;
    private double balance;
    private double pending_transfer;
    private double pending_receive;
    private boolean pending_activation;
    public static Logger theLogger = User.theLogger;

    Accounts(){
    }

    //CONSTRUCTOR TO CREATE A NEW DATA INSTANCE
    Accounts(int user_id,String account_type,double balance,double pending_receive, double pending_transfer, boolean pending_activation){
        this.user_id = user_id;
        this.account_type = account_type;
        this.balance = balance;
        this.pending_receive = pending_receive;
        this.pending_transfer = pending_transfer;
        this.pending_activation = pending_activation;
    }

    //CONSTRUCTOR TO LOAD FROM DB
    Accounts(int id, int user_id,String account_type,double balance,double pending_receive, double pending_transfer, boolean pending_activation){
        this.id = id;
        this.user_id = user_id;
        this.account_type = account_type;
        this.balance = balance;
        this.pending_receive = pending_receive;
        this.pending_transfer = pending_transfer;
        this.pending_activation = pending_activation;
    }

    //GETTERS and SETTERS
    public int get_id(){return id;}
    public String get_account_type(){
        return account_type;
    }
    public int getUser_id(){
        return user_id;
    }
    public double getBalance(){
        return balance;
    }
    public double getPending_transfer(){
        return pending_transfer;
    }
    public double getPending_receive(){
        return pending_receive;
    }
    public boolean isPending_activation(){
        return pending_activation;
    }
    public void setPending_activation(boolean pending_activation){
        this.pending_activation = pending_activation;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public void setPending_transfer(double pending_transfer){
        this.pending_transfer = pending_transfer;
    }
    public void setPending_receive(double pending_receive){
        this.pending_receive = pending_receive;
    }

    //TOSTRING
    @Override
    public String toString(){
        String balance = String.format("%.2f",this.balance);
        String balance_recieve = String.format("%.2f",this.pending_receive);
        String balance_transfer = String.format("%.2f",this.pending_transfer);
        return "------------------Account: " + this.id + "------------------\nBalance: $" + balance + "\nType: " + this.account_type + "\nPending Approval: $" + balance_recieve
                + "\nPending Transfer: $" + balance_transfer;
    }

    //ACCOUNT MENU FOR EMPLOYEE
    void accountMenuForEmployee(Accounts account) throws SQLException {
        Scanner scan = new Scanner(System.in);
        MenuDisplay menu = new MenuDisplay();
        boolean exit = false;

        //ACCOUNT NEEDS APPROVAL
        if (account.isPending_activation()){
            System.out.println("ACCOUNT WAITING FOR YOU OR OTHER EMPLOYEE'S APPROVAL");
        }
        else {
            while (!exit) {
                menu.getEmployeeAccountMenu(account);
                String option = scan.nextLine();
                switch (option) {
                    //GET ALL TRANSACTIONS
                    case "1" -> getTransactions();
                    //QUIT
                    case "2" -> exit = true;
                    //WRONG CHOICE
                    default -> {
                        System.out.println("INVALID OPTION");
                    }
                }
            }
        }
    }

    //ACCOUNT FUNCTIONS FOR CUSTOMER
    void accountMenuForCustomer(Accounts account) throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {

            MenuDisplay menu = new MenuDisplay();
            menu.getCustomerAccountMenu(account);
            String option = scan.next();

            switch (option) {
                //GET ACCOUNT TRANSACTIONS
                case "1" -> getTransactions();
                //DEPOSIT
                case "2" -> tryDeposit();
                //WITHDRAW
                case "3" -> tryWithdraw();
                //SEND MONEY
                case "4" -> tryTransfer();
                //ACCEPT MONEY TRANSFERS
                case "5" -> {
                    System.out.println("..........Looking for money transfers.....");
                    handleSentMoney();
                }
                //QUIT
                case "6" -> exit = true;
                //WRONG CHOICE
                default -> System.out.println("INVALID CHOICE. Try again.");
            }
        }
    }

    //DEPOSIT MONEY
    void deposit(double amount) throws Exceptions.depositNegative, SQLException {
        try {
            //NEGATIVE ERROR
            if (amount < 0) {
                throw new Exceptions.depositNegative();
            } else {
                //DEPOSIT
                double balance_before = balance;
                balance = balance + amount;
                Transactions transaction = new Transactions(user_id, id, "deposit", amount, balance_before, balance);

                //UPDATE IN DB
                TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
                dao.addTransaction(transaction);
                AccountsDao dao1 = AccountsDaoFactory.getAccountsDao();
                dao1.updateAccount(this);
                theLogger.debug("Deposit made by User " + this.getUser_id());

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //WITHDRAW
    void withdraw(double amount) throws Exceptions.withdrawPositive, Exceptions.negativeBalance, SQLException {
        try {
            //NEGATIVE ERROR
            if (amount < 0) {
                throw new Exceptions.withdrawPositive();
            }
            //GREATER THAN BALANCE ERROR
            else if (amount > balance - pending_transfer) {
                throw new Exceptions.negativeBalance();
            }
            else {
                //WITHDRAW
                double balance_before = balance;
                balance = balance - amount;
                Transactions transaction = new Transactions(user_id, id, "withdrawal", -amount, balance_before, balance);

                //UPDATE DB
                TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
                dao.addTransaction(transaction);
                AccountsDao dao1 = AccountsDaoFactory.getAccountsDao();
                dao1.updateAccount(this);
                theLogger.debug("Withdraw made by User " + this.getUser_id());
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    //HANDLE MONEY SENT TO ACCOUNT
    void handleSentMoney() throws SQLException {
        Scanner scan = new Scanner(System.in);
        TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
        ArrayList<Transactions> transactions;
        transactions = dao.getUserTransfers(id);

        //IF NO SENT
        if (transactions.size() == 0){
            System.out.println("NO MONEY SENT");
        }
        //GO THROUGH ALL SENT TO APPROVE
        else{
            for(Transactions transaction : transactions) {
                String money = String.format("%.2f",-transaction.getAmount());
                System.out.println("User " + transaction.getUser_id() + " sent $" + money + ". Accept? (Y or N): ");
                String option = scan.nextLine();
                switch (option.toLowerCase()) {
                    case "y" ->
                            //APPROVE
                            handleAccept(transaction);
                    case "n" ->
                            //REJECT
                            handleReject(transaction);
                    default ->
                            //WRONG CHOICE
                            System.out.println("INVALID CHOICE. Try again.");
                }
            }
        }
    }

    //SEND MONEY TO OTHER ACCOUNT
    void sendMoney(int id, double amount) throws Exceptions.negativeBalance, Exceptions.sendNegative, SQLException {
        AccountsDao dao = AccountsDaoFactory.getAccountsDao();
        Accounts account;

        //IF TRYING TO SEND MONEY TO CURRENT ACCOUNT, DON'T
        if (id == this.id) {
            System.out.println("SORRY... you cannot send money to current account.");
        }
        else {
            //GET ACCOUNT TO SEND TO
            account = dao.getAccountsByID(id);
            try {
                //CANT SEND NEGATIVE
                if (amount < 0) {
                    throw new Exceptions.sendNegative();
                } else if (amount > balance - pending_transfer) {
                    //CAN'T SEND MORE THAN YOU HAVE
                    throw new Exceptions.negativeBalance();
                } else if (account == null) {
                    //CANT SENT TO NON EXISTENT ACCOUNT
                    System.out.println("NO ACCOUNT WITH THAT ID FOUND.");
                } else {
                    //UPDATE DATABASE WITH PENDING AMOUNTS
                    Transactions transaction = new Transactions(user_id, this.id, "pending transfer", -amount, balance, balance, id);
                    TransactionsDao dao1 = TransactionsDaoFactory.getTransactionDao();
                    dao1.addTransaction(transaction);
                    account.setPending_receive(account.getPending_receive() + amount);
                    pending_transfer = pending_transfer + amount;
                    dao.updateAccount(account);
                    dao.updateAccount(this);
                    theLogger.debug("Transfer request made by User " + this.getUser_id());
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    //GET TRANSACTIONS FOR ACCOUNT
    void getTransactions() throws SQLException {
        ArrayList<Transactions> transactions = new ArrayList<>();
        TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
        transactions = dao.getUserTransactions(this.id);

        //IF NONE
        if (transactions.size() == 0){
            System.out.println("No transactions for this account.");
        }

        //PRINT EM ALL
        else {
            for(Transactions transaction : transactions) {
                System.out.println(transaction);
            }
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter anything to quit: ");
        scan.next();

    }

    //ADD TRANSACTION REFLECTION ACCEPTED, ADDING TO RECEIVING ACCOUNT AND SUBTRACTING FROM TRANSFERRING
    void handleAccept(Transactions transaction) throws SQLException {
        //ADD DEPOSIT, EDIT PENDING
        TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
        AccountsDao dao1 = AccountsDaoFactory.getAccountsDao();
        double balance_before = balance;
        balance = balance + -transaction.getAmount();
        pending_receive = pending_receive + transaction.getAmount();
        dao.acceptTransaction(transaction.getId());
        Transactions transaction1 = new Transactions(user_id,id,"accepted transfer",-transaction.getAmount(),balance_before,balance);
        dao.addTransaction(transaction1);

        //ADD WITHDRAW, EDIT PENDING
        Accounts account = dao1.getAccountsByID(transaction.getAccount_id());
        Transactions transaction2 = new Transactions(account.getUser_id(),account.get_id(),"accepted transfer",transaction.getAmount(),account.getBalance(),account.getBalance() + transaction.getAmount());
        dao.addTransaction(transaction2);
        account.setPending_transfer(account.getPending_transfer() + transaction.getAmount());
        account.setBalance(account.getBalance() + transaction.getAmount());
        dao1.updateAccount(account);
        dao1.updateAccount(this);
        theLogger.debug("Transfer accepted made by User " + this.getUser_id());
    }

    //ADD TRANSACTION REFLECTION REJECTED, DOING NOTHING TO RECEIVING ACCOUNT AND ADDING BACK INTO TRANSFERRING
    void handleReject(Transactions transaction) throws SQLException {
        //EDIT PENDING RECEIVE FOR RECEIVING ACCOUNT
        TransactionsDao dao = TransactionsDaoFactory.getTransactionDao();
        AccountsDao dao1 = AccountsDaoFactory.getAccountsDao();
        pending_receive = pending_receive + transaction.getAmount();
        dao.rejectTransaction(transaction.getId());

        //EDIT PENDING TRANSFER TO ACCOUNT SENT FROM
        Accounts account1 = dao1.getAccountsByID(transaction.getAccount_id());
        Transactions transaction3 = new Transactions(account1.getUser_id(),account1.get_id(),"rejected transfer",-transaction.getAmount(), account1.getBalance(), account1.getBalance());
        dao.addTransaction(transaction3);
        account1.setPending_transfer(account1.getPending_transfer() + transaction.getAmount());
        dao1.updateAccount(account1);
        theLogger.debug("Transfer rejection made by User " + this.getUser_id());
    }

    //MAKE SURE DEPOSIT DOUBLE
    void tryDeposit() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Amount to Deposit: ");
        try {
            double amount = scan.nextDouble();
            deposit(amount);
        }
        catch (Exception e){
            System.out.println("Im sorry, you cannot deposit whatever that amount was.");
        }
    }

    //MAKE SURE WITHDRAW DOUBLE
    void tryWithdraw() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Amount to Withdrawal: ");
        try {
            double withdrawal_amount = scan.nextDouble();
            withdraw(withdrawal_amount);
        }
        catch (Exception e){
            System.out.println("Im sorry, you cannot withdraw whatever that amount was.");
        }
    }

    //MAKE SURE CORRECT TYPE FOR SENDING
    void tryTransfer() {
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("Enter Account ID to send money to: ");
            int acc_id = scan.nextInt();
            System.out.println("Enter Amount to Send: ");
            double send_amount = scan.nextDouble();
            sendMoney(acc_id,send_amount);
        }
        catch (Exception e) {
            System.out.println("Im sorry, you cannot transfer whatever that amount was.");
        }
    }
}