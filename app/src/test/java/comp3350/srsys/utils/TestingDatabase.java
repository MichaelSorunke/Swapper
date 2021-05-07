package comp3350.srsys.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import comp3350.srsys.persistence.DatabasePath;
import comp3350.srsys.persistence.IDatabasePath;

public class TestingDatabase {
    private static final File DB_SRC = new File("src/main/assets/db/SC.script");
    private static IDatabasePath databasePath = DatabasePath.getInstance();

    public static void setupDatabaseFile() throws IOException {
        // create temporary script to create the test database
        File target = File.createTempFile("temp-db", ".script");
        // create an output stream so we can write to the file
        FileOutputStream targetStream = new FileOutputStream(target);
        // copy the contents of Sc.script to temp-db.script so it can be run
        Files.copy(DB_SRC.toPath(), targetStream);
        String pathName = target.getAbsolutePath().replace(".script", "");
        databasePath.sendDatabasePathName(pathName);
    }
}
