package com.astrapay.service;

import com.astrapay.dto.NotesDto;
import com.astrapay.entity.Notes;
import com.astrapay.exception.NotesNotFoundException;
import com.astrapay.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotesServiceTest {

    @InjectMocks
    private NotesService notesService;

    @Mock
    private NotesRepository notesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNote() {
        NotesDto noteDto = new NotesDto(1, "Test Note", "New Note");
        Notes note = new Notes(1, "Test Note", "New Note");

        when(notesRepository.save(any(Notes.class))).thenReturn(note);

        NotesDto result = notesService.addNote(noteDto);

        assertNotNull(result);
        assertEquals("New Note", result.getContent());
    }

    @Test
    void testGetAllNotes() {
        List<Notes> notesList = List.of(new Notes(1, "Note 1", "Content 1"),
                new Notes(2, "Note 2", "Content 2"));

        when(notesRepository.findAll()).thenReturn(notesList);

        List<NotesDto> notes = notesService.getAllNotes();
        assertEquals(2, notes.size());
    }

    @Test
    void testGetAllNotesEmpty() {
        when(notesRepository.findAll()).thenReturn(Collections.emptyList());

        List<NotesDto> notes = notesService.getAllNotes();
        assertTrue(notes.isEmpty());
    }


}
