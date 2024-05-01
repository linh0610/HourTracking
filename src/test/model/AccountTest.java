package model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void runBefore() {
        account = new Account("linh0610", "70370507");
    }

    @Test
    void testGetUsername(){
        Account testAccount = new Account("Linh", "12345678");
        assertEquals("Linh", testAccount.getUsername() );
    }

    @Test
    void testGetPassword() {
        Account testAccount = new Account("Linh", "12345678");
        assertEquals( "12345678", testAccount.getPassword());
    }

    @Test
    void testSetUsername(){
        Account testAccount = new Account("Linh", "12345678");
        testAccount.setUsername("Bach");
        assertEquals("Bach", testAccount.getUsername() );
    }

    @Test
    void testSetPassword() {
        Account testAccount = new Account("Linh", "12345678");
        testAccount.setPassword("87654321");
        assertEquals( "87654321", testAccount.getPassword());
    }

    @Test
    void testGetWagePerHour() {
        assertEquals(0, account.getWagePerHour());
    }

    @Test
    void testSetWagePerHour() {
        account.setWagePerHour(15.56);
        assertEquals(15.56, account.getWagePerHour());
    }

    @Test
    void testGetShift() {
        assertNull(account.getShift());
    }

    @Test
    void testSetShift() {
        account.setShift("8am to 12pm on 24/12/2023");
        assertEquals("8am to 12pm on 24/12/2023", account.getShift());
    }

    @Test
    void testGetHoursWorked() {
        assertEquals(0, account.getHoursWorked());
    }

    @Test
    void testSetHoursWorked() {
        account.setHoursWorked(8.35);
        assertEquals(8.35, account.getHoursWorked());
    }

    @Test
    public void testValidUsernameValidWithDotCom() {
        Account account = new Account("example@example.com", "Password1");
        assertTrue(account.validUsername(account.getUsername()));
    }

    @Test
    public void testValidUsernameValidWithDotCa() {
        Account account = new Account("example@example.ca", "Password1");
        assertTrue(account.validUsername(account.getUsername()));
    }

    @Test
    public void testValidUsernameInvalid() {
        Account account = new Account("example", "Password1");
        assertFalse(account.validUsername(account.getUsername()));
    }

    @Test
    public void testValidPasswordValid() {
        Account account = new Account("example@example.com", "Password1");
        assertTrue(account.validPassword(account.getPassword()));
    }

    @Test
    public void testValidPasswordInvalidNoUpperCase() {
        Account account = new Account("example@example.com", "password1");
        assertFalse(account.validPassword(account.getPassword()));
    }

    @Test
    public void testValidPasswordInvalidNoDigit() {
        Account account = new Account("example@example.com", "Password");
        assertFalse(account.validPassword(account.getPassword()));
    }

    @Test
    public void testContainsUpperCaseValid() {
        Account account = new Account("example@example.com", "Password1");
        assertTrue(account.containsUpperCase(account.getPassword()));
    }

    @Test
    public void testContainsUpperCaseInvalid() {
        Account account = new Account("example@example.com", "password");
        assertFalse(account.containsUpperCase(account.getPassword()));
    }

    @Test
    public void testContainsNumberValid() {
        Account account = new Account("example@example.com", "Password1");
        assertTrue(account.containsNumber(account.getPassword()));
    }

    @Test
    public void testContainsNumberInvalid() {
        Account account = new Account("example@example.com", "Password");
        assertFalse(account.containsNumber(account.getPassword()));
    }

    @Test
    void testToJson() {
        // Create an Account object
        Account account = new Account("testUser", "testPass");
        account.setWagePerHour(15.0);
        account.setShift("Morning");
        account.setHoursWorked(8.5);

        // Generate expected JSON object manually
        JSONObject expectedJson = new JSONObject();
        expectedJson.put("username", "testUser");
        expectedJson.put("password", "testPass");
        expectedJson.put("wagePerHour", 15.0);
        expectedJson.put("shift", "Morning");
        expectedJson.put("hoursWorked", 8.5);

        // Generate JSON object using toJson() method
        JSONObject actualJson = account.toJson();

        // Compare the generated JSON objects with the expected JSON objects
        assertEquals(expectedJson.toString(), actualJson.toString());
    }
}

