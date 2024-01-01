package com.example.socialnetwork.java.ir.map.controllers;

import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.service.Service;
import com.example.socialnetwork.java.ir.map.validation.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditController {
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldSecondName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;

    private Service service;
    Stage dialogStage;
    User user;

    @FXML
    private void initialize() {
    }

    /* ----------------- public methods ----------------- */
    public void setService(Service service, Stage stage, User user) {
        this.service = service;
        this.dialogStage = stage;
        this.user = user;
        if (user != null) {
            setFields(user);
            textFieldId.setEditable(false);
        }
    }

    @FXML
    public void handleSave() {
        long id = Long.parseLong(textFieldId.getText());
        // debug and print id
        System.out.println(id);
        String firstName = textFieldFirstName.getText();
        String secondName = textFieldSecondName.getText();
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        User u = new User(id, username, password, firstName, secondName);
        if (this.user == null) {
            saveUser(u);
        }
        else {
            updateUser(u);
        }
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }


    /* ----------------- private methods ----------------- */
    private void setFields(User user) {
        textFieldId.setText(user.getId().toString());
        textFieldFirstName.setText(user.getFirstName());
        textFieldSecondName.setText(user.getSecondName());
        textFieldUsername.setText(user.getUsername());
        textFieldPassword.setText(user.getPassword());
    }

    private void saveUser(User user) {
        try {
            this.service.saveUser(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getSecondName());
            dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Save", "User saved successfully!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void updateUser(User user) {
        try {
            this.service.updateUser(user);
            dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Update", "User has been updated successfully!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }
}
