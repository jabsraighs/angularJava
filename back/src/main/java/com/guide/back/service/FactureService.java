package com.guide.back.service;

import com.guide.back.dto.FactureRequestDTO;
import com.guide.back.dto.FactureResponseDTO;
import java.util.List;

public interface FactureService {
    FactureResponseDTO create(FactureRequestDTO dto);
    List<FactureResponseDTO> findAll();
    FactureResponseDTO findById(Long id);
    void delete(Long id);
}