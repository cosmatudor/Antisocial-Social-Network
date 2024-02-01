package com.example.socialnetwork.java.ir.map;

import com.example.socialnetwork.java.ir.map.controllers.LandingPageController;
import com.example.socialnetwork.java.ir.map.domain.Friendship;
import com.example.socialnetwork.java.ir.map.domain.Message;
import com.example.socialnetwork.java.ir.map.domain.Tuple;
import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.repositories.database.FriendshipsDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.database.MessagesDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;
import com.example.socialnetwork.java.ir.map.repositories.paging.IPagingRepository;
import com.example.socialnetwork.java.ir.map.repositories.paging.UserDBPagingRepository;
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
        IPagingRepository<Long, User> userDBRepository = new UserDBPagingRepository(URL, USERNAME, PASSWORD);
        IRepository<Tuple<Long, Long>, Friendship> friendshipDBRepository = new FriendshipsDBRepository(URL, USERNAME, PASSWORD);
        IRepository<Long, Message> messagesDBRepository = new MessagesDBRepository(URL, USERNAME, PASSWORD);

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
        loginLoader.setLocation(getClass().getResource("/com/example/socialnetwork/views/landing-page-view.fxml"));
        AnchorPane userLayout = loginLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LandingPageController landingPageController = loginLoader.getController();
        landingPageController.setService(service);

    }
}