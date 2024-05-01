package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListOfAccountsTest {

    private ListOfAccounts listOfAccounts;

    @Test
    void testAddAccount() {
        ListOfAccounts listOfAccounts = new ListOfAccounts();

        Account account1 = new Account("user1", "password1");
        Account account2 = new Account("user2", "password2");
        Account account3 = new Account("user1", "password3"); // Duplicate username

        listOfAccounts.addAccount(account1);
        assertEquals(1, listOfAccounts.numOfAccount());

        listOfAccounts.addAccount(account2);
        assertEquals(2, listOfAccounts.numOfAccount());

        listOfAccounts.addAccount(account3);
        assertEquals(2, listOfAccounts.numOfAccount()); // Number of accounts should not change

        assertNotNull(listOfAccounts.searchAccount("user1"));
        assertNotNull(listOfAccounts.searchAccount("user2"));
    }

    @Test
    void testSearchAccount() {
        ListOfAccounts listOfAccounts = new ListOfAccounts();

        Account account1 = new Account("user1", "password1");
        Account account2 = new Account("user2", "password2");

        listOfAccounts.addAccount(account1);
        listOfAccounts.addAccount(account2);

        assertNotNull(listOfAccounts.searchAccount("user1")); // Existing account
        assertNotNull(listOfAccounts.searchAccount("user2")); // Existing account
        assertNull(listOfAccounts.searchAccount("user3")); // Non-existing account
    }

    @Test
    void testNumOfAccount() {
        ListOfAccounts listOfAccounts = new ListOfAccounts();

        assertEquals(0, listOfAccounts.numOfAccount());

        listOfAccounts.addAccount(new Account("user1", "password1"));
        listOfAccounts.addAccount(new Account("user2", "password2"));

        assertEquals(2, listOfAccounts.numOfAccount());

        listOfAccounts.addAccount(new Account("user3", "password3"));

        assertEquals(3, listOfAccounts.numOfAccount());
    }

    public void testGetListEmpty() {
        listOfAccounts = new ListOfAccounts();
        ArrayList<Account> accounts = listOfAccounts.getList();
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void testGetListNonEmpty() {
        listOfAccounts = new ListOfAccounts();
        Account account1 = new Account("user1", "password1");
        Account account2 = new Account("user2", "password2");

        listOfAccounts.addAccount(account1);
        listOfAccounts.addAccount(account2);

        ArrayList<Account> accounts = listOfAccounts.getList();
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(account1));
        assertTrue(accounts.contains(account2));
    }

    @Test
    void testToJson() {
        // Create a ListOfAccounts object
        ListOfAccounts listOfAccounts = new ListOfAccounts();

        // Create some Account objects and add them to the ListOfAccounts
        Account account1 = new Account("user1", "pass1");
        Account account2 = new Account("user2", "pass2");
        account1.setWagePerHour(15.0);
        account1.setShift("Morning");
        account1.setHoursWorked(8.5);
        account2.setWagePerHour(20.0);
        account2.setShift("Evening");
        account2.setHoursWorked(7.5);
        listOfAccounts.addAccount(account1);
        listOfAccounts.addAccount(account2);

        // Generate expected JSON array manually
        JSONArray expectedJsonArray = new JSONArray();
        JSONObject expectedJson1 = new JSONObject();
        expectedJson1.put("username", "user1");
        expectedJson1.put("password", "pass1");
        expectedJson1.put("wagePerHour", 15.0);
        expectedJson1.put("shift", "Morning");
        expectedJson1.put("hoursWorked", 8.5);
        JSONObject expectedJson2 = new JSONObject();
        expectedJson2.put("username", "user2");
        expectedJson2.put("password", "pass2");
        expectedJson2.put("wagePerHour", 20.0);
        expectedJson2.put("shift", "Evening");
        expectedJson2.put("hoursWorked", 7.5);
        expectedJsonArray.put(expectedJson1);
        expectedJsonArray.put(expectedJson2);

        // Generate JSON array using toJson() method
        JSONArray actualJsonArray = listOfAccounts.toJson();

        // Compare the generated JSON array with the expected JSON array
        assertEquals(expectedJsonArray.toString(), actualJsonArray.toString());
    }
}

