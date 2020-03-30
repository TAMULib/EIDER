package edu.tamu.eider.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import edu.tamu.eider.ApiDocumentation;
import edu.tamu.eider.app.model.Entity;

public class EntityControllerTest extends ApiDocumentation {

    @Test
    public void testGetEntity() throws Exception {
        this.mockMvc.perform(get("/entity"))
            .andExpect(status().isOk())
            .andDo(document("sample"));
    }

    @Test
    public void testCreateEntity() throws Exception {
        Entity entity = new Entity();
        entity.setUrl(new URL("http://www.example.com"));
        entity.setCanonicalName("Example");
        entity.setNotes("Example notes");
        this.mockMvc.perform(
            post("/entity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(entity))
            )
            .andExpect(status().isCreated())
            .andDo(document("create-entity", requestFields(
                fieldWithPath("url").description("The URL of the Entity"),
                fieldWithPath("canonicalName").description("The canonical name for the Entity"),
                fieldWithPath("notes").description("Notes describing the Entity")
            )));
    }
}