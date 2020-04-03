package edu.tamu.eider.app.model.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.eider.app.model.IdentifierType;

@RepositoryRestResource(collectionResourceRel = "identifierType", path = "identifierType")
public interface IdentifierTypeRepository extends PagingAndSortingRepository<IdentifierType, UUID> {

}
