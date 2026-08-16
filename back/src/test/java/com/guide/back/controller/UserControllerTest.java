package com.guide.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guide.back.dto.UserRequestDTO;
import com.guide.back.dto.UserResponseDTO;
import com.guide.back.exception.EmailAlreadyExistsException;
import com.guide.back.exception.ResourceNotFoundException;
import com.guide.back.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @Test
    void create_shouldReturn201_whenPayloadIsValid() throws Exception {
        UserRequestDTO request = new UserRequestDTO("Dupont", "Marie", "marie@mail.com", "motdepasse123");
        UserResponseDTO response = new UserResponseDTO(1L, "Dupont", "Marie", "marie@mail.com");
        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("marie@mail.com"))
                .andExpect(jsonPath("$.motDePasse").doesNotExist());
    }

    @Test
    void create_shouldReturn400_whenEmailIsInvalid() throws Exception {
        UserRequestDTO invalid = new UserRequestDTO("Dupont", "Marie", "pas-un-email", "motdepasse123");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void create_shouldReturn400_whenPasswordTooShort() throws Exception {
        UserRequestDTO invalid = new UserRequestDTO("Dupont", "Marie", "marie@mail.com", "123");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.motDePasse").exists());
    }

    @Test
    void create_shouldReturn409_whenEmailAlreadyExists() throws Exception {
        UserRequestDTO request = new UserRequestDTO("Dupont", "Marie", "marie@mail.com", "motdepasse123");
        when(service.create(any()))
                .thenThrow(new EmailAlreadyExistsException("Un compte existe déjà avec l'email marie@mail.com"));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void findAll_shouldReturnListOfUsers() throws Exception {
        when(service.findAll()).thenReturn(List.of(new UserResponseDTO(1L, "Dupont", "Marie", "marie@mail.com")));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("marie@mail.com"));
    }

    @Test
    void findById_shouldReturn404_whenUserNotFound() throws Exception {
        when(service.findById(99L))
                .thenThrow(new ResourceNotFoundException("Utilisateur introuvable avec l'id 99"));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204_whenSuccessful() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
