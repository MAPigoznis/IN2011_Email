package proto.server;

import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User("johndoe@gmail.com", "password123"));
        users.add(new User("janedoe@yahoo.com", "password456"));
        users.add(new User("admin@example.com", "password789"));
        users.add(new User("a", "a"));
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void removeUser(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                users.remove(user);
                break;
            }
        }
    }
}
