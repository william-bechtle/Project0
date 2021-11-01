package com.thevarungupta;

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
    Exceptions exceptions = new Exceptions();

    Accounts(){
    }

    Accounts(int user_id,String account_type,double balance,double pending_receive, double pending_transfer, boolean pending_activation){
        this.user_id = user_id;
        this.account_type = account_type;
        this.balance = balance;
        this.pending_receive = pending_receive;
        this.pending_transfer = pending_transfer;
        this.pending_activation = pending_activation;
    }

    Accounts(int id, int user_id,String account_type,double balance,double pending_receive, double pending_transfer, boolean pending_activation){
        this.id = id;
        this.user_id = user_id;
        this.account_type = account_type;
        this.balance = balance;
        this.pending_receive = pending_receive;
        this.pending_transfer = pending_transfer;
        this.pending_activation = pending_activation;
    }

    public int get_id(){
        return id;
    }

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

    @Override
    public String toString(){
        return "------------------Account: " + this.id + "------------------\nBalance: $" + this.balance + "\nType: " + this.account_type + "\nPending Approval: $" + this.pending_receive
                + "\nPending Transfer: $" + this.pending_transfer;
    }


    void account_menu_for_employee(Accounts account) throws SQLException {
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println(account);
            System.out.println("1 - View Transactions");
            System.out.println("2 - Quit");
            System.out.println("Enter option: ");
            String option = scan.nextLine();
            switch (option) {
                case "1":
                    get_transactions();
                    break;
                case "2":
                    exit = true;
                    break;
                default:
                    break;
            }
        }
    }

    void account_menu_for_customer(Accounts account) throws SQLException, Exceptions.depositNegative, Exceptions.withdrawPositive, Exceptions.negativeBalance {
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println(account);
            System.out.println("1 - View Transactions");
            System.out.println("2 - Deposit");
            System.out.println("3 - Withdrawal");
            System.out.println("4 - Quit");
            System.out.println("Enter option: ");
            String option = scan.next();
            switch (option) {
                case "1":
                    get_transactions();
                    break;
                case "2":
                    System.out.println("Enter Amount to Deposit: ");
                    try {
                        double amount = scan.nextDouble();
                        deposit(amount);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                    break;
                case "3":
                    System.out.println("Enter Amount to Withdrawal: ");
                    try {
                        double withdrawal_amount = scan.nextDouble();
                        withdraw(withdrawal_amount);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    break;
            }
        }
    }

    void deposit(double amount) throws Exceptions.depositNegative, SQLException {
        try {
            if (amount < 0) {
                throw new Exceptions.depositNegative();
            } else {
                double balance_before = balance;
                balance = balance + amount;
                Transactions transaction = new Transactions(user_id, id, "deposit", amount, balance_before, balance);
                TransactionsDao dao = TransactionsDaoFactory.getEmployeeDao();
                dao.addTransaction(transaction);
                AccountsDao dao1 = AccountsDaoFactory.getAccountsDao();
                dao1.updateAccount(this);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    void withdraw(double amount) throws Exceptions.withdrawPositive, Exceptions.negativeBalance, SQLException {
        try {
            if (amount < 0) {
                throw new Exceptions.withdrawPositive();
            }
            else if (amount > balance) {
                throw new Exceptions.negativeBalance();
            }
            else {
                double balance_before = balance;
                balance = balance - amount;
                Transactions transaction = new Transactions(user_id, id, "withdrawal", amount, balance_before, balance);
                TransactionsDao dao = TransactionsDaoFactory.getEmployeeDao();
                dao.addTransaction(transaction);
                AccountsDao dao1 = AccountsDaoFactory.getAccountsDao();
                dao1.updateAccount(this);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    void get_transactions() throws SQLException {
        ArrayList<Transactions> transactions = new ArrayList<>();
        TransactionsDao dao = TransactionsDaoFactory.getEmployeeDao();
        transactions = dao.getUserTransactions(this.id);
        if (transactions.size() == 0){
            System.out.println("No transactions for this account.");
        }
        else {
            for(Transactions transaction : transactions) {
                System.out.println(transaction);
            }
        }

    }
}