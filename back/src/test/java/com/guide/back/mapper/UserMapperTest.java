package com.guide.back.mapper;

import com.guide.back.domain.User;
import com.guide.back.dto.UserRequestDTO;
import com.guide.back.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utilise un vrai BCryptPasswordEncoder (pas de mock) : ce test vérifie
 * un comportement de sécurité réel, pas juste "la méthode a été appelée".
 */
class UserMapperTest {

    private final UserMapper mapper = new UserMapper(new BCryptPasswordEncoder());

    @Test
    void toEntity_neDoitJamaisStockerLeMotDePasseEnClair() {
        UserRequestDTO dto = new UserRequestDTO("Dupont", "Marie", "marie@mail.com", "motdepasse123");

        User user = mapper.toEntity(dto);

        assertThat(user.getMotDePasse()).isNotEqualTo("motdepasse123");
        assertThat(user.getMotDePasse()).startsWith("$2a$"); // préfixe standard BCrypt
    }

    @Test
    void toEntity_leHashDoitCorrespondreAuMotDePasseOriginal() {
        UserRequestDTO dto = new UserRequestDTO("Dupont", "Marie", "marie@mail.com", "motdepasse123");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = mapper.toEntity(dto);

        assertThat(encoder.matches("motdepasse123", user.getMotDePasse())).isTrue();
        assertThat(encoder.matches("mauvais-mot-de-passe", user.getMotDePasse())).isFalse();
    }

    @Test
    void toDto_neDoitJamaisExposerLeMotDePasse() {
        User user = new User("Dupont", "Marie", "marie@mail.com", "$2a$10$hashSimule");
        user.setId(1L);

        UserResponseDTO dto = mapper.toDto(user);

        // UserResponseDTO n'a même pas de champ motDePasse : la garantie
        // est structurelle, pas seulement testée au runtime.
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.email()).isEqualTo("marie@mail.com");
    }
}
