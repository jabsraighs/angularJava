package com.guide.back.repository;

import com.guide.back.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldPersistAndGenerateIdAndTimestamp() {
        User user = new User("Dupont", "Marie", "marie@mail.com", "$2a$10$hashSimule");

        User saved = repository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldFindAllPersistedUsers() {
        repository.save(new User("Dupont", "Marie", "marie@mail.com", "hash1"));
        repository.save(new User("Martin", "Paul", "paul@mail.com", "hash2"));

        List<User> all = repository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldReturnEmptyOptional_whenIdDoesNotExist() {
        Optional<User> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailIsUsed() {
        repository.save(new User("Dupont", "Marie", "marie@mail.com", "hash1"));

        assertThat(repository.existsByEmail("marie@mail.com")).isTrue();
        assertThat(repository.existsByEmail("inconnu@mail.com")).isFalse();
    }

    @Test
    void findByEmail_shouldReturnTheMatchingUser() {
        repository.save(new User("Dupont", "Marie", "marie@mail.com", "hash1"));

        Optional<User> result = repository.findByEmail("marie@mail.com");

        assertThat(result).isPresent();
        assertThat(result.get().getNom()).isEqualTo("Dupont");
    }

    @Test
    void shouldRejectDuplicateEmail_atDatabaseLevel() {
        repository.saveAndFlush(new User("Dupont", "Marie", "marie@mail.com", "hash1"));

        assertThatThrownBy(() ->
                repository.saveAndFlush(new User("Martin", "Paul", "marie@mail.com", "hash2"))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}
