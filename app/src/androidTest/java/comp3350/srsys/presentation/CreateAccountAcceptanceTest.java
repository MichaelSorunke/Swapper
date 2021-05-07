package comp3350.srsys.presentation;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.example.swapper.R;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.PersistenceService;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/* The steps this acceptance test goes through are:
 * 1. Start with a fresh install at the MainActivity screen
 * 2. Press "LETS GO" button to go to the login page
 * 3. Press "Create Account" link to go to the create account page
 * 4. Type in name, last name, email, and password into those fields
 * 5. Press "Create Account" button and get moved to the login page
 * 6. Type in email and password
 * 7. Press "Login" button
 * 8. Verify we're now on the homepage
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateAccountAcceptanceTest {

    private static final String FIRST_NAME = "Bob";
    private static final String LAST_NAME = "Bobby";
    private static final String EMAIL = "bob@yahoo.com";
    private static final String PASSWORD = "ChocolateCake@!2";
    private static IUserPersistence userPersistence;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    /* Ensure the user doesn't exist in the database prior to running the test otherwise error will ensue
    *  from trying to add a user that already exists. */
    @Before
    public void cleanUp() {
        userPersistence = PersistenceService.getUserPersistence();
        deleteUserIfTheyExist();
    }

    /* Do a final cleanup of removing the user from the database so these tests don't potentially affect
    *  other tests */
    @AfterClass
    public static void finalCleanUp() {
        deleteUserIfTheyExist();
    }

    @Test
    public void CreateAccountGeneralCase() {
        // 1. Start on the LETS GO page (main activity)
        ViewInteraction mainActivityLayout = onView(withId(R.id.mainActivityLayout));
            mainActivityLayout.check(matches(isDisplayed()));

        // 2. press the LETS GO button
        ViewInteraction letsGoButton = onView(withId(R.id.letsGoButton));
        letsGoButton
                .check(matches(isClickable()))
                .perform(click());

        // Verify we're on the login page and there's the login form
        isOnLoginPage();

        // 3. Press "Create Account" link/button
        ViewInteraction createAccountTextField = onView(withId(R.id.createAccountText));
        createAccountTextField
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());

        // Verify we're on the create account page
        onView(withId(R.id.createAccountLayout)).check(matches(isDisplayed()));
        ViewInteraction firstName = onView(withId(R.id.firstNameTextInput));
        ViewInteraction lastName = onView(withId(R.id.lastNameTextInput));
        ViewInteraction email = onView(withId(R.id.createAccountEmailInput));
        ViewInteraction password = onView(withId(R.id.createAccountPasswordTextInput));
        ViewInteraction createAccountButton = onView(withId(R.id.createAccountButton));
        firstName.check(matches(isDisplayed()));
        lastName.check(matches(isDisplayed()));
        email.check(matches(isDisplayed()));
        password.check(matches(isDisplayed()));
        createAccountButton.check(matches(isDisplayed()));
        createAccountButton.check(matches(isClickable()));

        // 4. Type in a name, email, and password
        firstName.perform(typeText(FIRST_NAME));
        lastName.perform(typeText(LAST_NAME));
        email.perform(typeText(EMAIL));
        Espresso.closeSoftKeyboard(); // required otherwise the keyboard is in the way of clicking on the password textInput
        password.perform(typeText(PASSWORD));

        // 5. Press create account button
        Espresso.closeSoftKeyboard(); // required otherwise the keyboard is in the way of clicking on the createAccount button
        createAccountButton.perform(click());

        // Verify we're back on the login page
        isOnLoginPage();

        // 6. Type in email and password used
        onView(withId(R.id.loginEmailTextField)).perform(typeText(EMAIL));
        onView(withId(R.id.loginPasswordTextInput)).perform(typeText(PASSWORD));

        // 7. Press the login button
        onView(withId(R.id.signInButton)).perform(click());

        // 8. Verify we're on the homepage
        onView(withId(R.id.homepageLayout)).check(matches(isDisplayed()));
    }

    private void isOnLoginPage() {
        onView(withId(R.id.loginPage)).check(matches(isDisplayed()));
        onView(withId(R.id.loginForm)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailTextField)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPasswordTextInput)).check(matches(isDisplayed()));
        onView(withId(R.id.signInButton)).check(matches(isDisplayed()));
    }

    private static void deleteUserIfTheyExist() {
        boolean userExistsInDatabase = userPersistence.retrieveUser(EMAIL) != null;
        Log.i("CreateAccountAcceptanceTest", userExistsInDatabase ? "Deleting " + EMAIL + " from the database" : EMAIL + " doesn't exist in the database.");
        if (userExistsInDatabase) {
            userPersistence.deleteUser(new User(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD));
        }
    }
}


