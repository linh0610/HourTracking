package persistence;

import model.Account;
import model.ListOfAccounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private ListOfAccounts listOfAccounts;
    private Account account1;
    private Account account2;

    @BeforeEach
    void runBefore() {
        listOfAccounts = new ListOfAccounts();
        account1 = new Account("user1", "password123");
        account1.setShift("Morning"); // Account with "shift" field
        account2 = new Account("user2", "password456");
        listOfAccounts.addAccount(account1);
        listOfAccounts.addAccount(account2);
    }

    @Test
    public void testWriterInvalidFile() {
        try {
            ListOfAccounts listOfAccounts = new ListOfAccounts();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyListOfAccounts() {
        try {
            ListOfAccounts listOfAccounts = new ListOfAccounts();
            JsonWriter writer = new JsonWriter("./data/testEmptyListOfAccounts.json");
            writer.open();
            writer.write(listOfAccounts);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyListOfAccounts.json");
            listOfAccounts = reader.read();
            assertEquals(0, listOfAccounts.numOfAccount());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneralListOfAccounts() {
        try {
            ListOfAccounts listOfAccounts = new ListOfAccounts();
            Account account1 = new Account("username1", "password1");
            Account account2 = new Account("username2", "password2");
            listOfAccounts.addAccount(account1);
            listOfAccounts.addAccount(account2);
            JsonWriter writer = new JsonWriter("./data/testGeneralListOfAccounts.json");
            writer.open();
            writer.write(listOfAccounts);
            writer.close();

            JsonReader reader = new JsonReader("./data/testGeneralListOfAccounts.json");
            listOfAccounts = reader.read();
            ArrayList<Account> accounts = listOfAccounts.getList();
            assertEquals(2, accounts.size());
            assertEquals("username1", accounts.get(0).getUsername());
            assertEquals("password1", accounts.get(0).getPassword());
            assertEquals("username2", accounts.get(1).getUsername());
            assertEquals("password2", accounts.get(1).getPassword());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testGeneralListOfAccounts() {
        try {
            JsonWriter writer = new JsonWriter("./data/testReaderGeneralListOfAccounts.json");
            writer.open();
            writer.write(listOfAccounts);
            writer.close();

            JsonReader reader = new JsonReader("./data/testReaderGeneralListOfAccounts.json");
            ListOfAccounts readListOfAccounts = reader.read();

            assertEquals(listOfAccounts.numOfAccount(), readListOfAccounts.numOfAccount());
            List<Account> accounts = readListOfAccounts.getList();
            assertEquals("user1", accounts.get(0).getUsername());
            assertEquals("password123", accounts.get(0).getPassword());
            assertEquals("Morning", accounts.get(0).getShift());

            assertEquals("user2", accounts.get(1).getUsername());
            assertEquals("password456", accounts.get(1).getPassword());
            assertEquals(null, accounts.get(1).getShift()); // Account without "shift" field

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


}
