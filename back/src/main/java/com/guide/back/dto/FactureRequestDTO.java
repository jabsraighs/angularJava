package com.guide.back.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record FactureRequestDTO(
        @NotNull Long devisId,
        @NotNull LocalDate dateEcheance
) {
}