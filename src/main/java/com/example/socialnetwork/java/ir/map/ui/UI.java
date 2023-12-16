package com.example.socialnetwork.java.ir.map.ui;

import  com.example.socialnetwork.java.ir.map.domain.Friendship;
import  com.example.socialnetwork.java.ir.map.domain.User;
import  com.example.socialnetwork.java.ir.map.service.Service;

import java.util.List;
import java.util.Scanner;

public class UI {
    Scanner in = new Scanner(System.in);
    Service service;

    public UI(Service service) {
        this.service = service;
    }

    public void run() {
        int input;
        while( true ) {
            System.out.printf("OPTION: ");
            input = in.nextInt();

            if (input == 0) {
                break;
            }

            if (input == 1) {
                Iterable<User> listOfUsers = service.findAllUsers();
                for(User u : listOfUsers) {
                    System.out.println(u);
                }

                System.out.println("=====================================");

                Iterable<Friendship> listOfFriends = service.findAllFriendships();
                for (Friendship f : listOfFriends) {
                    System.out.println(f);
                }
            }

            if (input == 2) {
                System.out.println("ID: ");
                long id = in.nextLong();
                System.out.println("First name: ");
                String fn = in.next();
                System.out.println("Second name: ");
                String sn = in.next();
                System.out.println("Username: ");
                String us = in.next();
                System.out.println("Password: ");
                String ps = in.next();
                try {
                    service.saveUser(id, us, ps, fn, sn);
                    System.out.println("User saved!");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (input == 3) {
                System.out.println("Enter the ID of the user you want to delete: ");
                long id = in.nextLong();
                try {
                    if (id < 0) {
                        throw new RuntimeException("ID must be positive!");
                    }
                    service.deleteUser(id);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (input == 4) {
                System.out.println("Add a friendship between 2 people");
                System.out.println("First ID: ");
                Long id1 = in.nextLong();
                System.out.println("Second ID: ");
                Long id2 = in.nextLong();
                try {
                    service.addFriendship(id1, id2);
                    System.out.println("Friendship added!");
                    System.out.println(service.findAllFriendships());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (input == 5) {
                System.out.println("Delete a friendship between 2 people");
                System.out.println("First ID: ");
                Long id1 = in.nextLong();
                System.out.println("Second ID: ");
                Long id2 = in.nextLong();
                try {
                    service.deleteFriendship(id1, id2);
                    System.out.println("Friendship deleted!");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (input == 6) {
                int numberOfComunities = service.numberOfComunities();
                System.out.println("Number of comunities: " + numberOfComunities);
            }

            if (input == 7) {
                List<List<User>> listOfMostSociableComunities = service.mostSociableComunities();
                System.out.println("Most sociable comunities: ");
                for (List<User> comunity : listOfMostSociableComunities) {
                    for (User u : comunity) {
                        System.out.println(u);
                    }
                    System.out.println("=====================================");
                }
            }

            if (input == 8) {
                System.out.println("ID: ");
                long id = in.nextLong();
                User user = service.findOne(id);
                for (User friends : user.getFriends()) {
                    System.out.println(friends.getFirstName());
                }
            }

            if (input == 9) {
                System.out.println("ID: ");
                long id = in.nextLong();
                System.out.println("Month: ");
                int month = in.nextInt();
                System.out.println(service.getFriendsOfUserFromDate(id,month));
            }

        }
    }
}
