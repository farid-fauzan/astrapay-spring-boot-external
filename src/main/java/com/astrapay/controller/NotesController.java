package com.astrapay.controller;

import com.astrapay.dto.ApiResponseDto;
import com.astrapay.dto.NotesDto;
import com.astrapay.exception.NotesNotFoundException;
import com.astrapay.service.NotesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
@Validated
@Api(value = "NotesController", tags = "Notes Management")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping
    @ApiOperation(value = "Get all notes", response = ApiResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseDto<List<NotesDto>>> getAllNotes() {
        log.info("Fetching all notes...");
        List<NotesDto> notes = notesService.getAllNotes();
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Notes retrieved successfully", notes));
    }

    @PostMapping
    @ApiOperation(value = "Create a new note", response = ApiResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Note created successfully"),
            @ApiResponse(code = 400, message = "Invalid request data")
    })
    public ResponseEntity<ApiResponseDto<NotesDto>> createNote(@Valid @RequestBody NotesDto noteDto) {
        log.info("Creating note: {}", noteDto);
        NotesDto createdNote = notesService.addNote(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(true, "Note created successfully", createdNote));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a note by ID", response = ApiResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Note deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid note ID"),
            @ApiResponse(code = 404, message = "Note not found")
    })
    public ResponseEntity<ApiResponseDto<String>> deleteNote(@PathVariable(required = false) Integer id) {
        if (id == null || id <= 0) {
            log.warn("Invalid note ID provided: {}", id);
            throw new IllegalArgumentException("Note ID must be a positive integer.");
        }

        log.info("Deleting note with ID: {}", id);
        notesService.deleteNote(id);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Note deleted successfully", "ID: " + id));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation Error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(false, "Validation Error", errorMessage));
    }

    @ExceptionHandler(NotesNotFoundException.class)
    public ResponseEntity<ApiResponseDto<String>> handleNotesNotFoundException(NotesNotFoundException ex) {
        log.warn("NotesNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(false, "Note not found", ex.getMessage()));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponseDto<String>> handleMissingPathVariable(MissingPathVariableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(false, "Missing required path variable", ex.getMessage()));
    }
}
