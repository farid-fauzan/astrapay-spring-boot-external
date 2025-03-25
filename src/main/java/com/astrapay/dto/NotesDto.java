package com.astrapay.dto;

import com.astrapay.entity.Notes;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto {
    private int id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    public static NotesDto fromEntity(Notes note) {
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null");
        }
        if (note.getTitle() == null || note.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (note.getContent() == null || note.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        return new NotesDto(note.getId(), note.getTitle(), note.getContent());
    }



}
