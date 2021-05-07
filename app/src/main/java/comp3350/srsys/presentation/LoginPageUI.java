package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.AuthenticateUser;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.business.IAuthenticateUser;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.User;

import static comp3350.srsys.presentation.MainActivity.currUser;


public class LoginPageUI extends AppCompatActivity {

    String emailInput;
    String passInput;
    User user;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button signIn;
    private IAccessUsers accessUsers;
    private IAuthenticateUser authenticateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.listing_title);
        emailInput = email.getEditText().getText().toString().trim();
        password = findViewById(R.id.password);
        passInput = password.getEditText().getText().toString().trim();

        accessUsers = AccessService.getAccessUsers();
        authenticateUser = AuthenticateUser.getInstance();

        signIn = (Button) findViewById(R.id.signInButton);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });
    }

    public void confirmInput() {
        emailInput = email.getEditText().getText().toString().trim();
        passInput = password.getEditText().getText().toString().trim();
        try {
            user = accessUsers.retrieveUser(emailInput);
            if (authenticateUser.authenticate(emailInput, passInput)) {
                currUser = user;
                accessUsers.setCurrUserEmail(emailInput); // TODO delete this if needed
                String toShow = "Sign in successful!";
                clearLoginFields();
                Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
                goToHomePage();
            } else {
                String toShow = "Incorrect Password!";
                Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            }
        } catch (UserNotFoundException e) {
            String toShow = "Incorrect Login information \n" +
                    "Please create an account";
            Toast.makeText(this, toShow, Toast.LENGTH_LONG).show();
        }
    }

    public void openResetPassPage(View view) {
        Intent intent = new Intent(this, ResetPassPageUI.class);
        startActivity(intent);
    }

    public void openCreateAccPage(View view) {
        Intent intent = new Intent(this, CreateAccPageUI.class);
        startActivity(intent);
    }

    private void goToHomePage() {
        Intent intent = new Intent(this, HomePageUI.class);
        // we send the user's email so we can retrieve them and their information in the account page
        startActivity(intent);
    }

    private void clearLoginFields() {
        email.getEditText().setText("");
        password.getEditText().setText("");
    }


}
