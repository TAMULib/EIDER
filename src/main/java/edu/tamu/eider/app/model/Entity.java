package edu.tamu.eider.app.model;

import java.net.URL;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Entity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private URL url;

    @Size(max = 250)
    private String canonicalName;

    @Size(max = 250)
    private String notes;

}
