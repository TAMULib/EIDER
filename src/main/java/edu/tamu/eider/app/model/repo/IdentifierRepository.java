package edu.tamu.eider.app.model.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.PathVariable;

import edu.tamu.eider.app.model.Identifier;

@RepositoryRestResource(collectionResourceRel = "identifier", path = "identifier")
public interface IdentifierRepository extends PagingAndSortingRepository<Identifier, UUID> {

    @RestResource(exported = false)
    public Optional<Identifier> findByIdentifier(@PathVariable("identifier") String identifier);
}