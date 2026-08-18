package com.guide.back.service;

import com.guide.back.dto.DevisRequestDTO;
import com.guide.back.dto.DevisResponseDTO;
import java.util.List;

public interface DevisService {
    DevisResponseDTO create(DevisRequestDTO dto);
    List<DevisResponseDTO> findAll();
    DevisResponseDTO findById(Long id);
    DevisResponseDTO update(Long id, DevisRequestDTO dto);
    void delete(Long id);
}