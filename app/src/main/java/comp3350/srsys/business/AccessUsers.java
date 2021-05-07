package comp3350.srsys.business;

import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.exceptions.crudexceptions.UserNotDeletedException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.crudexceptions.UserNotUpdated;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;

public class AccessUsers implements IAccessUsers {

    private final IUserPersistence userPersistence;
    private static char[] currUserEmail;

    public AccessUsers(IUserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public User insertUser(User newUser) throws EmailInUseException {
        boolean emailInUse = userPersistence.retrieveUser(newUser.getEmail()) != null;
        if (emailInUse) {
            throw new EmailInUseException("Cannot add " + newUser.getEmail() +
                    " to the database because it's already in use.");
        }
        userPersistence.insertUser(newUser);
        return userPersistence.retrieveUser(newUser.getEmail());
    }

    @Override
    public User retrieveUser(String userEmail) throws UserNotFoundException {
        User retrievedUser = userPersistence.retrieveUser(userEmail);
        if (retrievedUser == null) {
            throw new UserNotFoundException(userEmail + " was not found.");
        }
        return retrievedUser;
    }

    @Override
    public User updateUser(User user) throws UserNotUpdated {
        try {
            throwExceptionIfUserDoesNotExist(user);
            userPersistence.updateUser(user);
        } catch (NullPointerException e) {
            throw new UserNotUpdated("Cannot update a null user");
        }
        return userPersistence.retrieveUser(user.getEmail());
    }

    @Override
    public void deleteUser(User user) throws UserNotDeletedException {
        try {
            User retrievedUser = retrieveUser(user.getEmail());
            deleteUserFromDB(retrievedUser);
        } catch (NullPointerException e) {
            throw new UserNotDeletedException("Cannot delete a null user");
        } catch (UserNotFoundException e) {
            throw new UserNotDeletedException("Cannot delete " + user.getEmail() +
                    "who doesn't exist in the database.");
        }
    }

    @Override
    public boolean authenticateUser(String email, String password) throws UserNotFoundException {
        try {
            User user = retrieveUser(email);
            if (user.getPassword().equals(password)) {
                return true;
            }
            return false;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(email + " not found in the database.");
        }
    }

    public void setCurrUserEmail(String email) {
        char[] userEmail = email.toCharArray();
        currUserEmail = userEmail;
    }

    public char[] getCurrUserEmail() {
        return currUserEmail;
    }

    private void deleteUserFromDB(User user) throws UserNotDeletedException {
        userPersistence.deleteUser(user);
        boolean userWasNotDeleted = userPersistence.retrieveUser(user.getEmail()) != null;
        if (userWasNotDeleted)
            throw new UserNotDeletedException(user.getEmail() + " was not deleted.");
    }

    private void throwExceptionIfUserDoesNotExist(User user) throws UserNotUpdated {
        boolean userDoesNotExists = userPersistence.retrieveUser(user.getEmail()) == null;
        if (userDoesNotExists)
            throw new UserNotUpdated("Could not update user " + user.getEmail() +
                    " who doesn't exist in the database");
    }
}
