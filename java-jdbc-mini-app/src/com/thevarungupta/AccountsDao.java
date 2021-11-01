package com.thevarungupta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface AccountsDao {
    ArrayList<Accounts> getUserAccounts(User account) throws SQLException;

    ArrayList<Accounts> getUserAccountsByID(int id) throws SQLException;

    Accounts getAccountsByID(int id) throws SQLException;

    void addAccount(Accounts account) throws SQLException;

    ArrayList<Accounts> getApprovalList() throws SQLException;

    void updateAccount(Accounts account) throws SQLException;

}