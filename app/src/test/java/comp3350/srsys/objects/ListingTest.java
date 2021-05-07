package comp3350.srsys.objects;

import org.junit.Test;

public class ListingTest {

    @Test
    public void testEqualsSameObject() {
        Listing testListing = new Listing(0, "", 0, "", "");

        assert (testListing.equals(testListing));
    }

    @Test
    public void testEqualsNotEqual() {
        Listing l1 = new Listing(0, "Couch", 0, "Furniture", "");
        Listing l2 = new Listing(1, "Couch", 0, "Automotives", "");
    }
}