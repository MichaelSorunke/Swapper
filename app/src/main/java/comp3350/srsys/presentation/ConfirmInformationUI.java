package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessListings;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.persistence.IListingPersistence;
import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;

public class ConfirmInformationUI extends AppCompatActivity {

    private ILogout logout;
    private Intent intent;
    private TextView itemTitle1;
    private TextView itemTitle2;
    private TextView timeText;
    private TextView dateText;
    private Listing listing1;
    private Listing listing2;
    private int hourOfDay;
    private int minute;
    private int year;
    private int month;
    private int date;
    private Button confirmInputButton;
    private IAccessListings accessListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure the user is logged in to access this page
        logout = new Logout(this);
        logout.ensureUserLoggedIn();

        accessListings = AccessService.getAccessListings();

        setContentView(R.layout.activity_check_info_page);

        intent = getIntent();

        itemTitle1 = findViewById(R.id.listingDetailTitle1);
        itemTitle2 = findViewById(R.id.listingDetailTitle2);
        timeText = findViewById(R.id.listingDetailTime);
        dateText = findViewById(R.id.listingDetailDate);

        ParcelableListing parcelableListing1 = intent.getParcelableExtra("choosenownlisting1");
        listing1 = parcelableListing1.getListing();
        ParcelableListing parcelableListing2 = intent.getParcelableExtra("choosenownlisting2");
        listing2 = parcelableListing2.getListing();
        hourOfDay = intent.getIntExtra("choosenhourofday", -1);
        minute = intent.getIntExtra("choosenminute", -1);
        year = intent.getIntExtra("choosenyear", -1);
        month = intent.getIntExtra("choosenmonth", -1);
        date = intent.getIntExtra("choosendate", -1);

        populateFields();

        confirmInputButton = (Button) findViewById(R.id.confirmInputButton); // search for it here
        confirmInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomepage(); //send the user back to the homepage
            }
        });
    }

    private void populateFields() {
        itemTitle1.setText(listing1.getTitle());
        itemTitle2.setText(listing2.getTitle());
        timeText.setText(hourOfDay + ":" + String.format("%02d", minute));
        dateText.setText("m." + month + " d." + date + " y." + year +"?");
    }

    private void goToHomepage() {
        Intent intent = new Intent(this, HomePageUI.class);
        accessListings.deleteListing(listing1);
        accessListings.deleteListing(listing2);
        String toShow = "Swap Successful";
        Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
