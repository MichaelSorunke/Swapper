package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.business.UserValidator;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.crudexceptions.UserNotUpdated;
import comp3350.srsys.objects.User;

import static comp3350.srsys.presentation.MainActivity.currUser;

public class ResetPassPageUI extends AppCompatActivity {
    User user;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button button;
    private String emailInput;
    private String passInput;
    private IAccessUsers accessUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_pass_page);

        accessUsers = AccessService.getAccessUsers();
        user = currUser;

        email = findViewById(R.id.resetPageEmail);
        emailInput = email.getEditText().getText().toString().trim();

        password = findViewById(R.id.resetPagePassword);
        passInput = password.getEditText().getText().toString().trim();
    }


    private boolean checkEmail() {
        emailInput = email.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Email cannot be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Invalid Email Address. Try Again!");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean checkPassword() {
        passInput = password.getEditText().getText().toString().trim();
        if (passInput.isEmpty()) {
            password.setError("Password cannot be empty!");
            return false;
        } else if (!UserValidator.validatePassword(passInput)) {
            password.setError("Password should be 6-15 characters, should include a capital letter, special character, and a number");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void confirmInput(View view) {
        if (!checkEmail() | !checkPassword()) {
            return;
        }

        try {// update the user
            User user = accessUsers.retrieveUser(emailInput);
            if ((user.getEmail()).equals(emailInput)) {
                User updatedUser = new User(emailInput, user.getFirstName(), user.getLastName(), passInput);
                    currUser = updatedUser;
                    accessUsers.updateUser(currUser);
                    String toShow = "Password Updated!";
                    Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginPageUI.class);
                    startActivity(intent);
            } else {
                String toShow = "This email is wrong";
                Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            }
        } catch (UserNotFoundException | UserNotUpdated e) {
            currUser.setPassword(passInput);
            String toShow = "Password Updated!!!!!";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginPageUI.class);
            startActivity(intent);
        }
    }


}
