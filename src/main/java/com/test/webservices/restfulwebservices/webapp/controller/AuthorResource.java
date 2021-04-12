package com.test.webservices.restfulwebservices.webapp.controller;

import com.test.webservices.restfulwebservices.webapp.config.InternationalizationLocale;
import com.test.webservices.restfulwebservices.webapp.dto.Author;
import com.test.webservices.restfulwebservices.webapp.dto.Course;
import com.test.webservices.restfulwebservices.webapp.dto.User;
import com.test.webservices.restfulwebservices.webapp.exception.AuthorNotFoundException;
import com.test.webservices.restfulwebservices.webapp.exception.UserNotFoundException;
import com.test.webservices.restfulwebservices.webapp.repository.AuthorRepository;
import com.test.webservices.restfulwebservices.webapp.repository.CourseRepository;
import com.test.webservices.restfulwebservices.webapp.repository.UserRepository;
import com.test.webservices.restfulwebservices.webapp.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthorResource {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InternationalizationLocale interLocale;

    @RequestMapping(method = RequestMethod.GET, path = "/authors")
    public List<Author> retrieveAllAuthors() {
        return authorRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/authors/{id}")
    public Author retrieveSpecificAuthorById(@PathVariable int id) {
        Optional<Author> author = authorRepository.findById(id);
        if (!author.isPresent()) {
            throw new AuthorNotFoundException("Author id is incorrect: " + id);
        }
        return (author.get()).add(linkTo(methodOn(getClass()).retrieveAllAuthors()).withRel("all-authors"));
    }

    @RequestMapping(method = RequestMethod.POST, path= "/authors")
    public ResponseEntity<Object> createAuthor(@Valid @RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAuthor.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/authors/{id}")
    public void deleteAuthor(@PathVariable int id) {
        authorRepository.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/authors/internationalized")
    public String retrieveAuthorInternationalized() {
        return interLocale.bundleMessageSource().getMessage("author.message", null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/authors/{id}/courses")
    public List<Course> retrieveCourses(@PathVariable int id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (!authorOptional.isPresent()) {
            throw new AuthorNotFoundException("Author id is incorrect: " + id);
        }
        return authorOptional.get().getCourses();
    }

    @RequestMapping(method = RequestMethod.POST, path= "/authors/{id}/courses")
    public ResponseEntity<Object> createCourse(@PathVariable int id, @RequestBody Course course) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (!authorOptional.isPresent()) {
            throw new AuthorNotFoundException("Author id is incorrect: " + id);
        }

        Author author = authorOptional.get();
        course.setAuthor(author);
        courseRepository.save(course);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(course.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
