package com.astrapay.repository;

import com.astrapay.entity.Notes;
import com.astrapay.exception.NotesNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryNotesRepository implements NotesRepository {
    private final List<Notes> notesList = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public List<Notes> findAll() {
        return new ArrayList<>(notesList);
    }

    @Override
    public Optional<Notes> findById(int id) {
        return notesList.stream()
                .filter(note -> note.getId() == id)
                .findFirst();
    }

    @Override
    public Notes save(Notes note) {
        if (note.getId() == 0) {
            note.setId(idCounter.getAndIncrement()); // Auto-generate ID jika 0
        }
        notesList.add(note);
        return note;
    }

    @Override
    public void deleteById(int id) {
        boolean removed = notesList.removeIf(note -> note.getId() == id);
        if (!removed) {
            throw new NotesNotFoundException("Note with ID " + id + " not found.");
        }
    }
}
