package comp3350.srsys.business;

import java.util.List;

import comp3350.srsys.exceptions.listingexceptions.AbstractInvalidListingException;
import comp3350.srsys.exceptions.listingexceptions.ListingIDInUseException;
import comp3350.srsys.exceptions.userexceptions.AbstractInvalidUserException;
import comp3350.srsys.exceptions.listingexceptions.NullListingException;
import comp3350.srsys.exceptions.crudexceptions.NullCategoryException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.userexceptions.NullUserException;
import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;

public interface IAccessListings {
    void insertListing(Listing listing) throws NullListingException, ListingIDInUseException;

    Listing getListing(Integer listingID) throws AbstractInvalidListingException;

    // TODO: refactor to use the user's email instead of the User object itself
    List<Listing> getUserListings(User user) throws NullUserException, UserNotFoundException;

    List<Listing> getCategoryListings(Category category) throws NullCategoryException;

    void updateListing(Listing listing);

    boolean deleteListing(Listing listing);

    Integer getListingID();
}
