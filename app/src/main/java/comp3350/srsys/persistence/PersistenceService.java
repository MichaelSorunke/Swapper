package comp3350.srsys.persistence;

import comp3350.srsys.persistence.hsqldb.ListingPersistenceHSQLDB;
import comp3350.srsys.persistence.hsqldb.UserPersistenceHSQLDB;

public class PersistenceService {
    private static IUserPersistence userDB = null;
    private static IListingPersistence listingDB = null;
    private static IDatabasePath databasePath = DatabasePath.getInstance();

    public static IUserPersistence getUserPersistence() {
        if (userDB == null) {
            userDB = new UserPersistenceHSQLDB(databasePath.getDatabasePath());
        }
        return userDB;
    }

    public static IListingPersistence getListingPersistence() {
        if (listingDB == null) {
            listingDB = new ListingPersistenceHSQLDB(databasePath.getDatabasePath());
        }
        return listingDB;
    }
}
