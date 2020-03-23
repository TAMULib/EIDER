package edu.tamu.eider.app.model.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.eider.app.model.Entity;

@RepositoryRestResource(collectionResourceRel = "entity", path = "entity")
public interface EntityRepository extends PagingAndSortingRepository<Entity, UUID> {

    public Optional<Entity> findById(@Param("uuid") UUID uuid);
}