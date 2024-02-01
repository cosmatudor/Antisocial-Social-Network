package com.example.socialnetwork.java.ir.map.repositories.database;

import com.example.socialnetwork.java.ir.map.domain.DTO_FriendsOfUser;
import com.example.socialnetwork.java.ir.map.domain.Message;
import com.example.socialnetwork.java.ir.map.domain.Status;
import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class MessagesDBRepository implements IRepository<Long, Message> {
    String url;
    String username;
    String password;

    public MessagesDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null!");
        }

        String query = "SELECT * FROM messages WHERE id=?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long from = resultSet.getLong("sender");
                long to = resultSet.getLong("receiver");
                String text = resultSet.getString("text");
                LocalDate date = resultSet.getDate("date").toLocalDate();

                Message message = new Message(id, from, to, text, date);
                return Optional.of(message);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        ArrayList<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM messages";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while(resultSet.next()) {
                long id = resultSet.getLong("id");
                long from = resultSet.getLong("sender");
                long to = resultSet.getLong("receiver");
                String text = resultSet.getString("text");
                LocalDate date = resultSet.getDate("date").toLocalDate();

                Message message = new Message(id, from, to, text, date);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can not be null!");
        }

        String query = "INSERT INTO messages(ID, sender, receiver, text, date) values (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getFrom());
            statement.setLong(3, entity.getTo());
            statement.setString(4, entity.getText());
            statement.setDate(5, Date.valueOf(entity.getDate()));

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null!");
        }

        Message message = findOne(id).orElse(null);

        String query = "DELETE FROM messages WHERE id=?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            int response = statement.executeUpdate();
            return response != 0 ? Optional.of(message) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        String query = "SELECT COUNT(*) FROM messages";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

}
