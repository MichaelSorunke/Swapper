package comp3350.srsys.presentation;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.swapper.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IListingPersistence;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.PersistenceService;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


/* Assumptions:
 * - There's already an account in the database for us to log in with
 * Steps:
 * 1. Start with a fresh install at the MainActivity screen
 * 2. Press LETS GO button to go the login page
 * 3. Login with the credentials defined below
 * 4. Press "Add" button at the bottom of the homepage
 * 5. Select a category, and enter listing information
 * 6. Press "POST!" button
 * 7. Press the category of the listing
 * 8. Verify the listing exists
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateListingAcceptanceTest {

    private IUserPersistence userPersistence;
    private IListingPersistence listingPersistence;
    private User defaultUser = new User("bob@yahoo.com", "Bob", "Bobert", "ChocolateCake@!2");
    private static final String TITLE = "Couch";
    private static final String CATEGORY = "Furniture";
    private static final String DESCRIPTION = "Blue-navy suede couch. Barely used and recently dry cleaned";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setupAccount() {
        userPersistence = PersistenceService.getUserPersistence();
        listingPersistence = PersistenceService.getListingPersistence();
        /* we delete the user then re-insert them in case they already exist to avoid integrity constraint
         * errors. The user will remain once the tests complete if they existed before performing the tests. */
        userPersistence.deleteUser(defaultUser);
        userPersistence.insertUser(defaultUser);
    }

    @After
    public void cleanUp() {
        deleteListingsWithSameTitleAndDescription();
    }

    @Test
    public void createListingGeneralCase() {
        // 1. Start with a fresh install at the MainActivity screen
        ViewInteraction mainActivityLayout = onView(withId(R.id.mainActivityLayout));
            mainActivityLayout.check(matches(isDisplayed()));
        // 2. Press LETS GO button to go the login page
        ViewInteraction letsGoButton = onView(withId(R.id.letsGoButton));
        letsGoButton
                .check(matches(isClickable()))
                .perform(click());

        isOnLoginPage();
        // 3. Login with the credentials defined in defaultUser
        onView(withId(R.id.loginEmailTextField)).perform(typeText(defaultUser.getEmail()));
        onView(withId(R.id.loginPasswordTextInput)).perform(typeText(defaultUser.getPassword()));
        onView(withId(R.id.signInButton)).perform(click());

        // 4. Press "Add" button at the bottom of the homepage
        ViewInteraction addListingButton = onView(withId(R.id.add));
        addListingButton
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());

        // 5. Select a category, and enter listing information
        onView(withId(R.id.categorySpinner)).perform(click());
        onView(withText(CATEGORY)).perform(click());
        onView(withId(R.id.createListingTitleEditText)).perform(typeText(TITLE));
        onView(withId(R.id.createListingDescriptionEditText)).perform(typeText(DESCRIPTION));
        Espresso.closeSoftKeyboard();
        // 6. Press "POST!" button
        onView(withId(R.id.createListingButton))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());
        // 7. Press the category of the listing
        onView(withText(CATEGORY)).perform(click());
        // 8. Verify the listing exists
        onView(withText(TITLE)).check(matches(isDisplayed()));
        onView(withText(DESCRIPTION)).check(matches(isDisplayed()));
    }

    private void isOnLoginPage() {
        onView(withId(R.id.loginPage)).check(matches(isDisplayed()));
        onView(withId(R.id.loginForm)).check(matches(isDisplayed()));
        onView(withId(R.id.loginEmailTextField)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPasswordTextInput)).check(matches(isDisplayed()));
        onView(withId(R.id.signInButton)).check(matches(isDisplayed()));
    }

    /* Deletes listings that were created by the defaultUser, have the same title, and
     * same description, i.e., deletes listings created during testing */
    private void deleteListingsWithSameTitleAndDescription() {
        List<Listing> userListings = listingPersistence.retrieveUserListings(defaultUser);

        for (Listing listing : userListings) {
            boolean sameTitle = listing.getTitle().equals(TITLE);
            boolean sameDescription = listing.getDescription().equals(DESCRIPTION);
            if (sameTitle && sameDescription) {
                listingPersistence.deleteListing(listing);
            }
        }
    }
}
