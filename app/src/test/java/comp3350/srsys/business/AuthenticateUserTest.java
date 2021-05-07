package comp3350.srsys.business;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AuthenticateUserTest {

    @Mock private IUserPersistence userPersistence;

    @InjectMocks
    private AuthenticateUser authenticateUser;

    private static User defaultUser = new User("bob@yahoo.com", "Bob", "Bobert", "12345");

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void authenticateUserGeneralCase() throws EmailInUseException {
        boolean exceptionOccurred = false;
        boolean passwordCorrect = false;


        when(userPersistence.retrieveUser(defaultUser.getEmail())).thenReturn(defaultUser);

        try {
            passwordCorrect = authenticateUser.authenticate(defaultUser.getEmail(), defaultUser.getPassword());
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        verify(userPersistence).retrieveUser(defaultUser.getEmail());
        assertFalse(exceptionOccurred);
        assertTrue(passwordCorrect);
    }

    @Test
    public void authenticateUserIncorrectPassword() {
        boolean exceptionOccurred = false;
        boolean passwordCorrect = false;

        when(userPersistence.retrieveUser(defaultUser.getEmail())).thenReturn(defaultUser);

        try {
            passwordCorrect = authenticateUser.authenticate(defaultUser.getEmail(), "Donkey");
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        verify(userPersistence).retrieveUser(defaultUser.getEmail());
        assertFalse(exceptionOccurred);
        assertFalse(passwordCorrect);
    }

    @Test
    public void authenticateUserUserNotFound() {
        User testUser = new User("sanders@myumanitoba.ca", "Bernie", "Sanders", "sanders123");
        boolean exceptionOccurred = false;

        when(userPersistence.retrieveUser(testUser.getEmail())).thenReturn(null);

        try {
            authenticateUser.authenticate(testUser.getEmail(), testUser.getPassword());
        } catch (UserNotFoundException e) {
            exceptionOccurred = true;
        }

        verify(userPersistence).retrieveUser(testUser.getEmail());
        assertTrue(exceptionOccurred);
    }

    @Test
    public void authenticateUserGivenNullEmail() {
        boolean userNotFound = false;

        when(userPersistence.retrieveUser(null)).thenReturn(null);

        try {
            authenticateUser.authenticate(null, defaultUser.getPassword());
        } catch (UserNotFoundException e) {
            userNotFound = true;
        }

        verify(userPersistence).retrieveUser(null);
        assertTrue(userNotFound); // cannot find a null user
    }

    @Test
    public void authenticateUserGivenNullPassword() throws UserNotFoundException {
        boolean isValidCredentials = false;

        when(userPersistence.retrieveUser(defaultUser.getEmail())).thenReturn(defaultUser);

        isValidCredentials = authenticateUser.authenticate(defaultUser.getEmail(), null);

        verify(userPersistence).retrieveUser(defaultUser.getEmail());
        assertFalse(isValidCredentials); // null password doesn't match the user's password
    }
}