package ui;

import model.Account;
import model.EventLog;
import model.Event;
import model.ListOfAccounts;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import persistence.JsonReader;
import persistence.JsonWriter;
import org.jdatepicker.JDatePicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Scanner;

/**
 * Represents the graphical user interface for the Hour Tracking Application.
 * Allows users to sign in, register, clock in/out, view hours worked, and quit the application.
 */
public class GUI extends JFrame {
    private boolean isDarkMode = false;
    private static final Color LIGHT_MODE_BACKGROUND = Color.WHITE;
    private static final Color LIGHT_MODE_TEXT = Color.BLACK;
    private static final Color DARK_MODE_BACKGROUND = Color.DARK_GRAY;
    private static final Color DARK_MODE_TEXT = Color.WHITE;
    private static final String JSON_STORE = "./data/accounts.json";
    private ListOfAccounts listOfAccounts = new ListOfAccounts();
    private Account account;
    private LocalTime clockInTime;
    private Timer timer;
    private LocalTime clockOutTime;
    private JLabel timerLabel;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private EventLog eventLog = EventLog.getInstance();

    private JPanel mainPanel;
    private JFrame frame2;
    private JButton button;
    private JButton signInButton;
    private JButton registerButton;
    private JButton quitButton;

    /**
     * Constructs the GUI for the Hour Tracking Application.
     * Effects: Initializes the GUI components and loads existing account data if available.
     */
    public GUI() {
        super("Hour Tracking Application");
        initialize();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        loadListOfAccounts();
    }

    // MODIFIES: this
    // EFFECTS: Initializes the main panel and sets up the buttons for signing in, registering, and quitting
    private void initialize() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        //mainPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        //mainPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);

        signInButton = setButtons("Sign in");
        registerButton = setButtons("Register");
        quitButton = setButtons("Quit");
        JButton modeToggleButton = setButtons("Toggle theme");
        modeToggleButton.addActionListener(e -> toggleMode());

        signInButton.addActionListener(e -> signIn());
        registerButton.addActionListener(e -> register());
        quitButton.addActionListener(e -> quit());

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(modeToggleButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(signInButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(registerButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(quitButton);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    // MODIFIES: isDarkMode, mainPanel
    // EFFECTS: Toggles between dark mode and light mode, and applies corresponding UI changes
    private void toggleMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            applyDarkMode();
        } else {
            applyLightMode();
        }
    }

    // EFFECTS: Prints all logged events since the application started
    private void printEventLog() {
        System.out.println("Logged events:");
        for (Event event : eventLog) {
            System.out.println(event.getDate() + ": " + event.getDescription());
        }
        eventLog.clear();
    }

    // MODIFIES: mainPanel
    // EFFECTS: Changes the background and foreground colors of the main panel to reflect dark mode
    private void applyDarkMode() {
        mainPanel.setBackground(DARK_MODE_BACKGROUND);
        mainPanel.setForeground(DARK_MODE_TEXT);
    }

    // MODIFIES: mainPanel
    // EFFECTS: Changes the background and foreground colors of the main panel to reflect light mode
    private void applyLightMode() {
        mainPanel.setBackground(LIGHT_MODE_BACKGROUND);
        mainPanel.setForeground(LIGHT_MODE_TEXT);
    }

    // EFFECTS: Creates and returns a JButton with the given text, aligned to the center
    private JButton setButtons(String t) {
        button = new JButton(t);
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        return button;
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to load the list of accounts from file if the file exists;
    //          otherwise initializes a new list of accounts
    private void loadListOfAccounts() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to load the accounts?",
                "Load Accounts",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                listOfAccounts = jsonReader.read();
                JOptionPane.showMessageDialog(this,
                        "Loaded accounts from " + JSON_STORE,
                        "Load Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Unable to read from file: " + JSON_STORE,
                        "Load Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        eventLog.clear();
    }

    // MODIFIES: this
    // EFFECTS: Saves the list of accounts to file
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

    // MODIFIES: this
    // EFFECTS: Signs in the user with the given username
    private void signIn() {
        String username = promptForUsername();
        if (username != null && !username.isEmpty()) {
            account = listOfAccounts.searchAccount(username);
            if (account != null) {
                signInWithPassword();
            } else {
                JOptionPane.showMessageDialog(this, "Username not found", "Error", JOptionPane.ERROR_MESSAGE);
                this.register();
            }
        }
        createClockInAndOutFrame();
    }

    // EFFECTS: Prompts the user to input their username
    private String promptForUsername() {
        return JOptionPane.showInputDialog(this, "Please input your username:");
    }

    // MODIFIES: this
    // EFFECTS: Authenticates user with password
    private void signInWithPassword() {
        boolean correct = true;
        while (correct) {
            String password = promptForPassword();
            if (password != null) {
                if (account.getPassword().equals(password)) {
                    correct = false;
                    viewHoursWorkedAndWage(account);
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return;
            }
        }
    }

    // EFFECTS: Prompts the user to input their password
    private String promptForPassword() {
        return JOptionPane.showInputDialog(this, "Please input your password:");
    }

    // EFFECTS: Creates and returns a JLabel with the given text
    private JLabel setLabel(String text) {
        timerLabel = new JLabel(text);
        timerLabel.setAlignmentX(timerLabel.CENTER_ALIGNMENT);
        return timerLabel;
    }


    // MODIFIES: this
    // EFFECTS: Creates the clock in and out frame
    private void createClockInAndOutFrame() {
        JFrame frame = new JFrame("Clock In/Out");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton clockInButton = setButtons("Clock in");
        JButton clockOutButton = setButtons("Clock out");
        timerLabel = setLabel("Time Elapsed: 00:00:00");
        JButton viewButton = setButtons("View Hours Worked");

        clockInButton.addActionListener(e -> clockIn(frame));
        clockOutButton.addActionListener(e -> clockOut(account, frame));
        viewButton.addActionListener(e -> viewHoursWorkedAndWage(account));

        panel.add(Box.createVerticalGlue());
        panel.add(clockInButton);
        panel.add(clockOutButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(viewButton);

        panel.add(timerLabel);
        panel.add(Box.createVerticalGlue());

        frame.add(panel);
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    // MODIFIES: this
    // EFFECTS: Registers a new account with username and password
    private void register() {
        String username = JOptionPane.showInputDialog(this, "Please input a valid email as your username:");
        if (username != null && !username.isEmpty()) {
            if (listOfAccounts.searchAccount(username) != null) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return; // Cancel registration if username already exists
            }
            String password = JOptionPane.showInputDialog(this,
                    "Please input a password with at least one uppercase letter and numbers:");
            if (password != null && !password.isEmpty()) {
                account = new Account(username, password);
                setAccountWage();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets the wage for the current account
    private void setAccountWage() {
        double wage = 0;
        boolean validWage = false;
        while (!validWage) {
            try {
                String wageInput = JOptionPane.showInputDialog(this, "Please input your starting wage per hour:");
                if (wageInput != null && !wageInput.isEmpty()) {
                    wage = Double.parseDouble(wageInput);
                    validWage = true;
                } else {
                    return; // Cancel registration if wage input is canceled
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for wage",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        account.setWagePerHour(wage);
        setAccountShift();
    }

    // EFFECTS: Allows user to choose starting and ending time for shift
    private String chooseShiftTime() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel startTimeLabel = new JLabel("Starting Time:");
        JSpinner startTimeSpinner = createSpinner();

        JLabel endTimeLabel = new JLabel("Ending Time:");
        JSpinner endTimeSpinner = createSpinner();

        panel.add(startTimeLabel);
        panel.add(startTimeSpinner);
        panel.add(endTimeLabel);
        panel.add(endTimeSpinner);

        int result = JOptionPane.showConfirmDialog(null, panel, "Set Shift Time", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date startTime = (Date) startTimeSpinner.getValue();
            Date endTime = (Date) endTimeSpinner.getValue();
            // Format the start and end time as desired
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String shiftTime = "Start: " + timeFormat.format(startTime) + ", End: " + timeFormat.format(endTime);
            return shiftTime;
        } else {
            return null; // Cancelled
        }
    }


    // MODIFIES: this
    // EFFECTS: Creates and returns a JSpinner with a SpinnerDateModel and HH:mm format
    private JSpinner createSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
        spinner.setEditor(editor);
        return spinner;
    }

    // MODIFIES: this, account
    // EFFECTS: Prompts user to select shift date and time, sets the shift for the account,
    //          and adds the account to the list of accounts
    private void setAccountShift() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setTextEditable(true);

        int option = JOptionPane.showConfirmDialog(this, datePicker, "Please select the shift date:",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String shiftDate = dateFormat.format(selectedDate);

            String shiftTime = chooseShiftTime();
            if (shiftTime != null) {
                String shift = shiftDate + " " + shiftTime;
                account.setShift(shift);
                listOfAccounts.addAccount(account);
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Prompts user to save data before exiting and exits the application and print the event log
    private void quit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to save your data?",
                "Exiting the Hour Tracking Application",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            saveListOfAccounts();
        }
        printEventLog();
        System.exit(0);
    }

    // MODIFIES: this, frame, timerLabel
    // EFFECTS: Records clock in time, starts a timer to update elapsed time, and displays clock in message
    private void clockIn(JFrame frame) {
        clockInTime = LocalTime.now();
        JOptionPane.showMessageDialog(frame, "Clocked in at: " + clockInTime);
        //frame.dispose();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Duration duration = Duration.between(clockInTime, LocalTime.now());
                long hours = duration.toHours();
                long minutes = duration.toMinutesPart();
                long seconds = duration.toSecondsPart();

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timerLabel.setText("Time Elapsed: " + timeString);
            }
        });
        timer.start();
        frame2 = new JFrame("DVD screen saver");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.getContentPane().add(new BouncingBox());
        frame2.pack();
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
    }

    // MODIFIES: this, account, frame, timer
    // EFFECTS: Records clock out time, calculates hours worked, stops the timer,
    //          and displays clock out message with hours worked
    private void clockOut(Account account, JFrame frame) {
        if (clockInTime == null) {
            System.out.println("Error: Please clock in first.");
            JOptionPane.showMessageDialog(frame, "Error: Please clock in first.");
            //frame.dispose();
        } else {
            clockOutTime = LocalTime.now();
            JOptionPane.showMessageDialog(frame, "Clocked out at: " + clockOutTime);
            //frame.dispose();
            account.setHoursWorked(calculateWorkHours());
            clockInTime = null;
            timer.stop();
        }
        frame2.dispose();
    }

    // EFFECTS: Calculates and returns the total hours worked based on clock in and clock out times
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

    // EFFECTS: Displays a dialog with the total hours worked
    private void viewHoursWorkedAndWage(Account account) {
        double hoursWorked = calculateWorkHours();
        JOptionPane.showMessageDialog(this, "Hours worked: " + account.getHoursWorked(), "Hours Worked",
                JOptionPane.INFORMATION_MESSAGE);
    }


}