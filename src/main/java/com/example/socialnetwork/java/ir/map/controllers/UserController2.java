package com.example.socialnetwork.java.ir.map.controllers;

import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController2 {
    User user;
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> friendsTV;
    @FXML
    TableColumn<User,String> usernameCol;
    @FXML
    TableColumn<User,String> firstNameCol;
    @FXML
    TableColumn<User,String> lastNameCol;

    public void setService(Service service, User user) {
        this.user = user;
        this.service = service;
        initModel();
    }

    private void initModel() {
        List<User> friendsAndItself = service.getFriendsOfUser(this.user.getId());
        friendsAndItself.add(this.user);
        System.out.println(friendsAndItself);

        Iterable<User> users = service.findAllUsers();
        List<User> usersAsList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.clear();
        for (User u : usersAsList) {
            if (!friendsAndItself.contains(u)) {
                model.add(u);
            }
        }

    }

    @FXML
    private void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("secondName"));

        friendsTV.setItems(model);
    }

    public void handleAddFriend() {
        User selectedUser = friendsTV.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            MessageAlert.showErrorMessage(null, "No user selected!");
        } else {
            service.addFriendship(this.user.getId(), selectedUser.getId());
        }
    }
}
