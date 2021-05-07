package comp3350.srsys.business;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.DatabasePath;
import comp3350.srsys.persistence.IDatabasePath;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.srsys.utils.TestingDatabase;

import static org.junit.Assert.*;

public class AuthenticateUserIntegrationTest {

    private static IAuthenticateUser authenticateUser;
    private static IUserPersistence userPersistence;
    private static User defaultUser;
    private static IDatabasePath databasePath;

    @BeforeClass
    public static void setUpClass() throws IOException {
        TestingDatabase.setupDatabaseFile();
        databasePath = DatabasePath.getInstance();
        userPersistence = new UserPersistenceHSQLDB(databasePath.getDatabasePath());
        authenticateUser = new AuthenticateUser(userPersistence);
        defaultUser = new User("bob@yahoo.com", "Bob", "Bobert", "12345");
    }

    @Before
    public void insertUser() {
        userPersistence.insertUser(defaultUser);
    }

    @After
    public void removeUser() {
        userPersistence.deleteUser(defaultUser);
    }

    @Test
    public void authenticateUserGeneralCase() {
        boolean exceptionOccurred = false;
        boolean passwordCorrect = false;

        try {
            passwordCorrect = authenticateUser.authenticate(defaultUser.getEmail(), defaultUser.getPassword());
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertTrue(passwordCorrect);
    }

    @Test
    public void authenticateUserIncorrectPassword() {
        boolean exceptionOccurred = false;
        boolean passwordCorrect = false;

        try {
            passwordCorrect = authenticateUser.authenticate(defaultUser.getEmail(), "Donkey");
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertFalse(passwordCorrect);
    }

    @Test
    public void authenticateUserUserNotFound() {
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        boolean exceptionOccurred = false;

        try {
            authenticateUser.authenticate(testUser.getEmail(), testUser.getPassword());
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void authenticateUserGivenNullEmail() {
        boolean userNotFound = false;

        try {
            authenticateUser.authenticate(null, defaultUser.getPassword());
        } catch (UserNotFoundException e) {
            userNotFound = true;
        }

        assertTrue(userNotFound); // cannot find a null user
    }

    @Test
    public void authenticateUserGivenNullPassword() throws UserNotFoundException {
        boolean isValidCredentials = false;

        isValidCredentials = authenticateUser.authenticate(defaultUser.getEmail(), null);

        assertFalse(isValidCredentials); // null password doesn't match the user's password
    }

}