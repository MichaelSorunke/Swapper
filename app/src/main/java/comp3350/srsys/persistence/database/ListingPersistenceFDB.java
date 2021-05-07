package comp3350.srsys.persistence.database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IListingPersistence;

public class ListingPersistenceFDB implements IListingPersistence {

    static Integer id = 0;
    private static ListingPersistenceFDB listingPersistenceFDB = null;
    private TreeMap<Integer, Listing> listingDB;

    public ListingPersistenceFDB() {
        listingDB = new TreeMap<>();
    }

    @Override
    public void insertListing(Listing listing) {
        listingDB.put(listing.getID(), listing);
        id++;
    }


    @Override
    public Listing retrieveListing(Integer listingID) {
        return listingDB.get(listingID);
    }

    // Ordered by date created - newest to oldest
    @Override
    public List<Listing> retrieveUserListings(User user) {
        Map.Entry currPair = listingDB.firstEntry();
        ArrayList<Listing> listings = new ArrayList<Listing>();

        while (currPair != null) {
            Listing currListing = (Listing) currPair.getValue();
            boolean isUserListing = currListing.getUserEmail().equals(user.getEmail());
            if (isUserListing) {
                listings.add(currListing);
            }
            currPair = this.getNextPair(currPair);
        }
        return listings;
    }

    /* ordered by date created - newest to oldest */
    @Override
    public List<Listing> retrieveCategoryListings(String category) {
        Map.Entry currPair = listingDB.firstEntry();
        ArrayList<Listing> categoryListings = new ArrayList<Listing>();

        while (currPair != null) {
            Listing currListing = (Listing) currPair.getValue();
            boolean isCategoryListing = currListing.getCategory().equals(category);
            if (isCategoryListing) {
                categoryListings.add(currListing);
            }
            currPair = this.getNextPair(currPair);
        }
        sortListingsChronologically(categoryListings);
        return categoryListings;
    }

    @Override
    public void updateListing(Listing listing) {
        Listing oldListing = retrieveListing(listing.getID());
        deleteListing(oldListing);
        insertListing(listing);
    }

    @Override
    public void deleteListing(Listing listing) {
        try {
            listingDB.remove(listing.getID());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Integer getLargestID() {
        return id;
    }

    private Map.Entry getNextPair(Map.Entry pair) {
        return listingDB.higherEntry((Integer) pair.getKey());
    }

    /* Sorts listings from newest to oldest to replicate what occurs in the real
     * implementation */
    private void sortListingsChronologically(List<Listing> listings) {
        if (listings == null || listings.size() == 0) // null or no elements we don't sort
            return;

        int newestIndex = 0;
        Listing newestListing = listings.get(newestIndex);

        for (int i = 0; i < listings.size(); i++) {
            for (int j = i; j < listings.size(); j++) {
                Listing currentListing = listings.get(j);
                if (currentListing.getDatePosted().after(newestListing.getDatePosted())) {
                    newestListing = currentListing;
                    newestIndex = j;
                }
            }
            // swap the newest one with the ith element
            listings.set(newestIndex, listings.get(i));
            listings.set(i, newestListing);
        }
    }
}
