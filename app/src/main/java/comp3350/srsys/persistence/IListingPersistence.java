package comp3350.srsys.persistence;

import java.util.List;

import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;

public interface IListingPersistence {
    void insertListing(Listing listing);

    Listing retrieveListing(Integer listingID);

    // Ordered by date created - newest to oldest
    List<Listing> retrieveUserListings(User user);

    // ordered by date created - newest to oldest
    List<Listing> retrieveCategoryListings(String category);

    void updateListing(Listing listing);

    void deleteListing(Listing listing);

    Integer getLargestID();
}
