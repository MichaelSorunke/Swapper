package comp3350.srsys.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;
import comp3350.srsys.persistence.IListingPersistence;

public class ListingPersistenceHSQLDB implements IListingPersistence {

    private final String databasePath;

    public ListingPersistenceHSQLDB(final String dbPath) {
        databasePath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + databasePath + ";shutdown=true", "SA", "");
    }

    @Override
    public void insertListing(Listing listing) {
        try (final Connection c = connection()) {
            final PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO listings VALUES(?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, listing.getTitle());
            preparedStatement.setInt(2, listing.getID());
            // java.Util.Date can be converted to and from a long so we use that instead of converting to java.Sql.Date
            preparedStatement.setLong(3, listing.getDatePosted().getTime());
            preparedStatement.setString(4, listing.getCategory());
            preparedStatement.setInt(5, listing.getPriority());
            preparedStatement.setString(6, listing.getDescription());
            preparedStatement.setString(7, listing.getUserEmail());
            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    @Override
    public Listing retrieveListing(Integer listingID) {
        try (final Connection c = connection()) {
            final PreparedStatement statement = c.prepareStatement("SELECT * FROM listings WHERE id = ?");
            statement.setInt(1, listingID);
            final ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                final String title = resultSet.getString("title");
                final Integer id = resultSet.getInt("id");
                final String category = resultSet.getString("category");
                final Integer priority = resultSet.getInt("priority");
                final String description = resultSet.getString("description");
                final String userEmail = resultSet.getString("userEmail");
                final long date = resultSet.getLong("dateposted");

                Listing retrievedListing = new Listing(id, title, priority, category, userEmail, description);
                retrievedListing.getDatePosted().setTime(date); // set the time to the time stored in the database

                return retrievedListing;
            }

            resultSet.close();
            statement.close();


            return null; //<-- i believe since we check for null our special case can be null
        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    @Override
    public List<Listing> retrieveUserListings(User user) {
        final List<Listing> userListings = new ArrayList<>();

        try (final Connection c = connection()) {
            final PreparedStatement statement = c.prepareStatement("SELECT * FROM listings WHERE userEmail = ?");
            statement.setString(1, user.getEmail());
            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Listing currListing = retrieveListing(id);
                userListings.add(currListing);
            }
            resultSet.close();
            statement.close();

            return userListings;
        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    @Override
    public List<Listing> retrieveCategoryListings(String category) {
        final List<Listing> categoryListings = new ArrayList<>();

        try (final Connection c = connection()) {
            final PreparedStatement statement = c.prepareStatement("SELECT * FROM listings WHERE category = ? ORDER BY dateposted DESC");
            statement.setString(1, category);
            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Listing currListing = retrieveListing(id);
                categoryListings.add(currListing);
            }
            resultSet.close();
            statement.close();

            return categoryListings;
        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    @Override
    public void updateListing(Listing listing) {
        try (final Connection c = connection()) {
            final PreparedStatement preparedStatement = c.prepareStatement("UPDATE listings SET title = ?, datePosted = ?, category = ?, priority = ?, description = ?, userEmail = ? WHERE id = ?");
            preparedStatement.setString(1, listing.getTitle());
            preparedStatement.setLong(2, listing.getDatePosted().getTime());
            preparedStatement.setString(3, listing.getCategory());
            preparedStatement.setInt(4, listing.getPriority());
            preparedStatement.setString(5, listing.getDescription());
            preparedStatement.setString(6, listing.getUserEmail());
            preparedStatement.setInt(7, listing.getID());
            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    @Override
    public void deleteListing(Listing listing) {
        try (final Connection c = connection()) {
            final PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM listings WHERE id = ?");
            preparedStatement.setInt(1, listing.getID());
            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }
    }

    @Override
    public Integer getLargestID() {
        Integer id = -1;

        try (final Connection c = connection()) {
                final PreparedStatement preparedStatement = c.prepareStatement("SELECT TOP 1 * FROM listings ORDER BY id DESC");
                final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                id = resultSet.getInt("id");

            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            throw new RuntimeException(e); //might have to make our own PersistenceException.java
        }

        return id;
    }
}
