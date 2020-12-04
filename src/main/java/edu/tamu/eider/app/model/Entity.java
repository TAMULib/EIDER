package edu.tamu.eider.app.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.tamu.eider.app.model.annotation.ValidHttpUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@javax.persistence.Entity
@JsonInclude(Include.NON_NULL)
// @formatter:off
@Table(
  name = "entities",
  indexes = {
    @Index(columnList = "url", unique = true)
  }
)
// @formatter:on
public class Entity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @ValidHttpUrl
    @Column(unique = true, nullable = false)
    private String url;

    @Size(min = 2, max = 255)
    private String canonicalName;

    @Size(max = 1024)
    @Column(columnDefinition = "TEXT")
    private String notes;

}
