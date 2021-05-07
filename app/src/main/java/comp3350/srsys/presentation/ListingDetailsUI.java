package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;


import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;

public class ListingDetailsUI extends AppCompatActivity {

    private ILogout logout;
    private Intent intent;
    private TextView titleView;
    private ImageView imageView;
    private TextView accountNameView;
    private TextView descriptionView;
    private Listing listing;
    private Button swap;
    private IAccessUsers accessUsers;
    private static ParcelableListing listingParcel1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure the user is logged in to access this page
        logout = new Logout(this);
        logout.ensureUserLoggedIn();

        accessUsers = AccessService.getAccessUsers();

        setContentView(R.layout.activity_listing_details);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navSelector);

        intent = getIntent();

        titleView = findViewById(R.id.listingDetailTitle);
        imageView = findViewById(R.id.listingDetailGallery);
        accountNameView = findViewById(R.id.listingDetailUserName);
        descriptionView = findViewById(R.id.listingDetailDescription);

        ParcelableListing parcelableListing = intent.getParcelableExtra("listing");
        listing = parcelableListing.getListing();

        char[] charCurrUserEmail = accessUsers.getCurrUserEmail();
        String stringCurrUserEmail = String.valueOf(charCurrUserEmail);


        populateFields();
        setUserNameClickListener();


        swap = (Button) findViewById(R.id.listingDetailRequestSwapButton); // search for it here
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((listing.getUserEmail()).equals(stringCurrUserEmail)) {
                    cantSwapWithSelf();
                }
                else {
                    goToPickOwnListing(); //send to the chooseTimePageUI
                }
            }
        });
    }

    private void populateFields() {
        titleView.setText(listing.getTitle());
        ImageSetter setter = new ImageSetter(imageView,listing.getCategory());
        setter.setImage();
        try {
            User user = accessUsers.retrieveUser(listing.getUserEmail());
            accountNameView.setText(user.getFirstName() + " " + user.getLastName());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        descriptionView.setText(listing.getDescription());
    }

    private void setUserNameClickListener() {
        accountNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userListingsIntent = new Intent(getApplicationContext(), ListPageUI.class);
                userListingsIntent.putExtra("useremail", listing.getUserEmail());
                startActivity(userListingsIntent);
            }
        });
    }

    private void goToPickOwnListing() {
        String toShow = "Choose listing you wish to swap";
        Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        Intent userListingsIntent = new Intent(getApplicationContext(), ListPageUI.class);
        listingParcel1 = new ParcelableListing(listing);
        userListingsIntent.putExtra("email", accessUsers.getCurrUserEmail());
        userListingsIntent.putExtra("chooseownlisting", -1);
        startActivity(userListingsIntent);
        //send to listpageUI somehow and somehow have it so when they click a listing there they get sent to a screen to confirm listings choice
    }

    private void cantSwapWithSelf() {
        String toShow = "Can't swap an item with yourself";
        Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navSelector =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.home:
                            startActivity(new Intent(getApplicationContext(), HomePageUI.class));
                            break;

                        case R.id.accIcon:
                            startActivity(new Intent(getApplicationContext(), AccPageUI.class));
                            break;

                        case R.id.add:
                            startActivity(new Intent(getApplicationContext(), MakeListingsPageUI.class));
                            break;
                    }
                    return true;
                }
            };
    }

