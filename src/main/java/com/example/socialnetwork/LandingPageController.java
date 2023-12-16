package com.example.socialnetwork;

import com.example.socialnetwork.LoginController;
import com.example.socialnetwork.SignUpController;
import com.example.socialnetwork.java.ir.map.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {
    private Service service;

    @FXML
    private void initialize() {}

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void handleLogin() {
        showUserLoginDialog();
    }

    @FXML
    public void handleSignUp() {
        showUserSignUpDialog();
    }

    public void showUserLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.setService(service, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showUserSignUpDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("C:\\Users\\tudor\\OneDrive\\Desktop\\SocialNetwork\\src\\main\\resources\\com\\example\\socialnetwork\\login-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sign Up");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SignUpController signUpController = loader.getController();
            signUpController.setService(service, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
