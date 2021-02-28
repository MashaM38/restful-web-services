package com.test.webservices.restfulwebservices.webapp.repository;

import com.test.webservices.restfulwebservices.webapp.dto.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
