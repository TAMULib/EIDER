package edu.tamu.eider.app.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private Entity entity;

    private String name;
    private Date startDate;
    private Date endDate;
    private String notes;
}