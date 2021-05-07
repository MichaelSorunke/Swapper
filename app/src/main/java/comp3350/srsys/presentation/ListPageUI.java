package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swapper.R;

import java.util.List;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessListings;
import comp3350.srsys.exceptions.crudexceptions.NullCategoryException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.userexceptions.NullUserException;
import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;

public class ListPageUI extends AppCompatActivity {
    private IAccessListings accessListings;
    private List <Listing> listings = null;
    private Intent thisIntent;
    private Logout logout;
    private ImageView image;
    private RecyclerViewAdapter.IRecyclerViewClickListener clickListener;
    private static String TAG = "ListPageUI"; // tag used for logging
    private static ParcelableListing listingParcel1;
    private static ParcelableListing listingParcel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();
        setContentView(R.layout.activity_list_page);

        accessListings = AccessService.getAccessListings();
        thisIntent = getIntent();

        getListings();

        setOnClickListener();
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.listingsListRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, listings, clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /* Click listener for whenever a recyclerView listing is clicked. It then starts the details page
     * and sends the details page the listing that was clicked */
    private void setOnClickListener() {
        clickListener = new RecyclerViewAdapter.IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(thisIntent.getExtras().containsKey("chooseownlisting")) { // take this path if we are picking out own listing
                    pickedListing(position);
                }
                else { // take this path if we are not picking our own listing
                    Intent intent1 = new Intent(getApplicationContext(), ListingDetailsUI.class);
                    // the position is of the item clicked so we use it to get the listing that was clicked from the List
                    listingParcel1 = new ParcelableListing(listings.get(position));
                    intent1.putExtra("listing", listingParcel1);
                    startActivity(intent1);
                }
            }
        };
    }

    private void getListings() {
        // 1. If the intent has the useremail and chooseownlisting extra we will send the user to pick
        //    their own listing for a swap
        //    else if the intent has a "category" extra then we get the category's listings,
        //    else if the intent has a "useremail" extra, we get the user's listings.
        Bundle extras = thisIntent.getExtras();
        if (extras.containsKey("email") && extras.containsKey("chooseownlisting")) {
            char[] charCurrUserEmail = extras.getCharArray("email");
            String stringCurrUserEmail = String.valueOf(charCurrUserEmail);
            setListingsToUserListings(stringCurrUserEmail);
        }
        else if (extras.containsKey("category")) {
            String categoryName = extras.getString("category");
            setListingsToCategoryListings(categoryName);
        }
        else if (extras.containsKey("email")) {
            String userEmail = extras.getString("email");
            setListingsToUserListings(userEmail);
        }
    }

    private void setListingsToCategoryListings(String categoryName) {
        Category category = new Category(categoryName);
        try {
            listings = accessListings.getCategoryListings(category);
        } catch (NullCategoryException e) {
            Log.e(TAG, "Cannot get the listings of a null Category");
        }
    }

    private void setListingsToUserListings(String userEmail) {
        User user = new User(userEmail, "", "", ""); // only the email field matters
        try {
            listings = accessListings.getUserListings(user);
        } catch (UserNotFoundException e) {
            Log.e(TAG, "Cannot find the user " + userEmail);
            // TODO: provide some feedback to the user so they know something went wrong
        } catch (NullUserException e) {
            Log.e(TAG, "Cannot get the listings of a null user");
            // TODO: provide some feedback to the user so they know something went wrong
        }
    }

    private void pickedListing(int position) {
        Intent intent2 = new Intent(this, ChooseTimePageUI.class);
        listingParcel2 = new ParcelableListing(listings.get(position));
        intent2.putExtra("choosenownlisting1", listingParcel1);
        intent2.putExtra("choosenownlisting2", listingParcel2);
        startActivity(intent2);
    }


}
