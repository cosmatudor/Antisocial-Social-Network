package com.example.socialnetwork.java.ir.map;

import com.example.socialnetwork.java.ir.map.domain.Message;
import com.example.socialnetwork.java.ir.map.repositories.database.FriendshipsDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.database.MessagesDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.paging.IPagingRepository;
import com.example.socialnetwork.java.ir.map.repositories.paging.UserDBPagingRepository;
import  com.example.socialnetwork.java.ir.map.ui.UI;
import  com.example.socialnetwork.java.ir.map.domain.Friendship;
import  com.example.socialnetwork.java.ir.map.domain.Tuple;
import  com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;
import  com.example.socialnetwork.java.ir.map.service.Service;

public class Main {
    private final static String URL = "jdbc:postgresql://localhost:5433/socialnetwork";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "graspufos135";

    public static void main(String[] args) {
        IPagingRepository<Long, User> userRepo = new UserDBPagingRepository(URL, USERNAME, PASSWORD);
        IRepository<Tuple<Long, Long>, Friendship> friendsRepo = new FriendshipsDBRepository(URL, USERNAME, PASSWORD);
        IRepository<Long, Message> messagesRepo = new MessagesDBRepository(URL, USERNAME, PASSWORD);
        Service service = new Service(userRepo, friendsRepo, messagesRepo);
        UI ui = new UI(service);
        ui.run();
    }
}