package edu.tamu.eider.app.model;

import java.net.URL;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_NULL)
public class IdentifierType {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private URL namespace;

    @Size(max = 250)
    private String name;
}