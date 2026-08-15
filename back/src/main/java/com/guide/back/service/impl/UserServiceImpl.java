package com.guide.back.service.impl;

import com.guide.back.domain.User;
import com.guide.back.dto.UserRequestDTO;
import com.guide.back.dto.UserResponseDTO;
import com.guide.back.exception.EmailAlreadyExistsException;
import com.guide.back.exception.ResourceNotFoundException;
import com.guide.back.mapper.UserMapper;
import com.guide.back.repository.UserRepository;
import com.guide.back.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Un compte existe déjà avec l'email " + dto.email());
        }
        User user = mapper.toEntity(dto);
        User saved = repository.save(user);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = getOrThrow(id);
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = getOrThrow(id);
        mapper.updateEntity(user, dto);
        return mapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'id " + id);
        }
        repository.deleteById(id);
    }

    private User getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id " + id));
    }
}
