package comp3350.srsys.persistence;

public interface IDatabasePath {

    public void sendDatabasePathName(final String name);

    public String getDatabasePath();

    public String getDatabaseName();
}
