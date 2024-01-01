package com.example.socialnetwork.java.ir.map.controller;

import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameFieldId;
    @FXML
    private TextField passwordFieldId;

    private Service service;

    private Stage dialogStage;

    public void setService(Service service, Stage dialogStage) {
        this.service = service;
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleEnter() {
        String username = usernameFieldId.getText().toString();
        String password = passwordFieldId.getText().toString();
        long id = service.findIdUserByCredentials(username, password);
        if (id == -1) {
            MessageAlert.showErrorMessage(null, "Your email or password are not correct!");
        }
        else {
            User user = service.findOne(id);
            showUserEditDialog(user);
        }
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    public void showUserEditDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/socialnetwork/views/home-page-view.fxml"));
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
