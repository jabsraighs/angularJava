package com.guide.back.mapper;

import com.guide.back.domain.Devis;
import com.guide.back.dto.DevisRequestDTO;
import com.guide.back.dto.DevisResponseDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DevisMapper {

    public Devis toEntity(DevisRequestDTO dto, String numero) {
        return new Devis(
                numero,
                dto.clientNom(),
                dto.montantHT(),
                dto.statut(),
                LocalDate.now(),
                dto.dateValidite()
        );
    }

    public void updateEntity(Devis devis, DevisRequestDTO dto) {
        devis.setClientNom(dto.clientNom());
        devis.setMontantHT(dto.montantHT());
        devis.setStatut(dto.statut());
        devis.setDateValidite(dto.dateValidite());
    }

    public DevisResponseDTO toDto(Devis devis) {
        return new DevisResponseDTO(
                devis.getId(),
                devis.getNumero(),
                devis.getClientNom(),
                devis.getMontantHT(),
                devis.getStatut(),
                devis.getDateCreation(),
                devis.getDateValidite()
        );
    }
}