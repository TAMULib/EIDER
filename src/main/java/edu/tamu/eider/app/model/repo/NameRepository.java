package edu.tamu.eider.app.model.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.eider.app.model.Name;

@RepositoryRestResource(collectionResourceRel = "name", path = "name")
public interface NameRepository extends PagingAndSortingRepository<Name, UUID> {

}