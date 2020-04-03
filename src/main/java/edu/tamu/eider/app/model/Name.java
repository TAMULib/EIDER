package edu.tamu.eider.app.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_NULL)
public class Name {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Entity entity;

    @Size(max = 250)
    private String name;

    private Date startDate;
    private Date endDate;

    @Size(max = 250)
    private String notes;

    public Name(String name, String notes, Entity entity) {
        super();
        this.name = name;
        this.notes = notes;
        this.entity = entity;
    }
}
