package comp3350.srsys.business;

import comp3350.srsys.persistence.DatabasePath;
import comp3350.srsys.persistence.IDatabasePath;

public class AccessDatabasePath implements IAccessDatabasePath {
    private IDatabasePath accessDatabasePath;

    public AccessDatabasePath() {
        accessDatabasePath = DatabasePath.getInstance();
    }

    @Override
    public void sendDatabasePathName(final String name) {
        accessDatabasePath.sendDatabasePathName(name);
    }

    @Override
    public String getDatabaseName() {
        return accessDatabasePath.getDatabaseName();

    }
}
