package comp3350.srsys.persistence.database;

import org.junit.Before;
import org.junit.Test;

import comp3350.srsys.objects.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserPersistenceFDBTest {

    UserPersistenceFDB testFDB;

    @Before
    public void setUp() {
        testFDB = new UserPersistenceFDB();
    }

    @Test
    public void insertTwoUsers() {
        User u1 = new User("ander198@myumanitoba.ca", "Aaron", "Anderson", "12345");
        User u2 = new User("bob@yahoo.ca", "Bob", "Yah", "oo");
        boolean exceptionOccurred = false;

        try {
            testFDB.insertUser(u1);
            testFDB.insertUser(u2);

        } catch (Exception e) {
            exceptionOccurred = true;
        }

        assertFalse(exceptionOccurred);
        assertNotNull(testFDB.retrieveUser(u2.getEmail()));
    }

    @Test
    public void retrieveUserGeneralCase() {
        User testUser = new User("ander198@myumanitoba.ca", "Aaron", "Anderson", "12345");
        testFDB.insertUser(testUser);

        User retrievedUser = testFDB.retrieveUser(testUser.getEmail());

        assertNotNull(retrievedUser);
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void deleteUser() {
        User testUser = new User("ander198@myumanitoba.ca", "Aaron", "Anderson", "12345");
        testFDB.insertUser(testUser);


        testFDB.deleteUser(testUser);

        assertNull(testFDB.retrieveUser(testUser.getEmail()));
    }
}