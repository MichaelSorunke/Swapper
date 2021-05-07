package comp3350.srsys.business;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import comp3350.srsys.exceptions.listingexceptions.AbstractInvalidListingException;
import comp3350.srsys.exceptions.listingexceptions.ListingIDInUseException;
import comp3350.srsys.exceptions.userexceptions.AbstractInvalidUserException;
import comp3350.srsys.exceptions.listingexceptions.NullIDException;
import comp3350.srsys.exceptions.crudexceptions.NullCategoryException;
import comp3350.srsys.exceptions.crudexceptions.ListingDoesNotExistException;
import comp3350.srsys.exceptions.listingexceptions.NullListingException;
import comp3350.srsys.exceptions.userexceptions.NullUserException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.DatabasePath;
import comp3350.srsys.persistence.IDatabasePath;
import comp3350.srsys.persistence.IListingPersistence;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.hsqldb.ListingPersistenceHSQLDB;
import comp3350.srsys.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.srsys.utils.TestingDatabase;

import static org.junit.Assert.*;

public class AccessListingsIntegrationTest {

    private static AccessListings accessListings;
    private static IListingPersistence listingPersistence;
    private static IUserPersistence userPersistence;
    private static Listing defaultListing;
    private static IDatabasePath databasePath;

    @BeforeClass
    public static void setUpClass() throws IOException {
        TestingDatabase.setupDatabaseFile();
        databasePath = DatabasePath.getInstance();
        listingPersistence = new ListingPersistenceHSQLDB(databasePath.getDatabasePath());
        userPersistence = new UserPersistenceHSQLDB(databasePath.getDatabasePath());

        accessListings = new AccessListings(listingPersistence, userPersistence);
        defaultListing = new Listing(0, "Gooberberry Sunrise", 0, "Food", "bob@yahoo.com");
    }

    @Before
    public void setUp() {
        listingPersistence.insertListing(defaultListing);
    }

    @After
    public void tearDown() {
        listingPersistence.deleteListing(defaultListing);
    }

    @Test
    public void insertListingGeneralCase() throws AbstractInvalidListingException, NullCategoryException {
        Listing testListing = new Listing(1, "Sunrise Gooberberry", 1, "Hunger Items", "bobby@yahoo.com");

        accessListings.insertListing(testListing);

        assertEquals(testListing, accessListings.getListing(testListing.getID()));
        int numberOfHungerItemsListings = accessListings.getCategoryListings(new Category("Hunger Items")).size();
        assertEquals(1, numberOfHungerItemsListings); // should only be one listing

        listingPersistence.deleteListing(testListing);
    }

    @Test
    public void insertListingGivenNullException() throws ListingIDInUseException {
        boolean exceptionOccurred = false;

        try {
            accessListings.insertListing(null);
        } catch (NullListingException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    // Weird error case that came up when testing
    @Test
    public void insertListingWithSameTitleAndDescription() throws NullListingException, ListingIDInUseException {
        Listing testListing = new Listing(1, "Sunrise Gooberberry", 1, "Hunger Items", "bobby@yahoo.com", "Sunrise Gooberberry");

        accessListings.insertListing(testListing);

        assertEquals(testListing, listingPersistence.retrieveListing(testListing.getID()));
        listingPersistence.deleteListing(testListing);
    }

    @Test
    public void insertListingWithExistingIDException() throws NullListingException {
        boolean exceptionOccurred = false;

        try {
            accessListings.insertListing(defaultListing); // defaultListing should already be in the database
        } catch (ListingIDInUseException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void getListingGeneralCase() throws AbstractInvalidListingException {

        Listing retrievedListing = accessListings.getListing(defaultListing.getID());

        assertEquals(defaultListing, retrievedListing);
    }

    @Test
    public void getListingGivenNullException() throws AbstractInvalidListingException {
        boolean exceptionOccured = false;

        try {
            accessListings.getListing(null);
        } catch (NullIDException e) {
            exceptionOccured = true;
        }

        assertTrue(exceptionOccured);
    }

    @Test
    public void getListingGivenNonExistentIDException() throws AbstractInvalidListingException {
        boolean exceptionOccurred = false;

        try {
            accessListings.getListing(1); // only 0 should exist
        } catch (ListingDoesNotExistException e) {
            exceptionOccurred = true;
        }
        assertTrue(exceptionOccurred);
    }

    @Test
    public void getUserListingsGeneralCase() throws AbstractInvalidUserException, UserNotFoundException {
        User testUser = new User(defaultListing.getUserEmail(), "Bob", "Bobby", "12345");
        userPersistence.insertUser(testUser);

        List<Listing> userListings = accessListings.getUserListings(testUser);

        assertEquals(1, userListings.size());
        assertEquals(defaultListing, userListings.get(0)); // first and only element should be the default listing
        userPersistence.deleteUser(testUser);
    }

    @Test
    public void getUserListingsGivenNullException() throws UserNotFoundException {
        boolean exceptionOccurred = false;

        try {
            accessListings.getUserListings(null);
        } catch (NullUserException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void getUserListingGivenNonexistentUserException() throws AbstractInvalidUserException {
        boolean exceptionOccurred = false;
        User testUser = new User("honky@hotmail.com", "Hon", "Key", "54321");

        try {
            List<Listing> retrievedListings = accessListings.getUserListings(testUser);
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void getCategoryListingsGeneralCase() throws NullCategoryException {
        Category testCategory = new Category(defaultListing.getCategory());

        List<Listing> listings = accessListings.getCategoryListings(testCategory);

        assertEquals(defaultListing, listings.get(0)); // first and only element should be the default element
        assertEquals(1, listings.size());
    }

    @Test
    public void getCategoryListingsExpectingChronologicalOrder() throws NullCategoryException {
        Listing newerListing = new Listing(1, "Pop Tart", 0, defaultListing.getCategory(), "bob@yahoo.com");
        listingPersistence.insertListing(newerListing);

        List<Listing> listings = accessListings.getCategoryListings(new Category(defaultListing.getCategory()));

        assertEquals(newerListing, listings.get(0));
        assertEquals(defaultListing, listings.get(1));

        listingPersistence.deleteListing(newerListing);
    }

    @Test
    public void getCategoryListingsGivenNullException() {
        boolean exceptionOccurred = false;

        try {
            List<Listing> listings = accessListings.getCategoryListings(null);
        } catch (NullCategoryException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void updateListingGeneralCase() throws AbstractInvalidListingException {
        // Same as the default Listing except a different title
        Listing testListing = new Listing(defaultListing.getID(),
                "Different Title", defaultListing.getPriority(),
                defaultListing.getCategory(), defaultListing.getUserEmail());

        accessListings.updateListing(testListing);

        Listing updatedListing = accessListings.getListing(testListing.getID());
        assertEquals(testListing, updatedListing);
        assertEquals(testListing.getTitle(), updatedListing.getTitle());
    }

    @Test
    public void deleteListing() {

        accessListings.deleteListing(defaultListing);

        assertNull(listingPersistence.retrieveListing(0));
    }

    @Test
    public void getLatestListingIDWhenNoListingsInDB() {
        listingPersistence.deleteListing(defaultListing);

        int latestID = accessListings.getListingID();

        assertEquals(0, latestID);
    }

    @Test
    public void getLatestListingIDWhenOneListingExists() {

        int latestID = accessListings.getListingID();

        assertEquals(1, latestID);
    }
}