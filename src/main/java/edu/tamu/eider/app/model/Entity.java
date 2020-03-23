package edu.tamu.eider.app.model;

import java.net.URL;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@javax.persistence.Entity
public class Entity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private URL url;
    
    private String canonicalName;
    private String notes;

}