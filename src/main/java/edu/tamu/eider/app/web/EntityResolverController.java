package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;

@RepositoryRestController
public class EntityResolverController {

  @Autowired
  private EntityRepository entityRepo;

  @RequestMapping(method = POST, path = "/entity", produces = MediaType.TEXT_PLAIN_VALUE)
  public @ResponseBody ResponseEntity<String> createWithUrl(@RequestParam(required = true) String url) throws URISyntaxException {
      Optional<Entity> existingEntity = entityRepo.findByUrl(url);
      if (existingEntity.isPresent()) {
        return ResponseEntity.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .body(existingEntity.get().getId().toString());
      }
      Entity entity = new Entity();
      entity.setUrl(url);
      entity = entityRepo.save(entity);
      return ResponseEntity.created(new URI(url))
        .contentType(MediaType.TEXT_PLAIN)
        .body(entity.getId().toString());
  }

  @RequestMapping(method = GET, path = "/entity/{uuid}", produces = MediaType.TEXT_PLAIN_VALUE)
  public @ResponseBody ResponseEntity<String> resolveId(@PathVariable(required = true) String uuid) {
      return ResponseEntity.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .body(entityRepo.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Entity with id {} not found", uuid)))
                .getUrl().toString());
  }

  @RequestMapping(method = GET, path = "/entity", produces = MediaType.TEXT_PLAIN_VALUE)
  public @ResponseBody ResponseEntity<String> resolveUrl(@RequestParam(required = true) String url) {
      return ResponseEntity.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .body(entityRepo.findByUrl(url)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Entity with url {} not found", url)))
                .getId().toString());
  }

  @RequestMapping(method = DELETE, path = "/entity/{uuid}", produces = MediaType.TEXT_PLAIN_VALUE)
  public @ResponseBody ResponseEntity<Void> deleteById(@PathVariable(required = true) String uuid) {
      Entity entity = entityRepo.findById(UUID.fromString(uuid))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Entity with id {} not found", uuid)));
      entityRepo.delete(entity);
      return ResponseEntity.noContent().build();
  }

}
