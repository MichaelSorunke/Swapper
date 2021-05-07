package comp3350.srsys.business;

import org.junit.Before;
import org.junit.Test;

import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.exceptions.crudexceptions.UserNotDeletedException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.crudexceptions.UserNotUpdated;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.database.UserPersistenceFDB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AccessUsersTest {

    AccessUsers testAccess;

    @Before
    public void setUp() {
        IUserPersistence testDB = new UserPersistenceFDB();
        testAccess = new AccessUsers(testDB);
    }

    @Test
    public void insertUserGeneralCase() throws UserNotFoundException {
        User testUser = new User("bobby@myumanitoba.ca", "Bob", "By", "ybboB");
        boolean exceptionOccurred = false;

        try {
            testAccess.insertUser(testUser);
        } catch (EmailInUseException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertEquals(testUser, testAccess.retrieveUser(testUser.getEmail()));
    }

    @Test
    public void insertUserEmailInUseException() {
        User u1 = new User("ander198@myumanitoba.ca", "Aaron", "Anderson", "12345");
        User u2 = new User("ander198@myumanitoba.ca", "Bob", "By", "ybboB");
        boolean exceptionOccurred = false;

        try {
            testAccess.insertUser(u1);
            testAccess.insertUser(u2);
        } catch (EmailInUseException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void insertMultipleUsers() throws UserNotFoundException {
        User u1 = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
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
    }

    @Test
    public void retrieveUserGeneralCase() throws EmailInUseException {
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        boolean exceptionOccurred = false;
        User retrievedUser = null;
        testAccess.insertUser(testUser);

        try {
            retrievedUser = testAccess.retrieveUser(testUser.getEmail());
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertEquals(testUser, retrievedUser);
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
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        User updateUser = new User("sanders@myumanitoba.ca", "Bundie", "Ganderson", "12345");
        boolean exceptionOccured = false;
        testAccess.insertUser(testUser);

        try {
            testAccess.updateUser(updateUser);
        } catch (UserNotUpdated e) {
            exceptionOccured = true;
        }

        User retrievedUser = testAccess.retrieveUser(updateUser.getEmail());
        assertFalse(exceptionOccured);
        assertEquals(updateUser.getEmail(), retrievedUser.getEmail());
        assertEquals(updateUser.getPassword(), retrievedUser.getPassword());
        assertEquals(updateUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(updateUser.getLastName(), retrievedUser.getLastName());
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
}