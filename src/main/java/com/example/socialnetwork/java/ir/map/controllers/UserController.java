package com.example.socialnetwork.java.ir.map.controllers;

import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.service.Service;
import com.example.socialnetwork.java.ir.map.utils.events.UserChangedEvent;
import com.example.socialnetwork.java.ir.map.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangedEvent> {
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();


    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnSecondName;

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnSecondName.setCellValueFactory(new PropertyValueFactory<User, String>("secondName"));

        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> messages = service.findAllUsers();
        List<User> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(messageTaskList);
    }


    // ----------------- handle buttons -----------------
    // handle for add button
    public void handleAddButton(ActionEvent actionEvent) {
        showUserEditDialog(null);
    }

    // handle for delete button
    public void handleDeleteButton(ActionEvent actionEvent) {
        User selected = (User) tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.deleteUser(selected.getId());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "User deleted successfuly!");
            } catch (Error error) {
                MessageAlert.showErrorMessage(null, "You didn't select any user!");
            }
        }
    }

    // handle for update button
    public void handleUpdateButton(ActionEvent actionEvent) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showUserEditDialog(selected);
        }
        else {
            MessageAlert.showErrorMessage(null, "You didn't select any user!");
        }
    }

    public void showUserEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/socialnetwork/views/user_edit-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            // create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // set the user into the controller.
            UserEditController controller = loader.getController();
            controller.setService(service, dialogStage, user);

            // show the dialog and wait until the user closes it.
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(UserChangedEvent userChangedEvent) {
        initModel();
    }
}