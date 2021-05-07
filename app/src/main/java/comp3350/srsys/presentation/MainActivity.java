package comp3350.srsys.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.AccessDatabasePath;
import comp3350.srsys.business.IAccessListings;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.business.IAccessDatabasePath;
import comp3350.srsys.exceptions.listingexceptions.ListingIDInUseException;
import comp3350.srsys.exceptions.listingexceptions.NullListingException;
import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;

public class MainActivity extends AppCompatActivity {
    private IAccessUsers accessUsers;
    private IAccessListings accessListings;
    public static User currUser;
    private Button letsGo;
    public IAccessDatabasePath accessDatabasePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        /* The database must be copied prior to initializing any access class otherwise the database
         * path will be incorrectly set for getting a database object form PersistenceService when
         * initializing an access class */
        accessDatabasePath = new AccessDatabasePath();
        copyDatabaseToDevice();

        accessUsers = AccessService.getAccessUsers();
        accessListings = AccessService.getAccessListings();
        //populateDatabase();
        letsGo = (Button) findViewById(R.id.letsGoButton);
        letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
    }

    private void openLoginPage() {
        Intent intent = new Intent(this, LoginPageUI.class);
        startActivity(intent);
    }

    private void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }

    private void copyDatabaseToDevice() {
        final String DATABASE_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DATABASE_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DATABASE_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DATABASE_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            accessDatabasePath.sendDatabasePathName(dataDirectory.toString() + "/" + accessDatabasePath.getDatabaseName());

        } catch (final IOException ioe) {
            System.out.println("Unable to access application data: " + ioe.getMessage()); // should show error message
        }
    }

    private void populateDatabase()
    {
        try {
           User user1 = accessUsers.insertUser(new User("superbad@gmail.com", "Mick", "Lovin", "bootypoppin24!"));
            User user2 = accessUsers.insertUser(new User("sarah@myumanitoba.ca", "Jessica", "Parker", "Horse24!"));
            User user3 = accessUsers.insertUser(new User("RockandRoll@gmail.com", "Ronald", "McDonald", "wantFriesWithThat123!@"));
            User user4 = accessUsers.insertUser(new User("youwantmyname@hotmail.com", "Hugh", "Mungus", "WhatAbook24!"));
            User user5 = accessUsers.insertUser(new User("dreamjournal@gmail.com", "George", "Lopez", "electricidadR24!"));
            User user6 = accessUsers.insertUser(new User("oxlong24@myumanitoba.ca", "Mike", "Oxlong", "dontthinkaboutit0k24!"));
            //User user = accessUsers.insertUser(new User("adaa@yahoo.com", "Michael", "Sorunke", "Michael24!"));
        } catch (EmailInUseException e) {
            e.printStackTrace();
        }

        try {
            accessListings.insertListing(new Listing(accessListings.getListingID(), "Used Dream Journal", 1, "Books", "dreamjournal@gmail.com"));
            accessListings.insertListing(new Listing(accessListings.getListingID(), "Lost TV Remote", 1, "Electronics", "sarah@myumanitoba.ca"));
            accessListings.insertListing(new Listing(accessListings.getListingID(), "Well Used Toilet", 1, "Furniture", "oxlong24@myumanitoba.ca"));
            accessListings.insertListing(new Listing(accessListings.getListingID(), "Well Worn Socks", 1, "Clothes", "superbad@gmail.com"));
            accessListings.insertListing(new Listing(accessListings.getListingID(), "Hammer", 1, "Anything", "superbad@gmail.com"));
            accessListings.insertListing(new Listing(accessListings.getListingID(), "Fortnite", 1, "Games", "superbad@gmail.com"));
        } catch (NullListingException e) {
            e.printStackTrace();
        } catch (ListingIDInUseException e) {
            e.printStackTrace();
        }
    }
}