package org.test.bookpub.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.test.bookpub.entity.Author;

//http://localhost:8080/writers
@RepositoryRestResource(collectionResourceRel = "writers", path = "writers")
//@RepositoryRestResource
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
}
