package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final ArrayList<User> userArrayList = new ArrayList<User>();

    private void bootstrapUsers() {
        User user1 = new User("max", "pass1");
        User user2 = new User("jeff", "pass2", true);
        userArrayList.add(user1);
        userArrayList.add(user2);
    }

    public UserManager() {
        bootstrapUsers();
    }

    /**
     * Adds a new user to the list of users.
     * @param username
     * @param password
     * @return true if user was added , false if the user wasnt added and false if the user already exists.
     */
    public boolean addUser(String username, String password) {
      User user = new User(username, password);
        if (userArrayList.contains(user)) {
            System.out.println("User Already Exists");
            return false;
        } else {
            boolean added = userArrayList.add(user);
            if (added) {
                System.out.println("User added successfully");
            } else {
                System.out.println("Failed to add user to the list");
            }
            return added;
        }
    }

    /**
     *method to find user by the userName
     * @param username
     * @return user found by user name or null if not found
     */
    public User searchByUsername(String username) {
        for (User u : userArrayList) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        System.out.println("user by that name doesnt exist");
        return null;
    }
}




