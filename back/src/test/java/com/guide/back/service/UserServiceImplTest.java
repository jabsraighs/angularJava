package com.guide.back.service;

import com.guide.back.domain.User;
import com.guide.back.dto.UserRequestDTO;
import com.guide.back.dto.UserResponseDTO;
import com.guide.back.exception.EmailAlreadyExistsException;
import com.guide.back.exception.ResourceNotFoundException;
import com.guide.back.mapper.UserMapper;
import com.guide.back.repository.UserRepository;
import com.guide.back.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - tests unitaires")
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new User("Dupont", "Marie", "marie.dupont@mail.com", "$2a$10$hashSimule");
        user.setId(1L);
        requestDTO = new UserRequestDTO("Dupont", "Marie", "marie.dupont@mail.com", "motdepasse123");
        responseDTO = new UserResponseDTO(1L, "Dupont", "Marie", "marie.dupont@mail.com");
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("doit créer l'utilisateur si l'email est disponible")
        void shouldCreateUserWhenEmailIsAvailable() {
            when(repository.existsByEmail("marie.dupont@mail.com")).thenReturn(false);
            when(mapper.toEntity(requestDTO)).thenReturn(user);
            when(repository.save(user)).thenReturn(user);
            when(mapper.toDto(user)).thenReturn(responseDTO);

            UserResponseDTO result = service.create(requestDTO);

            assertThat(result).isEqualTo(responseDTO);
            verify(repository, times(1)).save(user);
        }

        @Test
        @DisplayName("doit lever EmailAlreadyExistsException si l'email est déjà pris")
        void shouldThrowWhenEmailAlreadyExists() {
            when(repository.existsByEmail("marie.dupont@mail.com")).thenReturn(true);

            assertThatThrownBy(() -> service.create(requestDTO))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessageContaining("marie.dupont@mail.com");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("doit retourner la liste mappée de tous les utilisateurs")
        void shouldReturnAllUsers() {
            when(repository.findAll()).thenReturn(List.of(user));
            when(mapper.toDto(user)).thenReturn(responseDTO);

            List<UserResponseDTO> result = service.findAll();

            assertThat(result).hasSize(1).containsExactly(responseDTO);
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("doit retourner l'utilisateur s'il existe")
        void shouldReturnUserWhenFound() {
            when(repository.findById(1L)).thenReturn(Optional.of(user));
            when(mapper.toDto(user)).thenReturn(responseDTO);

            UserResponseDTO result = service.findById(1L);

            assertThat(result).isEqualTo(responseDTO);
        }

        @Test
        @DisplayName("doit lever ResourceNotFoundException si absent")
        void shouldThrowWhenNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("doit déléguer la mise à jour au mapper sur l'entité gérée")
        void shouldUpdateManagedEntity() {
            when(repository.findById(1L)).thenReturn(Optional.of(user));
            doNothing().when(mapper).updateEntity(user, requestDTO);
            when(mapper.toDto(user)).thenReturn(responseDTO);

            UserResponseDTO result = service.update(1L, requestDTO);

            assertThat(result).isEqualTo(responseDTO);
            verify(mapper, times(1)).updateEntity(user, requestDTO);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("doit lever ResourceNotFoundException si l'utilisateur n'existe pas")
        void shouldThrowWhenUpdatingNonExistent() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(99L, requestDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("doit supprimer si l'utilisateur existe")
        void shouldDeleteWhenExists() {
            when(repository.existsById(1L)).thenReturn(true);

            service.delete(1L);

            verify(repository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("doit lever ResourceNotFoundException sans appeler deleteById si absent")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(99L))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(repository, never()).deleteById(any());
        }
    }
}
