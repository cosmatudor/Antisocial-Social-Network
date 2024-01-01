package com.example.socialnetwork.java.ir.map.repositories.DBRepositories;

import com.example.socialnetwork.java.ir.map.domain.DTO_FriendsOfUser;
import com.example.socialnetwork.java.ir.map.domain.Status;
import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.repositories.IRepository;
import com.example.socialnetwork.java.ir.map.utils.Crypting;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserDBRepository implements IRepository<Long, User> {
    protected String url;
    protected String username;
    protected String password;

    private SecretKey secretKey;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        String keyString;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/example/socialnetwork/java/ir/map/secret_key"))) {
            keyString = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] keyEncoded = Base64.getDecoder().decode(keyString);
        secretKey = new SecretKeySpec(keyEncoded, 0, keyEncoded.length, "AES");
    }

    @Override
    public Optional<User> findOne(Long aLong) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(id, username, Crypting.decrypt(password, secretKey), firstName, lastName);
                return Optional.of(user);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<DTO_FriendsOfUser> getFriendsOfUser(Long id) {
        String query = """
                select  uu.first_name, uu.last_name, f.friend_since from users u
                inner join friendships f on u.id = f.id1 or u.id = f.id2
                inner join users uu on\s
                (CASE
                    WHEN u.id = f.id1 THEN f.id2 = uu.id
                    ELSE f.id1 = uu.id
                END)
                where u.id = ?""";

        ArrayList<DTO_FriendsOfUser> friends = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                LocalDateTime friendSince = resultSet.getTimestamp("friend_since").toLocalDateTime();
                DTO_FriendsOfUser friend = new DTO_FriendsOfUser(firstName, lastName, friendSince);
                friends.add(friend);
            }

            return friends;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> getFriendsOfUser2(Long id) {
        String query = """
                select  uu.id, uu.first_name, uu.last_name, uu.username, uu.password from users u
                inner join friendships f on u.id = f.id1 or u.id = f.id2
                inner join users uu on\s
                (CASE
                    WHEN u.id = f.id1 THEN f.id2 = uu.id
                    ELSE f.id1 = uu.id
                END)
                where u.id = ?""";

        ArrayList<User> friends = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Long id2 = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User friend = new User(id2, username, Crypting.decrypt(password, secretKey), firstName, lastName);
                friends.add(friend);
            }

            return friends;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> getFriendsOfUserWithStatus(Long id, Status status) {
        String query = """
                select  uu.id, uu.first_name, uu.last_name, uu.username, uu.password from users u
                inner join friendships f on u.id = f.id1 or u.id = f.id2
                inner join users uu on\s
                (CASE
                    WHEN u.id = f.id1 THEN f.id2 = uu.id
                    ELSE f.id1 = uu.id
                END)
                where u.id = ? and f.status = ?""";

        ArrayList<User> friends = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            statement.setString(2, status.toString());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Long id2 = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User friend = new User(id2, username, Crypting.decrypt(password, secretKey), firstName, lastName);
                friends.add(friend);
            }

            return friends;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> getUsersForReceived(Long id) {
        String query = """
                select  uu.id, uu.first_name, uu.last_name, uu.username, uu.password from users u
                inner join friendships f on u.id = f.id2
                inner join users uu on f.id1 = uu.id
                where u.id = ? and f.status = 'PENDING'""";

        ArrayList<User> friends = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Long id2 = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User friend = new User(id2, username, Crypting.decrypt(password, secretKey), firstName, lastName);
                friends.add(friend);
            }

            return friends;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> getUsersForSent(Long id) {
        String query = """
                select  uu.id, uu.first_name, uu.last_name, uu.username, uu.password from users u
                inner join friendships f on u.id = f.id1
                inner join users uu on f.id2 = uu.id
                where u.id = ? and f.status = 'PENDING'""";

        ArrayList<User> friends = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Long id2 = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User friend = new User(id2, username, Crypting.decrypt(password, secretKey), firstName, lastName);
                friends.add(friend);
            }

            return friends;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        // create a sql query to get all users
        String query1  = "SELECT * FROM users";
        String query2 = """
                select  uu.id, uu.first_name, uu.last_name, uu.username, uu.password from users u
                inner join friendships f on u.id = f.id1 or u.id = f.id2
                inner join users uu on\s
                (CASE
                    WHEN u.id = f.id1 THEN f.id2 = uu.id
                    ELSE f.id1 = uu.id
                END)
                where u.id = ?""";

        Set<User> users = new HashSet<>();

        try (
                Connection connection = DriverManager.getConnection(url, username, password);

                PreparedStatement statement1 = connection.prepareStatement(query1);
                ResultSet resultSet = statement1.executeQuery();

                PreparedStatement statement2 = connection.prepareStatement(query2)
        ) {
            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(id, username, Crypting.decrypt(password, secretKey), firstName, lastName);

                ArrayList<User> friends = new ArrayList<>(); // list of friends for current user
                statement2.setLong(1, id); // set the id of the current user
                ResultSet resultSet2 = statement2.executeQuery();
                while(resultSet2.next()) { // find the friends of the current user
                    Long id2 = resultSet2.getLong("id");
                    String firstName2 = resultSet2.getString("first_name");
                    String lastName2 = resultSet2.getString("last_name");
                    String username2 = resultSet.getString("username");
                    String password2 = resultSet.getString("password");
                    User friend = new User(id2, username2, Crypting.decrypt(password2, secretKey), firstName2, lastName2);

                    friends.add(friend);
                }

                user.setFriends(friends);
                users.add(user);
            }

            return users;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<User> save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity can't be null!");

        String querry = "INSERT INTO users(id, first_name, last_name, username, password) values (?, ? ,?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(querry)
        ) {
            statement.setLong(1,entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getSecondName());
            statement.setString(4, entity.getUsername());
            statement.setString(5, Crypting.encrypt(entity.getPassword(), secretKey));

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<User> delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("ID can not be null");

        User user = findOne(id).orElse(null);

        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(user) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("User can't be null!");

        String query = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getSecondName());
            statement.setLong(3, entity.getId());

            int response = statement.executeUpdate();
            return response == 1 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        String query = "SELECT COUNT(*) FROM users";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();
        ) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
