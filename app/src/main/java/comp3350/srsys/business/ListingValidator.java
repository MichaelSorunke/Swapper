package comp3350.srsys.business;

import comp3350.srsys.objects.Listing;
import comp3350.srsys.persistence.IUserPersistence;

public class ListingValidator implements IValidator {

    IUserPersistence userPersistence;

    public ListingValidator(IUserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public boolean validate(Object o) {
        boolean isValid = false;
        boolean isListing = o instanceof Listing;

        if (isListing) {
            Listing listingToValidate = (Listing) o;
            isValid = userExistsInDatabase(listingToValidate.getUserEmail());
        }

        return isValid;
    }

    private boolean userExistsInDatabase(String userEmail) {
        boolean userExists = userPersistence.retrieveUser(userEmail) != null;
        return userExists;
    }
}
