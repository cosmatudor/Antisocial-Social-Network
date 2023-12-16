package com.example.socialnetwork.java.ir.map.controller;

import com.example.socialnetwork.UserController2;
import com.example.socialnetwork.java.ir.map.controller.MessageAlert;
import com.example.socialnetwork.java.ir.map.domain.Status;
import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.service.Service;
import com.example.socialnetwork.java.ir.map.utils.events.UserChangedEvent;
import com.example.socialnetwork.java.ir.map.utils.observer.Observer;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class HomePageController implements Observer<UserChangedEvent> {
    User user;
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();
    ObservableList<String> modelReceived = FXCollections.observableArrayList();
    ObservableList<String> modelSent = FXCollections.observableArrayList();
    ObservableList<String> modelMessagesFriends = FXCollections.observableArrayList();
    ObservableList<String> modelMessages = FXCollections.observableArrayList();

    @FXML
    TableView<User> friendsTableView;
    @FXML
    TableColumn<User,String> tableFriendsColumnUsername;
    @FXML
    TableColumn<User,String> tableFriendsColumnFirstName;
    @FXML
    TableColumn<User,String> tableFriendsColumnSecondName;
    @FXML
    ListView<String> receivedList;
    @FXML
    ListView<String> sentList;
    @FXML
    ListView<String> messagesFriendList;
    @FXML
    TextField textField;
    @FXML
    ListView<String> messagesList;

    public void setService(Service service, User user) {
        this.user = user;
        this.service = service;
        this.service.addObserver(this);
        initModels();
    }

    @FXML
    private void initModels() {
        System.out.println("initModels() called");

        // ----------- Friends Table -----------
        Iterable<User> users = service.findAllUsers();
        List<User> usersAsList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList()); // convert to list
        model.setAll(service.getFriendsOfUser(this.user.getId()));

        // ----------- Received List -----------
        modelReceived.clear();
        service.getUsersForReceived(this.user.getId()).forEach(
                u -> modelReceived.add(u.getFirstName() + " " + u.getSecondName() + "(" + u.getUsername() + ")" + " sent you a friend request!")
        );

        // ----------- Sent List -----------
        modelSent.clear();
        service.getUsersForSent(this.user.getId()).forEach(
                u -> modelSent.add("You sent a friend request to " + u.getFirstName() + " " + u.getSecondName() + "(" + u.getUsername() + ")")
        );

        // ----------- Message Friends List -----------
        modelMessagesFriends.clear();
        service.getFriendsOfUser(this.user.getId()).forEach(
                u -> modelMessagesFriends.add(u.getFirstName() + " " + u.getSecondName() + "(" + u.getUsername() + ")")
        );

        // ----------- Messages List -----------
        modelMessages.clear();
        service.getMessagesBetweenTwoUsers(this.user.getId(), _getIdOfFriend()).forEach(
                m -> modelMessages.add(service.findOne(m.getFrom()).getFirstName() + ": " + m.getText())
        );
    }


    @FXML
    private void initialize() {
        // ----------- Friends Table -----------
        tableFriendsColumnUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        tableFriendsColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableFriendsColumnSecondName.setCellValueFactory(new PropertyValueFactory<User, String>("secondName"));
        friendsTableView.setItems(model);

        // ----------- Received List -----------
        receivedList.setItems(modelReceived);

        // ----------- Sent List -----------
        sentList.setItems(modelSent);

        // ----------- Message Friends List -----------
        messagesFriendList.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        messagesFriendList.setItems(modelMessagesFriends);

        // ----------- Messages List -----------
        messagesList.setItems(modelMessages);
    }


    private void showUserNonFriendsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("user-view2.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            // create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Friend");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // set the user into the controller.
            UserController2 controller = loader.getController();
            controller.setService(service, user);

            // show the dialog and wait until the user closes it.
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ----------- Handlers for Buttons -----------
    public void handleAcceptFriend(ActionEvent actionEvent) {
        String str = receivedList.getSelectionModel().getSelectedItem();
        if (!str.isEmpty()) {
            // take the string between "(" and ")" from str
            String username = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
            long id = service.findUserByUsername(username);
            service.updateFriendRequest(id, this.user.getId(), Status.ACCEPTED);
            // delete the request from the list
            modelReceived.remove(str);
        }
        else {
            MessageAlert.showErrorMessage(null, "You didn't select any user!");
        }
    }

    public void handleDeclineFriend(ActionEvent actionEvent) {
        String str = receivedList.getSelectionModel().getSelectedItem();
        if (!str.isEmpty()) {
            // take the string between "(" and ")" from str
            String username = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
            long id = service.findUserByUsername(username);
            service.updateFriendRequest(id, this.user.getId(), Status.REJECTED);
        }
        else {
            MessageAlert.showErrorMessage(null, "You didn't select any user!");
        }
    }

    public void handleAddFriend(ActionEvent actionEvent) {
        showUserNonFriendsDialog();
    }

    public void sendMessage(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            // get the message from Text Field
            String text = _getTextFromField();

            // get the id of my friend that I'm chatting with
            ArrayList<Long> ids = _getIdOfFriends();
            if (ids != null) {
                ids.forEach(id -> service.saveMessage(this.user.getId(), id, text));

                // refresh the pane so the new message can be visible
                modelMessages.clear();
                service.getMessagesBetweenTwoUsers(this.user.getId(), ids.get(0)).forEach(
                        m -> modelMessages.add(service.findOne(m.getFrom()).getFirstName() + ": " + m.getText())
                );
            }
        }
    }

    public void handleMouseClick(MouseEvent actionEvent) {
        modelMessages.clear();
        service.getMessagesBetweenTwoUsers(this.user.getId(), _getIdOfFriend()).forEach(
                m -> modelMessages.add(service.findOne(m.getFrom()).getFirstName() + ":" + m.getText())
        );
    }


    // ----------- Utils -----------
    private String _getTextFromField() {
        String text = textField.getText();
        textField.clear();
        return text;
    }

    private ArrayList<Long> _getIdOfFriends() {
        ArrayList<Long> ids = messagesFriendList.getSelectionModel().getSelectedItems().stream()
                .map(str -> str.substring(str.indexOf("(") + 1, str.indexOf(")")))
                .filter(Objects::nonNull)
                .map(username -> service.findUserByUsername(username))
                .collect(Collectors.toCollection(ArrayList::new));
        if (ids.isEmpty()) {
            MessageAlert.showErrorMessage(null, "You didn't select any user!");
            return null;
        }
        return ids;
    }

    private long _getIdOfFriend() {
        System.out.println("getIdOfFriend() called");
        String str = messagesFriendList.getSelectionModel().getSelectedItem();
        if (str == null) {
            return this.user.getId();
        }
        if (!str.isEmpty()) {
            String username = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
            return service.findUserByUsername(username);
        }
        else {
            MessageAlert.showErrorMessage(null, "You didn't select any user!");
            return -1;
        }
    }


    // ----------- Observer Pattern -----------
    @Override
    public void update(UserChangedEvent userChangedEvent) {
        initModels();
        modelMessages.clear();
        service.getMessagesBetweenTwoUsers(this.user.getId(), _getIdOfFriend()).forEach(
                m -> modelMessages.add(service.findOne(m.getFrom()).getFirstName() + ":" + m.getText())
        );
    }
}
