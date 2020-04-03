package edu.tamu.eider.app.model.repo;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import edu.tamu.eider.app.model.Entity;

@RepositoryRestResource(collectionResourceRel = "entity", path = "entity")
public interface EntityRepository extends PagingAndSortingRepository<Entity, UUID> {

    @RestResource(exported = false)
    public Optional<Entity> findByUrl(URL url);

    @RestResource(exported = false)
    public boolean existsByUrl(URL url);

}
