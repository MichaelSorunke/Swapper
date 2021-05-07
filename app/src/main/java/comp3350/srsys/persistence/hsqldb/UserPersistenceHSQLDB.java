package comp3350.srsys.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IUserPersistence;

public class UserPersistenceHSQLDB implements IUserPersistence {

    private final String databasePath;

    public UserPersistenceHSQLDB(final String dbPath) {
        databasePath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + databasePath + ";shutdown=true", "SA", "");
    }


    public void insertUser(User user) {
        try(final Connection c = connection()) {
            final PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?)");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    // Email is the unique identifier for any account - no two accounts can share an email
    public User retrieveUser(String email) {
        try (final Connection c = connection()) {
            final PreparedStatement statement = c.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, email);
            final ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                final String userEmail = resultSet.getString("email");
                final String firstName = resultSet.getString("firstName");
                final String lastName = resultSet.getString("lastName");
                final String password = resultSet.getString("password");

                return new User(userEmail, firstName, lastName, password);
            }

            resultSet.close();
            statement.close();


            return null; //<-- i believe since we check for null our special case can be null
        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    public void updateUser(User user) {
        try (final Connection c = connection()) {
            final PreparedStatement preparedStatement = c.prepareStatement("UPDATE users SET firstName = ?, lastName = ?, password = ? WHERE email = ?");
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    public void deleteUser(User user) {
        try (final Connection c = connection()) {
            final PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM users WHERE email = ?");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }
}
