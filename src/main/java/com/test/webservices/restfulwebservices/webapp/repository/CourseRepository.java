package com.test.webservices.restfulwebservices.webapp.repository;

import com.test.webservices.restfulwebservices.webapp.dto.Author;
import com.test.webservices.restfulwebservices.webapp.dto.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
