package com.astrapay.service;

import com.astrapay.dto.NotesDto;
import com.astrapay.entity.Notes;
import com.astrapay.repository.NotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotesService {
    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public List<NotesDto> getAllNotes() {
        return notesRepository.findAll().stream()
                .map(NotesDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotesDto addNote(NotesDto notesDto) {
        if (notesDto.getTitle() == null || notesDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        Notes newNote = new Notes(notesDto.getTitle(), notesDto.getContent());
        Notes savedNote = notesRepository.save(newNote);
        return NotesDto.fromEntity(savedNote);
    }

    @Transactional
    public void deleteNote(int id) {
        notesRepository.deleteById(id);
    }
}
