package com.astrapay.repository;

import com.astrapay.entity.Notes;

import java.util.List;
import java.util.Optional;

public interface NotesRepository {
    List<Notes> findAll();
    Optional<Notes> findById(int id);
    Notes save(Notes note);
    void deleteById(int id);
}
