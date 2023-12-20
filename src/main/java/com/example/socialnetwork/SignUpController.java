package com.example.socialnetwork;

import com.example.socialnetwork.java.ir.map.controller.MessageAlert;
import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField firstNameFieldId;
    @FXML
    private TextField lastNameFieldId;
    @FXML
    protected TextField usernameFieldId;
    @FXML
    protected TextField passwordFieldId;

    protected Service service;

    protected Stage dialogStage;


    public void setService(Service service, Stage dialogStage) {
        this.service = service;
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleEnter() {
        String firstName = firstNameFieldId.getText();
        String lastName = lastNameFieldId.getText();
        String username = usernameFieldId.getText();
        String password = passwordFieldId.getText();
        long id = service.findAvailableId();
        try {
            User user = new User(id, username, password, firstName, lastName);
            service.saveUser(id, username, password, firstName, lastName);
            showUserEditDialog(user);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sing Up", "Account created successfully!");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    public void showUserEditDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("home-page-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            // create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Feed");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // set the user into the controller.
            HomePageController controller = loader.getController();
            controller.setService(service, user);

            // show the dialog and wait until the user closes it.
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
