package comp3350.srsys.business;

import comp3350.srsys.exceptions.userexceptions.EmailInUseException;
import comp3350.srsys.exceptions.crudexceptions.UserNotDeletedException;
import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;
import comp3350.srsys.exceptions.crudexceptions.UserNotUpdated;
import comp3350.srsys.objects.User;

public interface IAccessUsers {

    User insertUser(User newUser) throws EmailInUseException;

    User retrieveUser(String userEmail) throws UserNotFoundException;

    User updateUser(User user) throws UserNotUpdated;

    void deleteUser(User user) throws UserNotDeletedException;

    boolean authenticateUser(String email, String password) throws UserNotFoundException;

    void setCurrUserEmail(String email);

    char[] getCurrUserEmail();
}
