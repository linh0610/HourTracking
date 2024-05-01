package persistence;

import model.Account;
import model.ListOfAccounts;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

//represent a writer to write data into a json file
public class JsonWriter implements AutoCloseable {
    private String destination;
    private FileWriter writer;

    // EFFECTS: constructs a writer to write to the given destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer; throws IOException if writer cannot be opened
    public void open() throws IOException {
        writer = new FileWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of list of accounts to file
    public void write(ListOfAccounts listOfAccounts) throws FileNotFoundException {
        JSONObject jsonObject = listOfAccountsToJson(listOfAccounts);
        saveToFile(jsonObject.toString(4));
    }

    // EFFECTS: converts list of accounts to JSON object and returns it
    private JSONObject listOfAccountsToJson(ListOfAccounts listOfAccounts) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Account account : listOfAccounts.getList()) {
            jsonArray.put(accountToJson(account));
        }
        jsonObject.put("accounts", jsonArray);
        return jsonObject;
    }

    // EFFECTS: converts account to JSON object and returns it
    private JSONObject accountToJson(Account account) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", account.getUsername());
        jsonObject.put("password", account.getPassword());
        jsonObject.put("wagePerHour", account.getWagePerHour());
        jsonObject.put("shift", account.getShift());
        jsonObject.put("hoursWorked", account.getHoursWorked());
        return jsonObject;
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(destination));
        writer.print(json);
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    @Override
    public void close() throws IOException {
        writer.close();
    }
}
