package comp3350.srsys.business;

import comp3350.srsys.persistence.PersistenceService;

public class AccessService {
    private static IAccessUsers accessUsers = null;
    private static IAccessListings accessListings = null;

    public static IAccessUsers getAccessUsers() {
        if (accessUsers == null) {
            accessUsers = new AccessUsers(PersistenceService.getUserPersistence());
        }
        return accessUsers;
    }

    public static IAccessListings getAccessListings() {
        if (accessListings == null) {
            accessListings = new AccessListings(PersistenceService.getListingPersistence(), PersistenceService.getUserPersistence());
        }
        return accessListings;
    }

}
