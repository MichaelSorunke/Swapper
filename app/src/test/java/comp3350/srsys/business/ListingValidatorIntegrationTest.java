package comp3350.srsys.business;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import comp3350.srsys.exceptions.crudexceptions.DeletingNonExistentCategoryException;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.DatabasePath;
import comp3350.srsys.persistence.IDatabasePath;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.srsys.utils.TestingDatabase;

import static org.junit.Assert.*;

public class ListingValidatorIntegrationTest {

    private static IUserPersistence userPersistence;
    private static User defaultUser;
    private static IDatabasePath databasePath;
    private IValidator listingValidator;

    @BeforeClass
    public static void setUpClass() throws IOException {
        TestingDatabase.setupDatabaseFile();
        databasePath = DatabasePath.getInstance();
        userPersistence = new UserPersistenceHSQLDB(databasePath.getDatabasePath());
        defaultUser = new User("bob@yahoo.com", "Bob", "Rob", "12345");
    }

    @Before
    public void setUp() {
        listingValidator = new ListingValidator(userPersistence);
        userPersistence.insertUser(defaultUser);
    }

    @After
    public void tearDown() throws DeletingNonExistentCategoryException {
        userPersistence.deleteUser(defaultUser);
    }

    @Test
    public void validateValidListing() {
        Listing listing = new Listing(0, "Hair Dryer", 0, "Home Appliances", "bob@yahoo.com");

        boolean isValid = listingValidator.validate(listing);

        assertTrue(isValid);
    }

    @Test
    public void validateListingGivenNullListing() {

        boolean isValid = listingValidator.validate(null);

        assertFalse(isValid);
    }

    @Test
    public void validateListingWithInvalidUser() {
        Listing listing = new Listing(0, "Hair Dryer", 0, "Animals", "jenkins@goodrop.com");

        boolean isValid = listingValidator.validate(listing);

        assertFalse(isValid);
    }
}