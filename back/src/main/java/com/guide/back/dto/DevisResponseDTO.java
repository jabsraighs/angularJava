package com.guide.back.dto;

import com.guide.back.domain.StatutDevis;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DevisResponseDTO(
        Long id,
        String numero,
        String clientNom,
        BigDecimal montantHT,
        StatutDevis statut,
        LocalDate dateCreation,
        LocalDate dateValidite
) {
}