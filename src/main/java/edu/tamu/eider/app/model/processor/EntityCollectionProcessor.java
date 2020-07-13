package edu.tamu.eider.app.model.processor;

import static org.springframework.hateoas.Link.of;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.tamu.eider.app.model.Entity;

@Component
public class EntityCollectionProcessor implements RepresentationModelProcessor<CollectionModel<Entity>> {

    @Override
    public CollectionModel<Entity> process(CollectionModel<Entity> model) {
        if (model.getLinks().getLink("self").toString().contains("entity")) {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            model.add(
                of(baseUrl + "/entity/name?name={name}").withRel(LinkRelation.of("findByName"))
            );
            model.add(
                of(baseUrl + "/entity/url?url={url}").withRel(LinkRelation.of("findByUrl"))
            );
        }
        return model;
    }
}
