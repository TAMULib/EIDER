package edu.tamu.eider.app.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
