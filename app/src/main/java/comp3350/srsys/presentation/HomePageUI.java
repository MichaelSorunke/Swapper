package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.swapper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.srsys.objects.User;

public class HomePageUI extends AppCompatActivity  {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    private Button makeListing;
    private Button startSwapping;
    private User user;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout email;
    private TextInputLayout password;
    private ILogout logout;
    CardView itemSelected1;
    CardView itemSelected2;
    CardView itemSelected3;
    CardView itemSelected4;
    CardView itemSelected5;
    CardView itemSelected6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navSelector);

        itemSelected1 = (CardView) findViewById(R.id.Furniture);
        itemSelected2 = (CardView) findViewById(R.id.Books);
        itemSelected3 = (CardView) findViewById(R.id.Electronics);
        itemSelected4 = (CardView) findViewById(R.id.Games);
        itemSelected5 = (CardView) findViewById(R.id.Clothes);
        itemSelected6 = (CardView) findViewById(R.id.Anything);

        itemSelected1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPageUI.class);
                intent.putExtra("category", "Furniture");
                startActivity(intent);
            }
        });
        itemSelected2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPageUI.class);
                intent.putExtra("category", "Books");
                startActivity(intent);
            }
        });
        itemSelected3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPageUI.class);
                intent.putExtra("category", "Electronics");
                startActivity(intent);
            }
        });
        itemSelected4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPageUI.class);
                intent.putExtra("category", "Games");
                startActivity(intent);
            }
        });
        itemSelected5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPageUI.class);
                intent.putExtra("category", "Clothes");
                startActivity(intent);
            }
        });
        itemSelected6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPageUI.class);
                intent.putExtra("category", "Anything");
                startActivity(intent);
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navSelector =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.accIcon:
                            startActivity(new Intent(getApplicationContext(), AccPageUI.class));
                            break;

                        case R.id.add:
                            startActivity(new Intent(getApplicationContext(), MakeListingsPageUI.class));
                            break;

                        case R.id.home:
                            startActivity(new Intent(getApplicationContext(), HomePageUI.class));
                            break;

                    }
                    return true;
                }
            };
}





