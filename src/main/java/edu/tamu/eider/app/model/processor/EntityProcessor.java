package edu.tamu.eider.app.model.processor;

import static org.springframework.hateoas.Link.of;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.tamu.eider.app.model.Entity;

@Component
public class EntityProcessor implements RepresentationModelProcessor<EntityModel<Entity>> {

    @Override
    public EntityModel<Entity> process(EntityModel<Entity> model) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        model.add(
            of(baseUrl + "entity/{id}/redirect")
                .withRel(LinkRelation.of("redirect"))
                .expand(model.getContent().getId())
        );
        model.add(
            of(baseUrl + "/entity/{uuid}")
                .withRel(LinkRelation.of("redirectByHead"))
                .expand(model.getContent().getId())
                .withType("HEAD")
        );
        return model;
    }
}
