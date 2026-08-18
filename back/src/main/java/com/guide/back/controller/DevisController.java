package com.guide.back.controller;

import com.guide.back.dto.DevisRequestDTO;
import com.guide.back.dto.DevisResponseDTO;
import com.guide.back.service.DevisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/devis")
public class DevisController {

    private final DevisService service;

    public DevisController(DevisService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DevisResponseDTO> create(@Valid @RequestBody DevisRequestDTO dto) {
        DevisResponseDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/devis/" + created.id())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DevisResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevisResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DevisResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DevisRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}