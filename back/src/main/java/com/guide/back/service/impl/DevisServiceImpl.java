package com.guide.back.service.impl;

import com.guide.back.domain.Devis;
import com.guide.back.dto.DevisRequestDTO;
import com.guide.back.dto.DevisResponseDTO;
import com.guide.back.exception.ResourceNotFoundException;
import com.guide.back.mapper.DevisMapper;
import com.guide.back.repository.DevisRepository;
import com.guide.back.service.DevisService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevisServiceImpl implements DevisService {

    private final DevisRepository repository;
    private final DevisMapper mapper;

    public DevisServiceImpl(DevisRepository repository, DevisMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DevisResponseDTO create(DevisRequestDTO dto) {
        String numero = genererNumero();
        Devis devis = mapper.toEntity(dto, numero);
        return mapper.toDto(repository.save(devis));
    }

    @Override
    public List<DevisResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public DevisResponseDTO findById(Long id) {
        Devis devis = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis introuvable : " + id));
        return mapper.toDto(devis);
    }

    @Override
    public DevisResponseDTO update(Long id, DevisRequestDTO dto) {
        Devis devis = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis introuvable : " + id));
        mapper.updateEntity(devis, dto);
        return mapper.toDto(repository.save(devis));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Devis introuvable : " + id);
        }
        repository.deleteById(id);
    }

    private String genererNumero() {
        long count = repository.count() + 1;
        return "DEV-%d-%04d".formatted(java.time.Year.now().getValue(), count);
    }
}