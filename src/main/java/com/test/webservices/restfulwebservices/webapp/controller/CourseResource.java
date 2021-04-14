package com.test.webservices.restfulwebservices.webapp.controller;

import com.test.webservices.restfulwebservices.webapp.config.InternationalizationLocale;
import com.test.webservices.restfulwebservices.webapp.dto.Author;
import com.test.webservices.restfulwebservices.webapp.dto.Course;
import com.test.webservices.restfulwebservices.webapp.exception.СourseNotFoundException;
import com.test.webservices.restfulwebservices.webapp.repository.AuthorRepository;
import com.test.webservices.restfulwebservices.webapp.repository.CourseRepository;
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
public class CourseResource {

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/courses")
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/courses/{id}")
    public Course retrieveSpecificCourseById(@PathVariable int id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new СourseNotFoundException("Course ID is incorrect: " + id);
        }
        return (course.get()).add(linkTo(methodOn(getClass()).retrieveAllCourses()).withRel("all-courses"));
    }

    @RequestMapping(method = RequestMethod.POST, path= "/courses")
    public ResponseEntity<Object> createCourse(@Valid @RequestBody Course course) {
        Course savedCourse = courseRepository.save(course);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCourse.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/courses/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseRepository.deleteById(id);
    }

}
