package edu.tamu.eider.app.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_NULL)
@NamedEntityGraph(
  name = "graph.Name",
  attributeNodes = {
    @NamedAttributeNode(value = "entity")
  }
)
public class Name {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Entity entity;

    @Size(max = 250)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @Size(max = 250)
    private String notes;

    public Name(String name, String notes, Entity entity) {
        super();
        this.name = name;
        this.notes = notes;
        this.entity = entity;
    }

}
