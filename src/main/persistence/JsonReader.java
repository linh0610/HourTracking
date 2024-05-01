package persistence;

import model.Account;
import model.ListOfAccounts;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

//represent a reader to read data from a json file
public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader to read from the given source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads list of accounts from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ListOfAccounts read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseListOfAccounts(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        return Files.lines(Paths.get(source)).collect(Collectors.joining());
    }

    // EFFECTS: parses list of accounts from JSON object and returns it
    private ListOfAccounts parseListOfAccounts(JSONObject jsonObject) {
        ListOfAccounts listOfAccounts = new ListOfAccounts();
        JSONArray jsonArray = jsonObject.getJSONArray("accounts");
        for (Object json : jsonArray) {
            JSONObject nextAccount = (JSONObject) json;
            addAccount(listOfAccounts, nextAccount);
        }
        return listOfAccounts;
    }

    // MODIFIES: listOfAccounts
    // EFFECTS: parses account from JSON object and adds it to list of accounts
    private void addAccount(ListOfAccounts listOfAccounts, JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        double wagePerHour = jsonObject.getDouble("wagePerHour");
        double hoursWorked = jsonObject.getDouble("hoursWorked");
        Account account = new Account(username, password);
        account.setWagePerHour(wagePerHour);
        if (jsonObject.has("shift")) {
            String shift = jsonObject.getString("shift");
            account.setShift(shift);
        }
        account.setHoursWorked(hoursWorked);
        listOfAccounts.addAccount(account);


    }
}
