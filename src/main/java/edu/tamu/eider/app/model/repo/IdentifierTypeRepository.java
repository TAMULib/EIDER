package edu.tamu.eider.app.model.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import edu.tamu.eider.app.model.IdentifierType;

@RepositoryRestResource(collectionResourceRel = "identifierType", path = "identifierType")
public interface IdentifierTypeRepository extends PagingAndSortingRepository<IdentifierType, UUID> {

    @RestResource(exported = false)
    Optional<IdentifierType> findByNamespace(String namespace);

}
