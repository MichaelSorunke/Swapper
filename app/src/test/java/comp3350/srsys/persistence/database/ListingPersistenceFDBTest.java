package comp3350.srsys.persistence.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ListingPersistenceFDBTest {

    ListingPersistenceFDB testFDB;

    @Before
    public void setUp() {
        testFDB = new ListingPersistenceFDB();
    }

    @Test
    public void retrieveListing() {
        Listing testListing1 = new Listing(0, "Title", 0, "", "");
        Listing testListing2 = new Listing(1, "Title", 0, "", "");

        testFDB.insertListing(testListing1);
        testFDB.insertListing(testListing2);

        assert testListing1.equals(testFDB.retrieveListing(0));
        assert testListing2.equals(testFDB.retrieveListing(1));
    }

    @Test
    public void insertListing() {
        Listing testListing = new Listing(0, "Title", 0, "", "");

        testFDB.insertListing(testListing);

        assertEquals(testListing, testFDB.retrieveListing(0));
    }

    @Test
    public void insertNullListing() {
        boolean exceptionOccurred = false;

        try {
            testFDB.insertListing(null);
        } catch (NullPointerException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void retrieveUserListingsSizeIsFour() {
        ArrayList<Listing> expectedListings = (ArrayList<Listing>) this.generateVariousListings();
        User user = new User("123@a.com", "", "", "");

        this.insertListingsListIntoDB(expectedListings);

        assertEquals(expectedListings.size(), this.testFDB.retrieveUserListings(user).size());
    }

    @Test
    public void retrieveUserListingsListsContainsUserListings() {

        ArrayList<Listing> expectedListings = (ArrayList<Listing>) this.generateVariousListings();
        User user = new User("123@a.com", "", "", "");

        this.insertListingsListIntoDB(expectedListings);

        assertTrue(listsAreEqual(this.testFDB.retrieveUserListings(user), expectedListings));
    }

    @Test
    public void retrieveSingleCategoryListing() {
        Listing testListing = new Listing(-1, "Godzilla!", 0, "Cars", "122@a.com");
        testFDB.insertListing(testListing);
        String category = testListing.getCategory();

        List<Listing> retrievedListings = testFDB.retrieveCategoryListings(category);

        assertNotNull(retrievedListings);
        assertEquals(retrievedListings.size(), 1);
        assertEquals(retrievedListings.get(0), testListing);
    }

    @Test
    public void retrieveCategoryListingsInChronologicalOrder() {
        final String category = "Food";
        Listing olderListing = new Listing(0, "Chocolate Cake", 0, category, "bob@yahoo.com");
        Listing newerListing = new Listing(1, "Pop Tart", 0, category, "bob@yahoo.com");
        testFDB.insertListing(olderListing);
        testFDB.insertListing(newerListing);

        List<Listing> retrievedListings = testFDB.retrieveCategoryListings(category);

        assertEquals(newerListing, retrievedListings.get(0));
        assertEquals(olderListing, retrievedListings.get(1));
    }

    @Test
    public void doNotRetrieveSingleListingNotInCategory() {
        Listing testListing = new Listing(-1, "Godzilla!", 0, "Cars", "123@a.com");
        testFDB.insertListing(testListing);
        String categoryWithoutListings = "Food";

        List<Listing> retrievedListings = testFDB.retrieveCategoryListings(categoryWithoutListings);

        assertNotNull(retrievedListings);
        assertEquals(retrievedListings.size(), 0);
    }

    @Test
    public void retrieveCategoryListingsGivenNull() {

        List<Listing> listings = testFDB.retrieveCategoryListings(null);

        assertEquals(0, listings.size());
    }

    @Test
    public void updateListing() {
        Listing listingVersion1 = new Listing(0, "", 0, "", "");
        Listing listingVersion2 = new Listing(0, "MY DOG", 0, "", "");
        testFDB.insertListing(listingVersion1);

        testFDB.updateListing(listingVersion2);

        Listing updatedListing = testFDB.retrieveListing(0);
        assertEquals("MY DOG", updatedListing.getTitle());
    }

    @Test
    public void deleteListing() {
        Listing testListing = new Listing(0, "", 0, "", "");
        this.testFDB.insertListing(testListing);

        this.testFDB.deleteListing(testListing);

        assertNotNull(testListing);
        assertNull(this.testFDB.retrieveListing(testListing.getID()));
    }

    private List<Listing> generateVariousListings() {
        ArrayList<Listing> variousListings = new ArrayList<Listing>();

        Listing l1 = new Listing(0, "Godzilla", 0, "", "123@a.com");
        Listing l2 = new Listing(1, "Nike Sneakers", 0, "", "123@a.com");
        Listing l3 = new Listing(2, "Hockey Stick", 3, "", "123@a.com");
        Listing l4 = new Listing(3, "My Mom's Cookware", 2, "", "123@a.com");

        variousListings.add(l1);
        variousListings.add(l2);
        variousListings.add(l3);
        variousListings.add(l4);

        return variousListings;
    }

    private void insertListingsListIntoDB(List<Listing> listingList) {
        for (int i = 0; i < listingList.size(); i++) {
            testFDB.insertListing(listingList.get(i));
        }
    }

    private boolean listsAreEqual(List<Listing> l1, List<Listing> l2) {
        if (l1.size() != l2.size())
            return false;
        for (int i = 0; i < l1.size(); i++) {
            boolean listingsNotEqual = !l1.get(i).equals(l2.get(i));
            if (listingsNotEqual) {
                return false;
            }
        }
        return true;
    }

}