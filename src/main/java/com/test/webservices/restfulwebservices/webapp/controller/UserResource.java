package com.test.webservices.restfulwebservices.webapp.controller;

import com.test.webservices.restfulwebservices.webapp.config.InternationalizationLocale;
import com.test.webservices.restfulwebservices.webapp.dto.Author;
import com.test.webservices.restfulwebservices.webapp.dto.Course;
import com.test.webservices.restfulwebservices.webapp.exception.AuthorNotFoundException;
import com.test.webservices.restfulwebservices.webapp.exception.UserNotFoundException;
import com.test.webservices.restfulwebservices.webapp.dto.User;
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
public class UserResource {

    @Autowired
    private UserDaoService userService; //user service with static fields

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InternationalizationLocale interLocale;

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
    public User retrieveSpecificUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User ID is incorrect: " + id);
        }
        return (user.get()).add(linkTo(methodOn(getClass()).retrieveAllUsers()).withRel("all-users"));
    }

    @RequestMapping(method = RequestMethod.POST, path= "/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/internationalized")
    public String retrieveUserInternationalized() {
        return interLocale.bundleMessageSource().getMessage("user.message", null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/users/{id}/courses")
    public List<Course> retrieveCourses(@PathVariable int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User ID is incorrect: " + id);
        }
        return userOptional.get().getCourses();
    }

    @RequestMapping(method = RequestMethod.POST, path= "/users/{id}/courses")
    public ResponseEntity<Object> createCourse(@PathVariable int id, @RequestBody Course course) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User ID is incorrect: " + id);
        }

        User user = userOptional.get();
        course.setUser(user);
        courseRepository.save(course);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(course.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }
}
