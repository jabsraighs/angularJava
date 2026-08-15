package com.guide.back.dto; 

public record UserResponseDTO(
        long id,
        String nom,
        String prenom,
        String email
) {
}
