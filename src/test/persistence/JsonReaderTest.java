package persistence;

import model.Account;
import model.ListOfAccounts;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ListOfAccounts listOfAccounts = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testReaderEmptyListOfAccounts() {
        JsonReader reader = new JsonReader("./data/testEmptyListOfAccounts.json");
        try {
            ListOfAccounts listOfAccounts = reader.read();
            assertEquals(0, listOfAccounts.numOfAccount());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testReaderGeneralListOfAccounts() {
        JsonReader reader = new JsonReader("./data/testGeneralListOfAccounts.json");
        try {
            ListOfAccounts listOfAccounts = reader.read();
            ArrayList<Account> accounts = listOfAccounts.getList();
            assertEquals(2, accounts.size());
            assertEquals("username1", accounts.get(0).getUsername());
            assertEquals("password1", accounts.get(0).getPassword());
            assertEquals("username2", accounts.get(1).getUsername());
            assertEquals("password2", accounts.get(1).getPassword());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderWithShift() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralListOfAccounts.json");
        try {
            Account account = reader.read().getList().get(0);
            assertEquals("user1", account.getUsername());
            assertEquals("password123", account.getPassword());
            assertEquals("Morning", account.getShift());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testReaderWithoutShift() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralListOfAccounts.json");
        try {
            Account account = reader.read().getList().get(1);
            assertEquals("user2", account.getUsername());
            assertEquals("password456", account.getPassword());
            assertEquals(null, account.getShift());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail("Exception should not have been thrown");
        }
    }

}
