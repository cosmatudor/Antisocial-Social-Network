package com.example.socialnetwork.java.ir.map.service;

import com.example.socialnetwork.java.ir.map.domain.*;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;
import com.example.socialnetwork.java.ir.map.repositories.paging.*;
import com.example.socialnetwork.java.ir.map.utils.events.ChangeEventType;
import com.example.socialnetwork.java.ir.map.utils.events.UserChangedEvent;
import com.example.socialnetwork.java.ir.map.utils.observer.Observable;
import com.example.socialnetwork.java.ir.map.utils.observer.Observer;
import  com.example.socialnetwork.java.ir.map.validation.IValidator;
import  com.example.socialnetwork.java.ir.map.validation.ValidationException;

import java.util.*;
import java.util.stream.Collectors;


public class Service implements Observable<UserChangedEvent> {
    private IPagingRepository<Long, User> usersRepo;
    private IRepository<Tuple<Long, Long>, Friendship> friendsRepo;
    private IRepository<Long, Message> messagesRepo;

    private final IValidator<User> userValidator;
    private final IValidator<Friendship> friendshipValidator;

    private Map<User, Integer> comunityOfUser = new HashMap<>();
    private List<Observer<UserChangedEvent>> observers = new ArrayList<>();

    private int page = 1;
    private int size = 0;

    private int pageFR = 1;
    private int sizeFR = 0;

    private int pageFR2 = 1;
    private int sizeFR2 = 0;

    /**
     * Constructor
     *
     * @param userRepository - the repository for users
     * @param friendsRepo    - the repository for friendships
     */
    public Service(IPagingRepository<Long, User> userRepository, IRepository<Tuple<Long, Long>, Friendship> friendsRepo, IRepository<Long, Message> messagesRepo) {
        this.usersRepo = userRepository;
        this.friendsRepo = friendsRepo;
        this.messagesRepo = messagesRepo;

        // validator for users
        this.userValidator = (User user) -> {
            if (user.getFirstName() == null || user.getFirstName().equals("gaga")) {
                throw new ValidationException("Firsr Name cannot be empty!");
            }
            if (user.getSecondName() == null || user.getSecondName().isEmpty()) {
                throw new ValidationException("Second Name cannot be empty!");
            }
            if (user.getId() < 0) {
                throw new ValidationException("Id cannot be negative!");
            }
        };

        // validator for friendships
        this.friendshipValidator = (Friendship friendship) -> {
            if (friendship.getUser1() == friendship.getUser2()) {
                throw new ValidationException("You cannot be friends with yourself!");
            }
        };
    }


    // -------------USER-------------

    /**
     * Adds a user to the repository
     *
     * @param id        - the id of the user
     * @param firstName - the first name of the user
     * @param lastName  - the last name of the user
     */
    public void saveUser(long id, String username, String password, String firstName, String lastName) {
        User entity = new User(id, username, password, firstName, lastName);
        userValidator.validate(entity);
        if (usersRepo.save(entity).isPresent()) {
            throw new RuntimeException("ID of user already exists!");
        }
        notifyObservers(new UserChangedEvent(ChangeEventType.ADD, entity));
    }

    /**
     * Deletes a user from the repository
     * @param id - the id of the user
     * - null, otherwise
     */
    public void deleteUser(long id) {
        User user = findOne(id);
        if (user == null) {
            throw new RuntimeException("There is no user with the given id!"); //!!!!!!!!!!!!!!!!!!!!
        } else {
//            for (User friends : user.getFriends()) {
//                deleteFriendship(id, friends.getId());
//            }
//            user.getFriends().forEach(friend -> deleteFriendship(id, friend.getId()));
            usersRepo.delete(id);
            notifyObservers(new UserChangedEvent(ChangeEventType.DELETE, user));
        }
    }

    /***
     * Updates a user from the repository
     * @param user - the updated user
     */
    public void updateUser(User user) {
        userValidator.validate(user);
        if (usersRepo.update(user).isPresent()) {
            throw new RuntimeException("There is no user with the given id!");
        }
        notifyObservers(new UserChangedEvent(ChangeEventType.UPDATE, user));
    }

    /**
     * @return the number of users
     */
    public int sizeOfUsers() {
        return usersRepo.size();
    }

    /**
     * @param id - the id of the user
     * @return the user with the given id, if it exists
     * null, otherwise
     */
    public User findOne(long id) {
        Optional<User> opt = usersRepo.findOne(id);
        return opt.orElse(null);
    }

    /**
     * @return all the users
     */
    public Iterable<User> findAllUsers() {
        return usersRepo.findAll();
    }

    public long findIdUserByCredentials(String username, String password) {
        Iterable<User> users = usersRepo.findAll();
        for (User u : users) {
            System.out.println(u.getUsername() + " " + u.getPassword());
            if (Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password)) {
                return u.getId();
            }
        }
        return -1;
    }

    public long findUserByUsername(String str) {
        Iterable<User> users = usersRepo.findAll();
        for (User u : users) {
            if (Objects.equals(u.getUsername(), str)) {
                return u.getId();
            }
        }
        return -1;
    }

    public long findAvailableId() {
        long id = sizeOfUsers() + 1;
        while (findOne(id) != null) {
            id++;
        }
        return id;
    }

    // -------------FRIEND REQUEST-------------
    public void updateFriendRequest(long id1, long id2, Status status) {
        Iterable<Friendship> friendships = friendsRepo.findAll();
        for (Friendship f : friendships) {
            if (f.getUser1() == id1 && f.getUser2() == id2) {
                Friendship updatedFriendship = new Friendship(id1, id2, status);
                friendsRepo.update(updatedFriendship);
                notifyObservers(new UserChangedEvent(ChangeEventType.UPDATE, null)); //// TODO
            }
        }
    }


    // -------------FRIENDSHIP-------------
    /**
     * Adds a friendship to the repository
     *
     * @param id1 - the id of the first user
     * @param id2 - the id of the second user
     * null, otherwise
     */
    public void addFriendship(long id1, long id2) {
        Friendship entity = new Friendship(id1, id2, Status.PENDING);
        System.out.println(entity);

        friendshipValidator.validate(entity);

        // add the user2 to the friend list of user1
        User u1 = findOne(id1);
        ArrayList<User> arr1 = usersRepo.getFriendsOfUser2(id1);
        arr1.add(findOne(id2));
        u1.setFriends(arr1);

        // add the user1 to the friend list of user2
        User u2 = findOne(id2);
        ArrayList<User> arr2 = usersRepo.getFriendsOfUser2(id2);
        arr2.add(findOne(id1));
        u2.setFriends(arr2);

        if (friendsRepo.save(entity).isPresent()) {
            throw new RuntimeException("ID of user already exists!"); //?????
        }
        notifyObservers(new UserChangedEvent(ChangeEventType.ADD, null));
    }

    /**
     * Deletes a friendship between a user with id1 and a user with id2 from the repository
     *
     * @param id1 - the id of the first user
     * @param id2 - the id of the second user
     */
    public void deleteFriendship(long id1, long id2) {
        // a shallow copy of the list of friendships
        List<Friendship> friendships = new ArrayList<>((Collection) friendsRepo.findAll());

//        for (Friendship f : friendships) {
//            if ((f.getUser1().getId() == id1 && f.getUser2().getId() == id2) ||
//                    (f.getUser1().getId() == id2 && f.getUser2().getId() == id1)) {
//                friendsRepo.delete(f.getId());
//                System.out.println("Friendship deleted!");
//            }
//        }
        friendships.stream()
                .filter(f -> (f.getUser1() == id1 && f.getUser2() == id2) ||
                        (f.getUser1() == id2 && f.getUser2() == id1))
                .forEach(f -> { friendsRepo.delete(f.getId());
                    System.out.println("Friendship deleted!");
                });

        User u1 = findOne(id1);
        //System.out.println(u1.getFirstName());
        u1.setFriends(deleteFriendFromFriendList(u1.getFriends(), id2));

        User u2 = findOne(id2);
        //System.out.println(u2.getFirstName());
        u2.setFriends(deleteFriendFromFriendList(u2.getFriends(), id1));

    }

    /**
     * Deletes a friend from the friend list of a user
     *
     * @param listOfFriends - the list of friends of a user
     * @param id            - the id of the friend to be deleted
     * @return the list of friends without the friend with the given id
     */
    private ArrayList<User> deleteFriendFromFriendList(ArrayList<User> listOfFriends, long id) {
        ArrayList<User> newList = new ArrayList<>();
//        for (User u : listOfFriends) {
//            if (u.getId() != id) {
//                newList.add(u);
//            }
//        }
//        return newList;

        listOfFriends.stream()
                .filter(u -> u.getId() != id)
                .forEach(newList::add);
        return newList;
    }

    /**
     * @return the number of friendships
     */
    public int sizeOfFriends() {
        return friendsRepo.size();
    }

    /**
     * @return all the friendships
     */
    public Iterable<Friendship> findAllFriendships() {
        return friendsRepo.findAll();
    }


    public ArrayList <DTO_FriendsOfUser> getFriendsOfUserFromDate(long id, int mouth) {
        ArrayList <DTO_FriendsOfUser> friends = new ArrayList<>();
        usersRepo.getFriendsOfUser(id).forEach(
                f -> {
                    if (f.getMouth() == mouth) {
                        friends.add(f);
                    }
                }
        );
        return friends;
    }

    public ArrayList<User> getFriendsOfUser(long id) {
        return usersRepo.getFriendsOfUserWithStatus(id, Status.ACCEPTED);
    }

    public ArrayList<User> getUsersForReceived(long id) {
        return usersRepo.getUsersForReceived(id);
    }

    public ArrayList<User> getUsersForSent(long id) {
        return usersRepo.getUsersForSent(id);
    }



    // COMUNITIES

    /**
     * @return the number of comunities
     */
    public int numberOfComunities() {
        Iterable<User> users = findAllUsers();
        Set<User> beenThere = new HashSet<>();
        int count = 0;
        for (User u : users) {
            if (!beenThere.contains(u)) { // if is not visited
                count++; // increment the count (number of comunities)
                dfs(u, beenThere, count); // start a dfs
            }
        }
        return count;
    }

    /**
     * Depth First Search
     *
     * @param u         - the user
     * @param beenThere - the set of visited users
     * @param count     - the current number of comunities
     */
    private void dfs(User u, Set<User> beenThere, int count) {
        comunityOfUser.put(u, count);
        beenThere.add(u);
        for (User v : u.getFriends()) {
            if (!beenThere.contains(v)) {
                dfs(v, beenThere, count);
            }
        }
    }

    /**
     * @return a list of lists of users, where each list of users is a comunity
     */
    public List<List<User>> mostSociableComunities() {
        List<List<User>> listOfMostSociableComunities = new ArrayList<>(); // list of lists of users
        List<Integer> listOfNumbers = new ArrayList<>(); // list of numbers of most sociable comunities
        Collection<Integer> numbers = comunityOfUser.values();
        int max = 0;
        for (int i = 1; i <= numberOfComunities(); i++) {
            int occurencies = Collections.frequency(numbers, i);
            if (occurencies > max) {
                max = occurencies;
                listOfNumbers = new ArrayList<>();
                listOfNumbers.add(i);
            }
            else if (occurencies == max) {
                listOfNumbers.add(i);
            }
        }

        for (int nr : listOfNumbers) {
            // find the users from the comunity with the number nr
            List<User> users = new ArrayList<>();
            for (Map.Entry<User, Integer> entry : comunityOfUser.entrySet()) {
                if (entry.getValue() == nr) {
                    users.add(entry.getKey());
                }
            }
            listOfMostSociableComunities.add(users);
        }

        return listOfMostSociableComunities;
    }

    // ------------- MESSAGES -------------
    public long findAvailableMessageId() {
        long id = messagesRepo.size() + 1;
        while (messagesRepo.findOne(id).isPresent()) {
            id++;
        }
        return id;
    }

    public void saveMessage(long from, long to, String text) {
        long id = findAvailableMessageId();
        Message m = null;
        try {
            Message message = new Message(id, from, to, text);
            m = messagesRepo.save(message).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (m != null) {
            throw new RuntimeException("Ivalid ID of message!");
        }
        notifyObservers(new UserChangedEvent(ChangeEventType.ADD, null));
    }

    public void deleteMessage(long id) {
        Message m = null;
        try {
            m = messagesRepo.delete(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (m == null) {
            throw new RuntimeException("There is no message with the given id!");
        }
        notifyObservers(new UserChangedEvent(ChangeEventType.DELETE, null));
    }


    public ArrayList<Message> getMessagesBetweenTwoUsers(long id1, long id2) {
        ArrayList<Message> messages = new ArrayList<>();
        messagesRepo.findAll().forEach(m -> {
            if ((m.getFrom() == id1 && m.getTo() == id2) || (m.getFrom() == id2 && m.getTo() == id1)) {
                messages.add(m);
            }
        });
        return messages;
    }

    // ------------- OBSERVER PATTERN -------------
    @Override
    public void addObserver(Observer<UserChangedEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangedEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserChangedEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }


    // ------------- PAGINATION -------------
    public void setPageSize(int size) {
        this.size = size;
    }


    public ArrayList<User> getNextPage(long id) {
        this.page++;
        return getUsersOnPage(id);
    }

    public ArrayList<User> getPreviousPage(long id) {
        if (this.page > 1) {
            this.page--;
        }
        return getUsersOnPage(id);
    }

    public ArrayList<User> getUsersOnPage(long id) {
        IPageable pageable = new Pageable(this.page, this.size);
        Page<User> page = usersRepo.getFriendsOfUserWithStatus(id, Status.ACCEPTED, pageable);
        return page.getContent().collect(Collectors.toCollection(ArrayList::new));
    }

    ////////////
    public void setPageSize2(int size) {
        this.sizeFR = size;
        this.sizeFR2 = size;
    }

    public ArrayList<User> getNextPageFR(long id) {
        this.pageFR++;
        return getUsersOnPageFR(id);
    }

    public ArrayList<User> getPreviousPageFR(long id) {
        if (this.pageFR > 1) {
            this.pageFR--;
        }
        return getUsersOnPageFR(id);
    }

    public ArrayList<User> getUsersOnPageFR(long id) {
        IPageable pageable = new Pageable(this.pageFR, this.sizeFR);
        Page<User> page = usersRepo.getUsersForReceived(id, pageable);
        return page.getContent().collect(Collectors.toCollection(ArrayList::new));
    }

    ////////////
    public ArrayList<User> getNextPageFR2(long id) {
        this.pageFR++;
        return getUsersOnPageFR2(id);
    }

    public ArrayList<User> getPreviousPageFR2(long id) {
        if (this.pageFR > 1) {
            this.pageFR--;
        }
        return getUsersOnPageFR2(id);
    }

    public ArrayList<User> getUsersOnPageFR2(long id) {
        IPageable pageable = new Pageable(this.pageFR, this.sizeFR);
        Page<User> page = usersRepo.getUsersForSent(id, pageable);
        return page.getContent().collect(Collectors.toCollection(ArrayList::new));
    }
}
