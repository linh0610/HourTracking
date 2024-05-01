package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import persistence.WritableArray;

import java.util.List;
import java.util.ArrayList;

//represent a list of all accounts
public class ListOfAccounts implements WritableArray {
    private ArrayList<Account> list;
    private EventLog eventLog;

    // effects: constructs a new ListOfAccounts object
    public ListOfAccounts() {
        list = new ArrayList<>();
        eventLog = EventLog.getInstance();
    }

    // effects: searches for an account with the given username
    public Account searchAccount(String username) {
        for (int i = 0; i < list.size(); i++) {
            if (username.equals(list.get(i).getUsername())) {
                return list.get(i);
            }
        }
        return null;
    }

    // requires: account != null
    // modifies: this
    // effects: adds an account to the list if the username is not already taken
    public void addAccount(Account account) {
        boolean usernameTaken = false;
        for (int i = 0; i < list.size(); i++) {
            if (account.getUsername().equals(list.get(i).getUsername())) {
                System.out.println("The username is already taken");
                usernameTaken = true;
                break;
            }
        }
        if (!usernameTaken) {
            list.add(account);
            eventLog.logEvent(new Event("Account added: " + account.getUsername()));
        }
    }

    // effects: returns the number of accounts in the list
    public int numOfAccount() {
        return list.size();
    }

    // EFFECTS: returns the list of accounts
    public ArrayList<Account> getList() {
        return list;
    }

    // Modifies: this
    // Effects: returns a JSONArray representing the list of accounts
    @Override
    public JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        for (Account account : list) {
            jsonArray.put(account.toJson());
        }
        return jsonArray;
    }
}
