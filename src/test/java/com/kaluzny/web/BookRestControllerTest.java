package com.kaluzny.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaluzny.domain.Book;
import com.kaluzny.domain.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void shouldCreateBook() throws Exception {
        Book book = Book.builder()
                .name("Java Programming")
                .description("Learn Java")
                .tags(List.of("java", "programming"))
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Java Programming"));
    }

    @Test
    @WithMockUser
    void shouldGetAllBooks() throws Exception {
        Book book = Book.builder()
                .name("Spring Boot")
                .description("Spring Boot Guide")
                .build();
        bookRepository.save(book);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Spring Boot"));
    }

    @Test
    @WithMockUser
    void shouldGetBookById() throws Exception {
        Book book = Book.builder()
                .name("Kotlin")
                .description("Kotlin Guide")
                .build();
        Book saved = bookRepository.save(book);

        mockMvc.perform(get("/api/books/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kotlin"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenBookNotFound() throws Exception {
        mockMvc.perform(get("/api/books/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldUpdateBook() throws Exception {
        Book book = Book.builder()
                .name("Old Name")
                .description("Old Description")
                .build();
        Book saved = bookRepository.save(book);

        Book updated = Book.builder()
                .name("New Name")
                .description("New Description")
                .tags(List.of("updated"))
                .build();

        mockMvc.perform(put("/api/books/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    @WithMockUser
    void shouldDeleteBook() throws Exception {
        Book book = Book.builder()
                .name("To Delete")
                .description("Will be deleted")
                .build();
        Book saved = bookRepository.save(book);

        mockMvc.perform(delete("/api/books/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnValidationErrorForEmptyName() throws Exception {
        Book book = Book.builder()
                .name("")
                .description("Description")
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }
}
