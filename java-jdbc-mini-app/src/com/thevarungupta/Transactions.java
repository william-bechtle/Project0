package com.thevarungupta;

import java.sql.SQLException;
import java.util.Scanner;

public class Transactions {
    private int id;
    private int user_id;
    private int account_id;
    private String type;
    private double amount;
    private double balance_before;
    private double balance_after;
    private int sent_to;

    Transactions() {
    }

    Transactions(int user_id, int account_id, String type, double amount, double balance_before, double balance_after, int sent_to) {
        this.user_id = user_id;
        this.account_id = account_id;
        this.type = type;
        this.amount = amount;
        this.balance_before = balance_before;
        this.balance_after = balance_after;
        this.sent_to = sent_to;
    }

    Transactions(int user_id, int account_id, String type, double amount, double balance_before, double balance_after) {
        this.user_id = user_id;
        this.account_id = account_id;
        this.type = type;
        this.amount = amount;
        this.balance_before = balance_before;
        this.balance_after = balance_after;
    }

    Transactions(int user_id, int account_id, String type, double amount, int sent_to) {
        this.user_id = user_id;
        this.account_id = account_id;
        this.type = type;
        this.amount = amount;
        this.sent_to = sent_to;
    }

    Transactions(int id, int user_id, int account_id, String type, double amount, double balance_before, double balance_after, int sent_to) {
        this.id = id;
        this.user_id = user_id;
        this.account_id = account_id;
        this.type = type;
        this.amount = amount;
        this.balance_before = balance_before;
        this.balance_after = balance_after;
        this.sent_to = sent_to;
    }

    public int getId(){
        return id;
    }

    public int getUser_id(){
        return user_id;
    }

    public int getAccount_id(){
        return account_id;
    }

    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }

    public double getBalance_before() {
        return balance_before;
    }

    public double getBalance_after() {
        return balance_after;
    }

    public int getSent_to(){
        return sent_to;
    }

    @Override
    public String toString(){
        return "ID: " + id + ", User: " + user_id + ", Account: " + account_id + ", Type: " + type + ", Amount: " + amount
                + ", Balance Before: " + balance_before + ", Balance After: " + balance_after;
    }
}