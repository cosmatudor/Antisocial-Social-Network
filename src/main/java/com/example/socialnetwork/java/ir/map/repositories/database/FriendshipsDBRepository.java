package com.example.socialnetwork.java.ir.map.repositories.database;

import com.example.socialnetwork.java.ir.map.domain.*;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipsDBRepository implements IRepository<Tuple<Long, Long>, Friendship> {
    String url;
    String username;
    String password;

    public FriendshipsDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Optional<Friendship> findOne(Tuple<Long, Long> id) {
        String query = "SELECT * FROM friendships WHERE id1 = ? AND id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id.getLeft());
            statement.setLong(2, id.getRight());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                Friendship friendship = new Friendship(id1, id2);
                return Optional.of(friendship);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        // create a sql query to get all users
        String query = "SELECT * FROM friendships";

        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                Friendship friendship = new Friendship(id1, id2);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity can not be null");

        String query = "INSERT INTO friendships (id1, id2, friend_since, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setLong(1, entity.getUser1());
            statement.setLong(2, entity.getUser2());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.setString(4, Status.PENDING.toString());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> id) {
        if (id == null)
            throw new IllegalArgumentException("ID can not be null");

        Friendship fr = findOne(id).orElse(null);

        String query = "DELETE FROM friendships WHERE (id1 = ? and id2 = ?) or (id2 = ? and id1 = ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setLong(1, id.getLeft());
            statement.setLong(2, id.getRight());
            statement.setLong(3, id.getLeft());
            statement.setLong(4, id.getRight());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(fr) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Friendship can't be null!");
        }
        String query = "UPDATE friendships SET status = ? WHERE id1 = ? AND id2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, entity.getStatus().toString());
            statement.setLong(2, entity.getUser1());
            statement.setLong(3, entity.getUser2());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        String query = "SELECT COUNT(*) FROM friendships";

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
