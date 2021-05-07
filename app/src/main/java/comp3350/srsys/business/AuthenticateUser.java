package comp3350.srsys.business;

import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;
import comp3350.srsys.persistence.PersistenceService;

public class AuthenticateUser implements IAuthenticateUser {

    private IUserPersistence userPersistence;
    private static final String TAG = "AuthenticateUser";

    public AuthenticateUser(IUserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public static IAuthenticateUser getInstance() {
        return new AuthenticateUser(PersistenceService.getUserPersistence());
    }

    @Override
    public boolean authenticate(String email, String password) throws UserNotFoundException {
        try {
            User user = userPersistence.retrieveUser(email);
            if (user.getPassword().equals(password)) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            throw new UserNotFoundException(email + " not found in the database.");
        }
    }
}
