package com.guide.back.service;

import com.guide.back.dto.UserRequestDTO;
import com.guide.back.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO dto);

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Long id);

    UserResponseDTO update(Long id, UserRequestDTO dto);

    void delete(Long id);
}
