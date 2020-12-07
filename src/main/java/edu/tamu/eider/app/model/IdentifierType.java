package edu.tamu.eider.app.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.tamu.eider.app.model.annotation.ValidUri;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_EMPTY)
@Table(name = "identifier_types")
public class IdentifierType {

    @Id
    @GeneratedValue
    private UUID id;

    @ValidUri
    @Size(max = 512)
    @Column(length = 512, unique = true, nullable = false)
    private String namespace;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String name;

}
