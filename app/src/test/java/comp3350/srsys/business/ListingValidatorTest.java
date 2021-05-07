package comp3350.srsys.business;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import comp3350.srsys.exceptions.listingexceptions.NullListingException;
import comp3350.srsys.exceptions.crudexceptions.DeletingNonExistentCategoryException;
import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.database.UserPersistenceFDB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListingValidatorTest {

    private IValidator listingValidator;
    private IUserPersistence userPersistence;
    private static Category defaultCategory;
    private static User defaultUser;

    @BeforeClass
    public static void setUpClass() {
        defaultUser = new User("bob@yahoo.com", "Bob", "Rob", "12345");
    }

    @Before
    public void setUp() {
        userPersistence = new UserPersistenceFDB();
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
    public void validateListingWithInvalidUser() throws NullListingException {
        Listing listing = new Listing(0, "Hair Dryer", 0, "Animals", "jenkins@hotmail.ca");

        boolean isValid = listingValidator.validate(listing);

        assertFalse(isValid);
    }
}