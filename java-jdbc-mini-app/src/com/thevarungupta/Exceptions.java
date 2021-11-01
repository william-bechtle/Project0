package com.thevarungupta;

public class Exceptions extends Exception {
    depositNegative depositNegative = new depositNegative();
    Exceptions() {
        super();
    }

    public static class depositNegative extends Exception {
        depositNegative() {
            super("CANNOT DEPOSIT A NEGATIVE AMOUNT.");
        }
    }

    public static class withdrawPositive extends Exception {
        withdrawPositive() {
            super("CANNOT WITHDRAW A NEGATIVE AMOUNT.");
        }
    }

    public static class negativeBalance extends Exception {
        negativeBalance() {
            super("TRANSACTION IS MORE THAN ACCOUNT BALANCE.");
        }
    }
}