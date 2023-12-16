package com.example.socialnetwork;

import com.example.socialnetwork.java.ir.map.repositories.DBRepositories.FriendshipsDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.DBRepositories.MessagesDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.DBRepositories.UserDBRepository;
import com.example.socialnetwork.java.ir.map.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserApplication extends Application {

    private Service service;

    @Override
    public void start(Stage stage) throws IOException {

        final String URL = "jdbc:postgresql://localhost:5433/socialnetwork";
        final String USERNAME = "postgres";
        final String PASSWORD = "graspufos135";
        UserDBRepository userDBRepository = new UserDBRepository(URL, USERNAME, PASSWORD);
        FriendshipsDBRepository friendshipDBRepository = new FriendshipsDBRepository(URL, USERNAME, PASSWORD);
        MessagesDBRepository messagesDBRepository = new MessagesDBRepository(URL, USERNAME, PASSWORD);

        service = new Service(userDBRepository, friendshipDBRepository, messagesDBRepository);

        initView(stage);
        stage.setWidth(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("landing-page-view.fxml"));
        AnchorPane userLayout = loginLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LandingPageController landingPageController = loginLoader.getController();
        landingPageController.setService(service);

    }
}