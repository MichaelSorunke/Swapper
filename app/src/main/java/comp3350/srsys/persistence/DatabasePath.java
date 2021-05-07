package comp3350.srsys.persistence;

import android.util.Log;

public class DatabasePath implements IDatabasePath {

    private static IDatabasePath databasePathInstance = null;
    private static final String TAG = "DatabasePath";
    private static final String databaseName = "SC";
    private String databasePath;

    private DatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    public static IDatabasePath getInstance() {
        if (databasePathInstance == null) {
            databasePathInstance = new DatabasePath(databaseName);
        }

        return databasePathInstance;
    }

    public void sendDatabasePathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException e) {
            Log.e(TAG, "Could not create an instance of org.hsqldb.jdbcDriver");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Cannot access the method newInstance()");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Cannot find the class org.hsqldb.jdbcDriver");
        }
        databasePath = name;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
