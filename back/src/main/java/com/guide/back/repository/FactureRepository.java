package com.guide.back.repository;

import com.guide.back.domain.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    long count();
    boolean existsByDevisId(Long devisId);
}