package com.test.webservices.restfulwebservices.webapp.service;

import com.test.webservices.restfulwebservices.webapp.dto.User;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User(1, "Adam", "Raible", "adam75@gmail.com"));
        users.add(new User(2, "Victor", "Garcia", "victor83@gmail.com"));
        users.add(new User(3, "Daniel", "Chapman", "dan85@gmail.com"));
    }

    public List<User> findAll() {
        return users;
    }

    public User findOne(int id) {
        for (User user: users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public User deleteById(int id) {
        Iterator<User> usersIterator  = users.iterator();
        while (usersIterator.hasNext()) {
            User user = usersIterator.next();
            if (user.getId() == id) {
                usersIterator.remove();
                return  user;
            }
        }
        return null;
    }
}
