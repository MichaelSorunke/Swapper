package comp3350.srsys.business;

import java.util.List;

import comp3350.srsys.exceptions.listingexceptions.AbstractInvalidListingException;
import comp3350.srsys.exceptions.listingexceptions.ListingIDInUseException;
import comp3350.srsys.exceptions.listingexceptions.NullIDException;
import comp3350.srsys.exceptions.listingexceptions.NullListingException;
import comp3350.srsys.exceptions.crudexceptions.NullCategoryException;
import comp3350.srsys.exceptions.crudexceptions.ListingDoesNotExistException;
import comp3350.srsys.exceptions.userexceptions.NullUserException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IListingPersistence;
import comp3350.srsys.persistence.IUserPersistence;


public class AccessListings implements IAccessListings {
    private final IListingPersistence listingPersistence;
    private IUserPersistence userPersistence;


    public AccessListings(IListingPersistence listingPersistence, IUserPersistence userPersistence) {
        this.listingPersistence = listingPersistence;
        this.userPersistence = userPersistence;
    }

    @Override
    public void insertListing(Listing listing) throws NullListingException, ListingIDInUseException {
        // Checking null prior to already existing is required otherwise a NullPointerException will occur by calling getID()
        if (listing == null)
            throw new NullListingException("Cannot insert a null listing.");
        boolean listingIDInUse = listingPersistence.retrieveListing(listing.getID()) != null;
        if (listingIDInUse)
            throw new ListingIDInUseException("Cannot insert a listing with the same ID as a listing already in the database");

        listingPersistence.insertListing(listing);
    }

    @Override
    public Listing getListing(Integer listingID) throws AbstractInvalidListingException {
        if (listingID == null)
            throw new NullIDException("Cannot retrieve a listing given a null listing ID");

        Listing retrievedListing = listingPersistence.retrieveListing(listingID);

        if (retrievedListing == null) {
            throw new ListingDoesNotExistException("Cannot find a listing given the ID of " + listingID);
        }
        return retrievedListing;
    }

    @Override
    public List<Listing> getUserListings(User user) throws NullUserException, UserNotFoundException {
        if (user == null) {
            throw new NullUserException("Cannot get the listings of a null user");
        } else if (userPersistence.retrieveUser(user.getEmail()) == null) {
            throw new UserNotFoundException("Cannot get the listings of " + user.toString() + " as they do not exist");
        }
        return listingPersistence.retrieveUserListings(user);
    }

    /* Gets listings from the database that have the category of the Category passed to it.
     * The list is in newest to oldest order */
    @Override
    public List<Listing> getCategoryListings(Category category) throws NullCategoryException {
        if (category == null)
            throw new NullCategoryException("Cannot retrieve listings given a null category");
        return listingPersistence.retrieveCategoryListings(category.getName());
    }

    @Override
    public void updateListing(Listing listing) {
        listingPersistence.updateListing(listing);
    }

    @Override
    public boolean deleteListing(Listing listing) {
        boolean listingWasDeleted = false;
        listingPersistence.deleteListing(listing);
        if (listingPersistence.retrieveListing(listing.getID()) == null)
            listingWasDeleted = true;
        return listingWasDeleted;
    }

    /* returns the next ID to use when creating a listing */
    public Integer getListingID() {
        return listingPersistence.getLargestID() + 1;
    }
}
