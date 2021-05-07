package comp3350.srsys.business;

import org.junit.Before;
import org.junit.Test;

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
import comp3350.srsys.persistence.IListingPersistence;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.database.ListingPersistenceFDB;
import comp3350.srsys.persistence.database.UserPersistenceFDB;

import static org.junit.Assert.*;

public class AccessListingsTest {

    private AccessListings accessListings;
    private IListingPersistence listingPersistence;
    private IUserPersistence userPersistence;
    private Listing defaultListing;

    @Before
    public void setUp() {
        defaultListing = new Listing(0, "Gooberberry Sunrise", 0, "Food", "bob@yahoo.com");
        listingPersistence = new ListingPersistenceFDB();
        userPersistence = new UserPersistenceFDB();

        listingPersistence.insertListing(defaultListing);
        userPersistence.insertUser(new User(defaultListing.getUserEmail(), "Goob", "Berry", "12345"));

        accessListings = new AccessListings(listingPersistence, userPersistence);
    }

    @Test
    public void insertListingGeneralCase() throws AbstractInvalidListingException, NullCategoryException {
        Listing testListing = new Listing(1, "Sunrise Gooberberry", 1, "Hunger Items", "bobby@yahoo.com");

        accessListings.insertListing(testListing);

        assertEquals(testListing, accessListings.getListing(testListing.getID()));
        int numberOfHungerItemsListings = accessListings.getCategoryListings(new Category("Hunger Items")).size();
        assertEquals(1, numberOfHungerItemsListings); // should only be one listing
    }

    @Test
    public void insertListingGivenNull() throws ListingIDInUseException {
        boolean exceptionOccurred = false;

        try {
            accessListings.insertListing(null);
        } catch (NullListingException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void insertListingSameID() throws NullListingException {
        boolean exceptionOcurred = false;

        try  {
            accessListings.insertListing(defaultListing);
        } catch (ListingIDInUseException e) {
            exceptionOcurred = true;
        }

        assertTrue(exceptionOcurred);
    }

    @Test
    public void getListingGeneralCase() throws AbstractInvalidListingException {

        Listing retrievedListing = accessListings.getListing(defaultListing.getID());

        assertEquals(defaultListing, retrievedListing);
    }

    @Test
    public void getListingGivenNull() throws AbstractInvalidListingException {
        boolean exceptionOccured = false;

        try {
            accessListings.getListing(null);
        } catch (NullIDException e) {
            exceptionOccured = true;
        }

        assertTrue(exceptionOccured);
    }

    @Test
    public void getListingGivenNonExistentID() throws AbstractInvalidListingException {
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

        List<Listing> userListings = accessListings.getUserListings(testUser);

        assertEquals(1, userListings.size());
        assertEquals(defaultListing, userListings.get(0)); // first and only element should be the default listing
    }

    @Test
    public void getUserListingsGivenNull() throws AbstractInvalidUserException, UserNotFoundException {
        boolean exceptionOccurred = false;

        try {
            accessListings.getUserListings(null);
        } catch (NullUserException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void getUserListingGivenNonexistentUser() throws AbstractInvalidUserException {
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
    public void getCategoryListingsGivenNull() {
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
    public void deleteListing() throws AbstractInvalidListingException {

        accessListings.deleteListing(defaultListing);

        assertNull(listingPersistence.retrieveListing(0));
    }
}