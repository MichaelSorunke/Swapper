package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessListings;
import comp3350.srsys.exceptions.listingexceptions.ListingIDInUseException;
import comp3350.srsys.exceptions.listingexceptions.NullListingException;
import comp3350.srsys.objects.Listing;

import static comp3350.srsys.presentation.MainActivity.currUser;

public class MakeListingsPageUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private ILogout logout;
    Spinner spinner;
    Listing listing;
    String category;
    String title;
    String description;
    private TextInputLayout listing_title;
    private TextInputLayout listing_desc;
    String email;
    int priority;
    int id;
    ImageView image;
    private IAccessListings accessListings = AccessService.getAccessListings();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_make_listings_page);

        Intent intent = getIntent();
        Listing item = (Listing)intent.getParcelableExtra("listing");
        if (item != null) {
            category = item.getCategory();
            priority = item.getPriority();
            title = item.getTitle();
            description = item.getDescription();
            email = item.getUserEmail();
            id = item.getID();
        }
        else {
            category = "";
            priority = 1;
            title = "";
            description = "";
            email = currUser.getEmail();
            id = accessListings.getListingID();
        }
        listing_title = findViewById(R.id.listing_title);
        listing_desc = findViewById(R.id.listing_desc);
        image = (ImageView) findViewById(R.id.gallery);

        spinner = findViewById(R.id.categorySpinner);
//        spinner.setSelection(0); // select the first item in the categories array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text)
        {
            case "":
                category = "None";
                break;
            case "Clothes":
            case "Furniture":
            case "Books":
            case "Games":
            case "Electronics":
            case "Anything":
                category = text;
                break;

        }
        ImageSetter setter = new ImageSetter(image,category);
        setter.setImage();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing if nothing in the spinner is selected
    }

    public void makePost(View view)
    {
        title = listing_title.getEditText().getText().toString().trim();
        description = listing_desc.getEditText().getText().toString().trim();
        if(category.equals("")||category.equals("None"))
        {
            String toShow = "You must select a category!";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        }
        else if(title.equals(""))
        {
            listing_title.setError("Title cannot be empty!");
        }
        else if(description.equals(""))
        {
            listing_desc.setError("Description cannot be empty!");
        }
        else
        {
            listing = new Listing(id, title, priority, category, email);
            listing.setDescription(description);
            try {
                accessListings.insertListing(this.listing);
                goToHomePage();
                String toShow = "Your Listing has been Posted. " +
                        "You can view your listings in the My Account section.";
                Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            } catch (NullListingException e) {
                Log.e("makeListingsPageUI", "Cannot insert a null listing into the database");
                Toast.makeText(this, "Sorry, something went wrong on our end, try again.", Toast.LENGTH_SHORT).show();
            } catch (ListingIDInUseException e) {
                Log.e("makeListingsPageUI", "Cannot insert a listing with an ID that already exists in the database");
                Toast.makeText(this, "Sorry, something went wrong on our end, try again.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void goToHomePage() {
        Intent intent = new Intent(this, HomePageUI.class);
        startActivity(intent);
    }

}