package com.guide.back.mapper;

import com.guide.back.domain.Devis;
import com.guide.back.domain.Facture;
import com.guide.back.domain.StatutFacture;
import com.guide.back.dto.FactureRequestDTO;
import com.guide.back.dto.FactureResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Component
public class FactureMapper {

    private static final BigDecimal TAUX_TVA = new BigDecimal("1.20");

    public Facture toEntity(FactureRequestDTO dto, Devis devis, String numero) {
        BigDecimal montantTTC = devis.getMontantHT().multiply(TAUX_TVA).setScale(2, RoundingMode.HALF_UP);
        return new Facture(
                numero,
                devis.getId(),
                devis.getClientNom(),
                devis.getMontantHT(),
                montantTTC,
                StatutFacture.BROUILLON,
                LocalDate.now(),
                dto.dateEcheance()
        );
    }

    public FactureResponseDTO toDto(Facture facture) {
        return new FactureResponseDTO(
                facture.getId(),
                facture.getNumero(),
                facture.getDevisId(),
                facture.getClientNom(),
                facture.getMontantHT(),
                facture.getMontantTTC(),
                facture.getStatut(),
                facture.getDateEmission(),
                facture.getDateEcheance()
        );
    }
}