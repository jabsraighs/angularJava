package com.guide.back.dto;

import com.guide.back.domain.StatutFacture;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FactureResponseDTO(
        Long id,
        String numero,
        Long devisId,
        String clientNom,
        BigDecimal montantHT,
        BigDecimal montantTTC,
        StatutFacture statut,
        LocalDate dateEmission,
        LocalDate dateEcheance
) {
}