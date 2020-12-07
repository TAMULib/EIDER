package edu.tamu.eider.app.model.handler;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.IdentifierType;
import edu.tamu.eider.app.model.repo.IdentifierTypeRepository;

@RepositoryEventHandler(Identifier.class)
public class IdentifierEventHandler {

    @Value("${app.default-identifier-type-name:Unspecified}")
    private String defaultIdentifierTypeName;

    @Autowired
    private IdentifierTypeRepository identifierTypeRepo;

    private IdentifierType defaultIdentifierType;

    @PostConstruct
    public void setup() {
        Optional<IdentifierType> identifierType = identifierTypeRepo.findByNamespace(StringUtils.EMPTY);
        if (identifierType.isPresent()) {
            defaultIdentifierType = identifierType.get();
        } else {
            defaultIdentifierType = new IdentifierType();
            defaultIdentifierType.setName(defaultIdentifierTypeName);
            defaultIdentifierType.setNamespace(StringUtils.EMPTY);
            defaultIdentifierType = identifierTypeRepo.save(defaultIdentifierType);
        }
    }

    @HandleBeforeCreate
    public void handleBeforeIdentifierCreate(Identifier identifier) {
        if (Objects.isNull(identifier.getIdentifierType())) {
            identifier.setIdentifierType(defaultIdentifierType);
        }
    }

    @HandleBeforeSave
    public void handleBeforeIdentifierSave(Identifier identifier) {
        if (Objects.isNull(identifier.getIdentifierType())) {
            identifier.setIdentifierType(defaultIdentifierType);
        }
    }

}
