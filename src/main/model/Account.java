package model;

import org.json.JSONObject;
import persistence.Writable;

//represent an account
public class Account implements Writable {
    private String username;
    private String password;
    private double wagePerHour;
    private String shift;
    private double hoursWorked;
    private EventLog eventLog;

    // requires: username and password are not null
    // modifies: this
    // effects: initializes the Account object with the given username and password
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.wagePerHour = 0;
        this.shift = null;
        this.hoursWorked = 0;
        eventLog = EventLog.getInstance();
    }

    // effects: returns the shift of the Account
    public String getShift() {
        return shift;
    }

    // effects: returns the wage per hour of the Account
    public double getWagePerHour() {
        return wagePerHour;
    }

    // effects: returns the username of the Account
    public String getUsername() {
        return username;
    }

    // effects: returns the password of the Account
    public String getPassword() {
        return password;
    }

    // requires: username is not null
    // modifies: this
    // effects: sets the username of the Account to the given username
    public void setUsername(String username) {
        this.username = username;
    }

    // requires: password is not null
    // modifies: this
    // effects: sets the password of the Account to the given password
    public void setPassword(String password) {
        this.password = password;
    }

    // modifies: this
    // effects: sets the wage per hour of the Account to the given wage per hour
    public void setWagePerHour(double wagePerHour) {
        this.wagePerHour = wagePerHour;
    }

    // modifies: this
    // effects: sets the shift of the Account to the given shift
    public void setShift(String shift) {
        this.shift = shift;
        eventLog.logEvent(new Event("Shift set at " + this.shift + " for account: " + this.getUsername()));
    }

    // modifies: this
    // effects: sets the hours worked of the Account to the given hours
    public void setHoursWorked(double hours) {
        this.hoursWorked = hours;
        eventLog.logEvent(new Event("Wage set at " + this.wagePerHour + " for account: " + this.getUsername()));
    }

    // effects: returns the hours worked of the Account
    public double getHoursWorked() {
        //eventLog.logEvent(new Event("Hours worked viewed for account: " + this.getUsername()));
        return this.hoursWorked;
    }

    // requires: username is not null
    // modifies: this
    // effects: Validates the username by ensuring it contains "@" symbol
    public boolean validUsername(String username) {
        return  (username.contains("@") && username.contains(".com") || username.contains(".ca"));
    }


    // requires: password is not null
    // modifies: this
    // effects: Validates the password by ensuring it contains at least one uppercase letter and one digit
    public boolean validPassword(String password) {
        return (containsUpperCase(password) && containsNumber(password));
    }


    // effects: Checks if the string contains at least one uppercase letter
    public boolean containsUpperCase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    // effects: Checks if the string contains at least one digit
    public boolean containsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    // Modifies: this
    // Effects: returns a JSONObject representing the account
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        json.put("wagePerHour", wagePerHour);
        json.put("shift", shift);
        json.put("hoursWorked", hoursWorked);
        return json;
    }
}


