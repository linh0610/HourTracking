package ui;

import model.Account;
import model.ListOfAccounts;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

//the main ui of the app
public class HourTrackingApp {
    private static final String JSON_STORE = "./data/accounts.json";
    private ListOfAccounts listOfAccounts = new ListOfAccounts();
    private Account account;
    private LocalTime clockInTime;
    private LocalTime clockOutTime;
    private Scanner in = new Scanner(System.in);
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // effects: Constructs a HourTrackingApp object and runs the application
    public HourTrackingApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // modifies: this
    // effects: Runs the hour tracking application
    private void runApp() {
        System.out.println("Would you like to load data from file");
        String choice0 = in.next();
        if (choice0.equalsIgnoreCase("Y")) {
            loadListOfAccounts();
        }
        in.nextLine();

        while (true) {
            this.menu();
            System.out.println(" Do you want to clock out now? ");
            String choice = in.next();
            if (choice.charAt(0) == 'y' || choice.charAt(0) == 'Y') {
                this.clockOut(account);
                in.nextLine();
            } else if (choice.charAt(0) == 'n' || choice.charAt(0) == 'N') {
                System.out.println("Ok");
                in.nextLine();

            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads list of accounts from file, if file exists; otherwise initializes new list
    private void loadListOfAccounts() {
        try {
            listOfAccounts = jsonReader.read();
            System.out.println("Loaded accounts from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Saves list of accounts to file
    private void saveListOfAccounts() {
        try {
            jsonWriter.open();
            jsonWriter.write(listOfAccounts);
            jsonWriter.close();
            System.out.println("Saved accounts to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // modifies: this
    // effects: Displays the menu of options and handles user input
    private void menu() {
        boolean keepGoing = true;
        while (keepGoing) {
            this.displayMenu();

            String command = in.nextLine();
            if (command.equals("Sign in") || command.equals("sign in")) {
                this.signIn();
            } else if (command.equals("register") || command.equals("Register")) {
                this.register();
            } else if (command.equals("quit") || command.equals("Quit")) {
                this.quit();
            } else {
                System.out.println("Invalid input");
            }
            System.out.println("Do you want to clock in(Y/N): ");
            String choice = in.next();
            if (choice.charAt(0) == 'y' || choice.charAt(0) == 'Y') {
                this.clockIn();
                keepGoing = false;
            } else if (choice.charAt(0) == 'n' || choice.charAt(0) == 'N') {
                System.out.println("Ok");
                keepGoing = false;
            }
        }

    }

    // effects: Displays the menu of options
    private void displayMenu() {
        System.out.println("\nPlease select a choice and input the exact choice:");
        System.out.println("\tSign in");
        System.out.println("\tRegister");
        System.out.println("\tQuit");
    }

    // effects: Prints a message indicating the application is exiting and terminates the program
    public void quit() {
        System.out.println("Exiting the Hour Tracking Application...");
        System.out.println("Would you like to save your data:");
        String choice0 = in.next();
        if (choice0.equalsIgnoreCase("Y")) {
            saveListOfAccounts();
        }
        System.exit(0);
    }

    // effects: sign in an existing account
    private void signIn() {
        System.out.println("Please in put your username");
        in = new Scanner(System.in);
        String username = in.nextLine();
        account = listOfAccounts.searchAccount(username);
        if (account != null) {
            boolean correct = true;
            while (correct) {
                System.out.println("Please input your password");
                String password = in.nextLine();
                if (account.getPassword().equals(password)) {
                    correct = false;
                } else {
                    System.out.println("Wrong password");
                    password = in.nextLine();
                }
            }
            viewHoursWorkedAndWage();
        } else {
            System.out.println("Please register instead");
            this.register();
        }

    }

    // effects: Registers a new account
    private void register() {
        System.out.println("Please include an valid email as your user name: ");
        in = new Scanner(System.in);
        String username = in.next();
        System.out.println("Please input a password with at least one Uppercase letter and numbers: ");
        String password = in.next();
        System.out.println("Please input your starting wage per hour: ");
        double wage = in.nextDouble();
        System.out.println("Please include your shift for this month with date: ");
        String shift = in.next();
        account = new Account(username, password);
        account.setWagePerHour(wage);
        account.setShift(shift);
        listOfAccounts.addAccount(account);
    }



    // effects: Records the clock-in time
    private void clockIn() {
        clockInTime = LocalTime.now();
        System.out.println("Clocked in at: " + clockInTime);
    }

    // effects: Records the clock-out time and calculates the hours worked
    private void clockOut(Account account) {
        if (clockInTime == null) {
            System.out.println("Error: Please clock in first.");
        } else {
            clockOutTime = LocalTime.now();
            System.out.println("Clocked out at: " + clockOutTime);
            account.setHoursWorked(calculateWorkHours());
            clockInTime = null;
        }
    }

    // effects: Calculates the hours worked based on clock-in and clock-out times
    private double calculateWorkHours() {
        String hoursWorked;
        int hours = 0;
        int minutes = 0;
        if (clockInTime != null && clockOutTime != null) {
            hours = clockOutTime.getHour() - clockInTime.getHour();
            minutes = clockOutTime.getMinute() - clockInTime.getMinute();
            if (minutes < 0) {
                hours--;
                minutes += 60;
            }
        }
        hoursWorked = hours + "." + minutes;
        return Double.parseDouble(hoursWorked);
    }

    // effects: View hours worked and wage per hour
    private void viewHoursWorkedAndWage() {
        System.out.println("Do you want to view your hours worked and wage per hour? (Y/N): ");
        String choice = in.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("Hours worked: " + account.getHoursWorked());
            System.out.println("Wage per hour: " + account.getWagePerHour());
        }
    }
}

