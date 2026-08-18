package com.guide.back.service.impl;

import com.guide.back.domain.Devis;
import com.guide.back.domain.Facture;
import com.guide.back.domain.StatutDevis;
import com.guide.back.dto.FactureRequestDTO;
import com.guide.back.dto.FactureResponseDTO;
import com.guide.back.exception.BusinessRuleException;
import com.guide.back.exception.ResourceNotFoundException;
import com.guide.back.mapper.FactureMapper;
import com.guide.back.repository.DevisRepository;
import com.guide.back.repository.FactureRepository;
import com.guide.back.service.FactureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final DevisRepository devisRepository;
    private final FactureMapper mapper;

    public FactureServiceImpl(FactureRepository factureRepository, DevisRepository devisRepository, FactureMapper mapper) {
        this.factureRepository = factureRepository;
        this.devisRepository = devisRepository;
        this.mapper = mapper;
    }

    @Override
    public FactureResponseDTO create(FactureRequestDTO dto) {
        Devis devis = devisRepository.findById(dto.devisId())
                .orElseThrow(() -> new ResourceNotFoundException("Devis introuvable : " + dto.devisId()));

        if (devis.getStatut() != StatutDevis.ACCEPTE) {
            throw new BusinessRuleException("Seul un devis accepté peut être converti en facture.");
        }
        if (factureRepository.existsByDevisId(devis.getId())) {
            throw new BusinessRuleException("Ce devis a déjà été converti en facture.");
        }

        String numero = genererNumero();
        Facture facture = mapper.toEntity(dto, devis, numero);
        return mapper.toDto(factureRepository.save(facture));
    }

    @Override
    public List<FactureResponseDTO> findAll() {
        return factureRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public FactureResponseDTO findById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable : " + id));
        return mapper.toDto(facture);
    }

    @Override
    public void delete(Long id) {
        if (!factureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Facture introuvable : " + id);
        }
        factureRepository.deleteById(id);
    }

    private String genererNumero() {
        long count = factureRepository.count() + 1;
        return "FAC-%d-%04d".formatted(java.time.Year.now().getValue(), count);
    }
}