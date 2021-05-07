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
import comp3350.srsys.business.UserValidator;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.objects.User;


public class CreateAccPageUI extends AppCompatActivity {

    String firstNameInput;
    String lastNameInput;
    String emailInput;
    String passInput;
    User user;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout email;
    private TextInputLayout password;
    private IAccessUsers accessUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_acc);

        accessUsers = AccessService.getAccessUsers();

        firstName = findViewById(R.id.firstName);
        firstNameInput = firstName.getEditText().getText().toString().trim();
        lastName = findViewById(R.id.lastName);
        lastNameInput = lastName.getEditText().getText().toString().trim();
        email = findViewById(R.id.createAccEmail);
        emailInput = email.getEditText().getText().toString().trim();
        password = findViewById(R.id.createAccPassword);
        passInput = password.getEditText().getText().toString().trim();
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


    private boolean checkEmail() {
        emailInput = email.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Email cannot be empty!");
            return false;
        } else if (!UserValidator.validateEmail(emailInput)) {
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
            password.setError("Weak Password. Try Again!");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }


    public void confirmInput(View view) {
        if (!checkFirstName() | !checkLastName() | !checkEmail() | !checkPassword()) {
            return;
        }
        try {
            accessUsers.retrieveUser(emailInput);
            String toShow = "This email is already in use! Please use a different one";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        } catch (UserNotFoundException e) {
            insertNewUser();
        }
    }

    private void insertNewUser() {
        user = new User(emailInput, firstNameInput, lastNameInput, passInput);
        try {
            accessUsers.insertUser(this.user);
            accountCreatedRedirectAndToast();
        } catch (EmailInUseException emailInUseException) {
            toastEmailInUse();
        }
    }

    private void toastEmailInUse() {
        String toast = emailInput + " already in use.";
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    private void accountCreatedRedirectAndToast() {
        String toShow = "Account Created! \n";
        toShow += "Hey " + firstName.getEditText().getText().toString();
        toShow += "\nWelcome to Swapper";
        toShow += "\nPlease Login again!";
        Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginPageUI.class);
        startActivity(intent);
    }
}