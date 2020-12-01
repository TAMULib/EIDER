package edu.tamu.eider.app.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "identifiers", indexes = { @Index(columnList = "identifier") })
public class Identifier {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Entity entity;

    @Size(max = 250)
    @Column(unique = true)
    private String identifier;

    private LocalDate startDate;
    private LocalDate endDate;

    @Size(max = 250)
    private String notes;

    @ManyToOne
    private IdentifierType identifierType;

}
