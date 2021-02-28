package com.test.webservices.restfulwebservices.webapp.service;

import com.test.webservices.restfulwebservices.webapp.dto.Author;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class AuthorDaoService {

    private static List<Author> authors = new ArrayList<>();

    private static int usersCount = 3;

    static {
        authors.add(new Author(1, "Adam", "Raible", "adam75@gmail.com"));
        authors.add(new Author(2, "Victor", "Garcia", "victor83@gmail.com"));
        authors.add(new Author(3, "Daniel", "Chapman", "dan85@gmail.com"));
    }

    public List<Author> findAll() {
        return authors;
    }

    public Author findOne(int id) {
        for (Author author : authors) {
            if (author.getId() == id) {
                return author;
            }
        }
        return null;
    }

    public Author save(Author author) {
        if (author.getId() == null) {
            author.setId(++usersCount);
        }
        authors.add(author);
        return author;
    }

    public Author deleteById(int id) {
        Iterator<Author> usersIterator  = authors.iterator();
        while (usersIterator.hasNext()) {
            Author author = usersIterator.next();
            if (author.getId() == id) {
                usersIterator.remove();
                return author;
            }
        }
        return null;
    }
}
