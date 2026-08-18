package com.guide.back.repository;

import com.guide.back.domain.Devis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevisRepository extends JpaRepository<Devis, Long> {
    long count();
}