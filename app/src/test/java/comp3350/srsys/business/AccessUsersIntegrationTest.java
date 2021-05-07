package comp3350.srsys.business;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.exceptions.crudexceptions.UserNotDeletedException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.crudexceptions.UserNotUpdated;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.DatabasePath;
import comp3350.srsys.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.srsys.persistence.IDatabasePath;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.utils.TestingDatabase;

import static org.junit.Assert.*;

public class AccessUsersIntegrationTest {

    private static IAccessUsers testAccess;
    private static IUserPersistence userPersistence;
    private static User defaultUser;
    private static IDatabasePath databasePath;

    @BeforeClass
    public static void setUpClass() throws IOException {
        TestingDatabase.setupDatabaseFile();
        databasePath = DatabasePath.getInstance();
        userPersistence = new UserPersistenceHSQLDB(databasePath.getDatabasePath());
        testAccess = new AccessUsers(userPersistence);
        defaultUser = new User("bobby@myumanitoba.ca", "Bob", "By", "ybboB");
    }


    @Before
    public void setUp() throws Exception {
        userPersistence.insertUser(defaultUser);
    }

    @After
    public void tearDown() throws Exception {
        userPersistence.deleteUser(defaultUser);
    }

    @Test
    public void insertUserGeneralCase() throws UserNotFoundException {
        User testUser = new User("bobert@myumanitoba.ca", "Bob", "Ert", "ybboB");
        boolean exceptionOccurred = false;

        try {
            testAccess.insertUser(testUser);
        } catch (EmailInUseException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertEquals(testUser, testAccess.retrieveUser(testUser.getEmail()));

        // Clean up the database so this test doesn't affect others
        userPersistence.deleteUser(testUser);
    }

    @Test
    public void insertUserEmailInUseException() {
        User sameUser = new User("bobby@myumanitoba.ca", "Bob", "By", "ybboB");
        boolean exceptionOccurred = false;

        try {
            testAccess.insertUser(sameUser); // should throw an exception and not insert the user
        } catch (EmailInUseException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void insertMultipleUsers() throws UserNotFoundException {
        User u1 = new User("bobert@myumanitoba.ca", "Bob", "By", "ybboB");
        User u2 = new User("bob@yahoo.com", "Bob", "Bobby", "bob123");
        boolean exceptionOccurred = false;

        try {
            testAccess.insertUser(u1);
            testAccess.insertUser(u2);
        } catch (EmailInUseException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertEquals(u1, testAccess.retrieveUser(u1.getEmail()));
        assertEquals(u2, testAccess.retrieveUser(u2.getEmail()));

        deletedInsertedUser(u1);
        deletedInsertedUser(u2);
    }

    @Test
    public void retrieveUserGeneralCase() throws EmailInUseException {
        boolean exceptionOccurred = false;
        User retrievedUser = null;

        try {
            retrievedUser = testAccess.retrieveUser(defaultUser.getEmail());
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertEquals(defaultUser, retrievedUser);
    }

    @Test
    public void retrieveUserNonExistentUser() {
        boolean exceptionOccurred = false;
        User retrievedUser = null;

        try {
            retrievedUser = testAccess.retrieveUser("bobby@hotmail.ca");
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
        assertNull(retrievedUser);
    }

    @Test
    public void retrieveUserGivenNull() {
        boolean exceptionOccurred = false;
        User retrievedUser = null;

        try {
            retrievedUser = testAccess.retrieveUser(null);
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertNull(retrievedUser);
        assertTrue(exceptionOccurred);
    }

    @Test
    public void updateUserGeneralCase() throws EmailInUseException, UserNotFoundException {
        User updatedUser = new User("bobby@myumanitoba.ca", "Bobby", "Jacobs", "12345");
        boolean exceptionOccured = false;

        try {
            testAccess.updateUser(updatedUser);
        } catch (UserNotUpdated e) {
            exceptionOccured = true;
        }

        User retrievedUser = testAccess.retrieveUser(updatedUser.getEmail());
        assertFalse(exceptionOccured);
        assertEquals(updatedUser.getEmail(), retrievedUser.getEmail());
        assertEquals(updatedUser.getPassword(), retrievedUser.getPassword());
        assertEquals(updatedUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(updatedUser.getLastName(), retrievedUser.getLastName());
    }

    @Test
    public void updateUserDoesNotExist() {
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        boolean exceptionOccurred = false;

        try {
            testAccess.updateUser(testUser);
        } catch (UserNotUpdated e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void updateUserGivenNull() {
        boolean exceptionOccurred = false;

        try {
            testAccess.updateUser(null);
        } catch (UserNotUpdated userNotUpdated) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void deleteUserGeneralCase() throws EmailInUseException {
        // Build
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        boolean exceptionOccurred = false;
        testAccess.insertUser(testUser);

        // Operate
        try {
            testAccess.deleteUser(testUser);
        } catch (UserNotDeletedException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
    }

    @Test
    public void deleteUserGivenNull() {
        boolean exceptionOccurred = false;

        try {
            testAccess.deleteUser(null);
        } catch (UserNotDeletedException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void deleteUserThatDoesNotExist() {
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        boolean exceptionOccurred = false;

        try {
            testAccess.deleteUser(testUser);
        } catch (UserNotDeletedException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    private void deletedInsertedUser(User user) {
        userPersistence.deleteUser(user);
    }
}