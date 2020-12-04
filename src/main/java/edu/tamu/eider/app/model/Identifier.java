package edu.tamu.eider.app.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_NULL)
// @formatter:off
@NamedEntityGraph(
  name = "graph.Identifier",
  attributeNodes = {
    @NamedAttributeNode(value = "entity"),
    @NamedAttributeNode(value = "identifierType")
  }
)
@Table(
  name = "identifiers",
  indexes = {
    @Index(columnList = "identifier")
  }
)
// @formatter:on
public class Identifier {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Entity entity;

    @NotNull
    @Size(min = 4, max = 64)
    @Column(unique = true, nullable = false)
    private String identifier;

    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 1024)
    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(optional = false)
    private IdentifierType identifierType;

}
