package comp3350.srsys.exceptions.crudexceptions;

import comp3350.srsys.exceptions.listingexceptions.AbstractInvalidListingException;

public class ListingDoesNotExistException extends AbstractInvalidListingException {
    public ListingDoesNotExistException(String message) {
        super(message);
    }
}
