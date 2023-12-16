package com.example.socialnetwork.java.ir.map.repositories.paging;

import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.repositories.DBRepositories.UserDBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<Long, User> {

    public UserDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }
    @Override
    public Page<User> findAll(Pageable pageable) {
        String query1  = "SELECT * FROM users LIMIT ? OFFSET ?";
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

                PreparedStatement statement2 = connection.prepareStatement(query2)
        ) {
            statement1.setInt(1, pageable.getPageSize());
            statement1.setInt(2, pageable.getPageSize() * (pageable.getPageNumber() - 1));
            ResultSet resultSet = statement1.executeQuery();

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(id, username, password, firstName, lastName);

                ArrayList<User> friends = new ArrayList<>(); // list of friends for current user
                statement2.setLong(1, id); // set the id of the current user
                ResultSet resultSet2 = statement2.executeQuery();

                while(resultSet2.next()) { // find the friends of the current user
                    Long id2 = resultSet2.getLong("id");
                    String firstName2 = resultSet2.getString("first_name");
                    String lastName2 = resultSet2.getString("last_name");
                    String username2 = resultSet.getString("username");
                    String password2 = resultSet.getString("password");
                    User friend = new User(id2, username2, password2, firstName2, lastName2);

                    friends.add(friend);
                }

                user.setFriends(friends);
                users.add(user);
            }

            return new Page<User>(pageable, users.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
