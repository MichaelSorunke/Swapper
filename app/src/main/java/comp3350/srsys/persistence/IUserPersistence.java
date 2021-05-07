package comp3350.srsys.persistence;

import comp3350.srsys.objects.User;

public interface IUserPersistence {
    void insertUser(User user);

    /* Returns a deep copy of a user with the email provided */
    User retrieveUser(String email);

    void updateUser(User user);

    void deleteUser(User user);
}
