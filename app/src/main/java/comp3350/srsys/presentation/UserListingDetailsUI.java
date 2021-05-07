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

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessListings;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.business.IValidator;
import comp3350.srsys.business.ValidatorService;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;

import static comp3350.srsys.presentation.MainActivity.currUser;

public class UserListingDetailsUI extends AppCompatActivity {

    private ILogout logout;
    private IValidator emailValidator;
    private IAccessUsers accessUsers;
    private IAccessListings accessListings;
    private User user;
    Button edit;
    Button delete;
    String category;
    ImageView image;
    Listing item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();
        setContentView(R.layout.activity_user_listing_details);

        emailValidator = ValidatorService.getEmailValidator();
        accessUsers = AccessService.getAccessUsers();
        accessListings = AccessService.getAccessListings();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navSelector);

        Intent intent = getIntent();


        ParcelableListing parcelableListing = intent.getParcelableExtra("listing");
        item = parcelableListing.getListing();
        category = item.getCategory();
        try {
            user = accessUsers.retrieveUser(item.getUserEmail());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        TextView title = (TextView)this.findViewById(R.id.listingDetailTitle);
        title.setText(item.getTitle());

        TextView description = (TextView)this.findViewById(R.id.listingDetailDescription);
        description.setText( item.getDescription());

        TextView name = (TextView)this.findViewById(R.id.listingDetailUserName); // check this with dummy profiles
        name.setText(user.getFirstName() + " " + user.getLastName());

        TextView date = (TextView) findViewById(R.id.datePosted);
        date.setText(item.toString());

        image = (ImageView) findViewById(R.id.listingDetailGallery);
        ImageSetter setter = new ImageSetter(image,category);
        setter.setImage();


        edit = (Button)findViewById(R.id.listingDetailEditPostButton);
        delete = (Button)findViewById(R.id.listingDetailDeletePostButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditListingsPageUI.class);
                intent.putExtra("listing", item);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListing(item);
                Intent intent = new Intent(getApplicationContext(), UserListingsPageUI.class);
                intent.putExtra("user", currUser.getEmail());
                startActivity(intent);
            }
        });


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

    public void deleteListing(Listing listing)
    {
        accessListings.deleteListing(item);
        String toShow = "Your Listing has been Deleted.";
        Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
    }
}
