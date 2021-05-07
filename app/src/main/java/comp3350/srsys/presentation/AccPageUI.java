package comp3350.srsys.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.srsys.objects.User;

import static comp3350.srsys.presentation.MainActivity.currUser;

public class AccPageUI extends AppCompatActivity {

    //private TextView password;
    Button editAcc;
    private User user;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private ILogout logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logout = new Logout(this);
        logout.ensureUserLoggedIn();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_acc_page);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navSelector);


        firstName = (TextView) findViewById(R.id.textView4);
        lastName = (TextView) findViewById(R.id.textView6);
        email = (TextView) findViewById(R.id.textView8);

        firstName.setText(currUser.getFirstName());
        lastName.setText(currUser.getLastName());
        email.setText(currUser.getEmail());
    }


    public void openEditAccPage(View view) {
        Intent intent = new Intent(this, EditAccPageUI.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setIcon(R.drawable.logout_icon)
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout.broadCastLogout();
                        logout.ensureUserLoggedIn();
                        //openLoginPage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create();
        builder.show();

    }

    public void openUserListingsPage(View view) {
        Intent intent = new Intent(getApplicationContext(), UserListingsPageUI.class);
        intent.putExtra("user", currUser.getEmail());
        startActivity(intent);
    }

    private void openLoginPage() {
        Intent intent = new Intent(this, LoginPageUI.class);//loginPage
        startActivity(intent);
        String toShow = "Logout Successful!";
        Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navSelector =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
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
