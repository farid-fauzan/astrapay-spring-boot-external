package com.astrapay.controller;

import com.astrapay.dto.ApiResponseDto;
import com.astrapay.dto.NotesDto;
import com.astrapay.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NotesController.class)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllNotes() throws Exception {
        List<NotesDto> notes = Collections.singletonList(new NotesDto(1, "Test Sample","Sample Note"));
        Mockito.when(notesService.getAllNotes()).thenReturn(notes);

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].content").value("Sample Note"));
    }

    @Test
    void testCreateNote() throws Exception {
        NotesDto requestDto = new NotesDto(1, "Test 1","New Note");
        NotesDto responseDto = new NotesDto(2, "Test 2","New Note");

        Mockito.when(notesService.addNote(any(NotesDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/notes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").value("New Note"));
    }

    @Test
    void testDeleteNoteSuccess() throws Exception {
        Mockito.doNothing().when(notesService).deleteNote(1);

        mockMvc.perform(delete("/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

}
