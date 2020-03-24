package edu.tamu.eider.app.model.processor;

import static org.springframework.hateoas.Link.of;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.tamu.eider.app.model.Entity;

public class EntityProcessor implements RepresentationModelProcessor<EntityModel<Entity>> {

    @Override
    public EntityModel<Entity> process(EntityModel<Entity> model) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        model.add(
            of(baseUrl + "entity/{id}/redirect").withRel(LinkRelation.of("entity"))
                .expand(model.getContent().getId())
        );
        return model;
    }
}