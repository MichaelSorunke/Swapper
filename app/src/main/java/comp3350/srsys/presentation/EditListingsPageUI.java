package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
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
import comp3350.srsys.objects.Listing;

import static comp3350.srsys.presentation.MainActivity.currUser;

public class EditListingsPageUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private Logout logout;
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
    Listing item;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_listings_page);

        Intent intent = getIntent();
        item = (Listing)intent.getSerializableExtra("listing");
        if (item != null) {
            category = item.getCategory();
            priority = item.getPriority();
            title = item.getTitle();
            description = item.getDescription();
            email = item.getUserEmail();
            id = item.getID();
        }
        else {
            category = "None";
            priority = 1;
            title = "";
            description = "";
            email = currUser.getEmail();
            id = accessListings.getListingID();
        }
        listing_title = findViewById(R.id.listing_title);
        listing_desc = findViewById(R.id.listing_desc);
        image = (ImageView) findViewById(R.id.gallery);

        listing_title.getEditText().setText(title);
        listing_desc.getEditText().setText(description);

        spinner = findViewById(R.id.spinner);
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

    }

    public void editPost(View view)
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
            item.setTitle(title);
            item.setDescription(description);
            item.setCategory(category);
            item.setPriority(priority);
            accessListings.updateListing(item);
            String toShow = "Your changes have been saved. " +
                    "You can view your listings in the My Account section.";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            goToListing();
        }
    }

    private void goToListing() {
        Intent intent = new Intent(this, UserListingDetailsUI.class);
        intent.putExtra("listing", item);
        category = item.getCategory();
        intent.putExtra("category", category);
        startActivity(intent);
    }
}