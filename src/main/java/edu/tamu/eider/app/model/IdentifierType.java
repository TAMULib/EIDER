package edu.tamu.eider.app.model;

import java.net.URL;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "identifier_types")
public class IdentifierType {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private URL namespace;

    @NotNull
    @Size(min = 4, max = 128)
    @Column(nullable = false)
    private String name;

}
