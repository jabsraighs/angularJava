package com.guide.back.mapper;

import com.guide.back.domain.User;
import com.guide.back.dto.UserRequestDTO;
import com.guide.back.dto.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserRequestDTO dto) {
        return new User(dto.nom(), dto.prenom(), dto.email(), passwordEncoder.encode(dto.motDePasse()));
    }

    /**
     * Met à jour une entité déjà chargée (managée par JPA) plutôt que
     * d'en créer une nouvelle : le dirty checking de Hibernate génère
     * l'UPDATE tout seul, sans save() explicite (voir UserServiceImpl).
     */
    public void updateEntity(User user, UserRequestDTO dto) {
        user.setNom(dto.nom());
        user.setPrenom(dto.prenom());
        user.setEmail(dto.email());
        user.setMotDePasse(passwordEncoder.encode(dto.motDePasse()));
    }

    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getNom(), user.getPrenom(), user.getEmail());
    }
}