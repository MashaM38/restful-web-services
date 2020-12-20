package com.test.webservices.restfulwebservices.webapp.controller;

import com.test.webservices.restfulwebservices.webapp.config.InternationalizationLocale;
import com.test.webservices.restfulwebservices.webapp.exception.UserNotFoundException;
import com.test.webservices.restfulwebservices.webapp.dto.User;
import com.test.webservices.restfulwebservices.webapp.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService userService;

    @Autowired
    private InternationalizationLocale interLocale;

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public List<User> retrieveAllUsers() {
        return userService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
    public User retrieveSpecificUserById(@PathVariable int id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User id is incorrect: " + id);
        }
        user.add(linkTo(methodOn(getClass()).retrieveAllUsers()).withRel("all-users"));

        return user;
    }

    @RequestMapping(method = RequestMethod.POST, path= "/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = userService.deleteById(id);
        if (user == null) {
            throw new UserNotFoundException("User id is incorrect: " + id);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/users/internationalized")
    public String retrieveUserInternationalized() {
        return interLocale.bundleMessageSource().getMessage("user.message", null, LocaleContextHolder.getLocale());
    }
}
