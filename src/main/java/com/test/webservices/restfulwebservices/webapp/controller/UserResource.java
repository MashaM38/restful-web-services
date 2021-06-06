package com.test.webservices.restfulwebservices.webapp.controller;

import com.test.webservices.restfulwebservices.webapp.config.InternationalizationLocale;
import com.test.webservices.restfulwebservices.webapp.dto.Course;
import com.test.webservices.restfulwebservices.webapp.dto.UserCourse;
import com.test.webservices.restfulwebservices.webapp.dto.UserCourseId;
import com.test.webservices.restfulwebservices.webapp.exception.UserNotFoundException;
import com.test.webservices.restfulwebservices.webapp.exception.СourseNotFoundException;
import com.test.webservices.restfulwebservices.webapp.dto.User;
import com.test.webservices.restfulwebservices.webapp.repository.CourseRepository;
import com.test.webservices.restfulwebservices.webapp.repository.UserCourseRepository;
import com.test.webservices.restfulwebservices.webapp.repository.UserRepository;
import com.test.webservices.restfulwebservices.webapp.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
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
    private UserCourseRepository userCourseRepository;

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

    @RequestMapping(method = {RequestMethod.PUT}, path = "/users")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User user) {
        User updatedUser = userRepository.save(user);
        return new ResponseEntity<Object>(updatedUser, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/internationalized")
    public String retrieveUserInternationalized() {
        return interLocale.bundleMessageSource().getMessage("user.message", null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/users/{id}/courses")
    public List<Course> retrieveCourses(@PathVariable int id) {
        List<Course> courses = new ArrayList<>();
        List<UserCourse> userCourseOptional = userCourseRepository.findByUserCourseId_UserId(id);
        for (UserCourse c: userCourseOptional) {
            Optional<Course> courseOptional = courseRepository.findById(c.getUserCourseId().getCourseId());
            if (!courseOptional.isPresent()) {
                throw new СourseNotFoundException("Course is not found for User ID: " + id);
            }
            courses.add(courseOptional.get());
        }
        return courses;
    }

    @GetMapping("/users/{user_id}/courses/{course_id}")
    public UserCourseId retrieveCourse(@PathVariable int user_id, @PathVariable int course_id) {
        List<UserCourse> userCourseOptional = userCourseRepository.findByUserCourseId_UserId(user_id);
        for (UserCourse c: userCourseOptional) {
            if (c.getUserCourseId().getCourseId() == course_id) {
                return c.getUserCourseId();
            }
        }
        return null;
    }

    @PostMapping("/users/{id}/courses")
    public ResponseEntity<Object> createCourseForUser(@PathVariable int id, @RequestBody UserCourseId userCourseId) {
        UserCourseId userCourseIdObj = new UserCourseId(id, userCourseId.getCourseId());
        Optional<Course> courseOptional = courseRepository.findById(userCourseIdObj.getCourseId());
        if (!courseOptional.isPresent()) {
            throw new СourseNotFoundException("Course ID is not found: " + userCourseIdObj.getCourseId());
        }
        UserCourse savedUserCourse = userCourseRepository.save(new UserCourse(userCourseIdObj));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUserCourse.getUserCourseId().getCourseId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}/courses")
    public ResponseEntity<Void> deleteUserCourse(@PathVariable int id, @RequestBody UserCourseId userCourseId) {
        UserCourseId userCourseIdObj = new UserCourseId(id, userCourseId.getCourseId());
        userCourseRepository.deleteById(userCourseIdObj);
        if (userCourseIdObj.getCourseId() != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }
}
