package comp3350.srsys.persistence.database;

import java.util.ArrayList;
import java.util.List;

import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;

public class UserPersistenceFDB implements IUserPersistence {

    private final List<User> userDB;

    public UserPersistenceFDB() {
        userDB = new ArrayList<>();
    }

    public void insertUser(User user) {
        userDB.add(user);
    }

    /* Returns a deep copy of a user with the email provided */
    public User retrieveUser(String email) // returns User object if it finds the user. Otherwise Null
    {
        for (int i = 0; i < userDB.size(); i++) {
            User user = userDB.get(i);
            boolean emailsAreEqual = user.getEmail().equals(email);
            if (emailsAreEqual)
                return user.clone(); // returns a deep copy of the user
        }
        return null;
    }

    public void updateUser(User user) {
        User oldUser = retrieveUser(user.getEmail());
        deleteUser(oldUser);
        insertUser(user);
    }

    public void deleteUser(User user) {
        userDB.remove(user);
    }
}
