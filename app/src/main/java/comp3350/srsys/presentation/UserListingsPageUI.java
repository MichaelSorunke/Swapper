package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swapper.R;

import java.util.List;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessListings;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.userexceptions.NullUserException;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;


public class UserListingsPageUI extends AppCompatActivity {
    private IAccessListings accessListings;
    private IAccessUsers accessUsers;
    private List <Listing> listings = null;
    User user;
    private Logout logout;
    private RecyclerViewAdapter.IRecyclerViewClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();
        setContentView(R.layout.activity_user_listing_page);

        accessListings = AccessService.getAccessListings();
        accessUsers = AccessService.getAccessUsers();
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("user");
        getUser(userEmail);
        try {
            listings = accessListings.getUserListings(user);
        } catch (NullUserException e) {
            Log.e("userListingsPageUI", "Cannot get the listings of a null user");
        } catch (UserNotFoundException e) {
            Log.e("userListingPageUI", "Cannot get the listings of a user that does not exist in the database");
        }

        setOnClickListener();
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.listingsListRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, listings, clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getUser(String email)
    {
        try {
            user = accessUsers.retrieveUser(email);
        } catch (UserNotFoundException e) {
            Intent intent = new Intent(this, HomePageUI.class);//loginPage
            startActivity(intent);
            String toShow = "User not found!";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        }
    }

    /* Click listener for whenever a recyclerView listing is clicked. It then starts the details page
     * and sends the details page the listing that was clicked */
    // TODO: Pull this out into its own class as both this and ListPageUI.java use this method
    private void setOnClickListener() {
        clickListener = new RecyclerViewAdapter.IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), UserListingDetailsUI.class);
                // the position is of the item clicked so we use it to get the listing that was clicked from the List
                ParcelableListing listingParcel = new ParcelableListing(listings.get(position));
                intent.putExtra("listing", listingParcel);
                startActivity(intent);
            }
        };
    }
}
