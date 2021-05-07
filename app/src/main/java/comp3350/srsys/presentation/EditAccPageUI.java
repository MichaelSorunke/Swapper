package comp3350.srsys.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swapper.R;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.srsys.business.AccessService;
import comp3350.srsys.business.IAccessUsers;
import comp3350.srsys.business.IValidator;
import comp3350.srsys.business.ValidatorService;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.crudexceptions.UserNotUpdated;
import comp3350.srsys.objects.User;

import static comp3350.srsys.presentation.MainActivity.currUser;


public class EditAccPageUI extends AppCompatActivity {

    String firstNameInput;
    String lastNameInput;
    String emailInput;
    private User user;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private IValidator emailValidator;
    private ILogout logout;
    private IAccessUsers accessUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acc);

        logout = new Logout(this);
        logout.ensureUserLoggedIn();

        emailValidator = ValidatorService.getEmailValidator();
        accessUsers = AccessService.getAccessUsers();
        user = currUser;

        firstNameInput = user.getFirstName();
        lastNameInput = user.getLastName();
        emailInput = user.getEmail();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);

        firstName.getEditText().setText(firstNameInput);
        lastName.getEditText().setText(lastNameInput);
    }

    private boolean checkFirstName() {
        firstNameInput = firstName.getEditText().getText().toString().trim();
        if (firstNameInput.isEmpty()) {
            firstName.setError("First Name cannot be empty!");
            return false;
        } else if (firstNameInput.length() > 15) {
            firstName.setError("First Name too long. Try Again!");
            return false;
        } else {
            firstName.setError(null);
            return true;
        }
    }


    private boolean checkLastName() {
        lastNameInput = lastName.getEditText().getText().toString().trim();
        if (lastNameInput.isEmpty()) {
            lastName.setError("Last Name cannot be empty!");
            return false;
        } else if (lastNameInput.length() > 15) {
            lastName.setError("Last Name too long. Try Again!");
            return false;
        } else {
            lastName.setError(null);
            return true;
        }
    }


    public void confirmInput(View view) {
        if (!checkFirstName() | !checkLastName()) {
            return;
        }

        try {// update the user
            User user = accessUsers.retrieveUser(emailInput);
            if ((user.getEmail()).equals(emailInput)) {
                User updatedUser = new User(emailInput, firstNameInput, lastNameInput, user.getPassword());
                currUser = updatedUser;
                try {
                    accessUsers.updateUser(updatedUser);
                    String toShow = "Account Updated!";
                    Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AccPageUI.class);
                    startActivity(intent);
                } catch (UserNotUpdated userNotUpdated) {
                    String toShow = "Error updating your account. Please try again";
                    Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, EditAccPageUI.class);
                    startActivity(intent);
                }
            } else {
                String toShow = "This email is already in use";
                Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            }
        } catch (UserNotFoundException e) {
            currUser.setFirstName(firstNameInput);
            currUser.setLastName(lastNameInput);
            currUser.setEmail(emailInput);
            String toShow = "Account Updated!";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AccPageUI.class);
            startActivity(intent);

        }
    }

    public void openEditAccPage(View view) {
        Intent intent = new Intent(this, EditAccPageUI.class);
        startActivity(intent);
    }

    public void openResetPassPage(View view) {
        Intent intent = new Intent(this, ResetPassPageUI.class);
        startActivity(intent);
    }

    public void openAccPage(View view) {
        Intent intent = new Intent(this, AccPageUI.class);
        startActivity(intent);
    }
}

