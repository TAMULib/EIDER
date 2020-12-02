package edu.tamu.eider.app.model.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import edu.tamu.eider.app.model.Name;

@RepositoryRestResource(collectionResourceRel = "name", path = "name")
public interface NameRepository extends PagingAndSortingRepository<Name, UUID> {

    @RestResource(exported = false)
    @EntityGraph(value = "graph.Name", type = EntityGraphType.LOAD)
    public List<Name> findByName(String name);

}
