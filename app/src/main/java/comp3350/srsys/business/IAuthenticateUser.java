package comp3350.srsys.business;

import comp3350.srsys.exceptions.crudexceptions.UserNotFoundException;

public interface IAuthenticateUser {
    boolean authenticate (String email, String password) throws UserNotFoundException;
}
