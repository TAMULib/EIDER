package edu.tamu.eider.app.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@javax.persistence.Entity
public class Identifier {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Entity entity;

    @Column(unique = true)
    private String identifier;

    private Date startDate;
    private Date endDate;
    private String notes;

    @ManyToOne
    private IdentifierType identifierType;

}