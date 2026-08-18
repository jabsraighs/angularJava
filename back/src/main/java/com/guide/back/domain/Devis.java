package com.guide.back.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "devis")
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(name = "client_nom", nullable = false)
    private String clientNom;

    @Column(name = "montant_ht", nullable = false)
    private BigDecimal montantHT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDevis statut;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "date_validite", nullable = false)
    private LocalDate dateValidite;

    protected Devis() {
    }

    public Devis(String numero, String clientNom, BigDecimal montantHT,
                 StatutDevis statut, LocalDate dateCreation, LocalDate dateValidite) {
        this.numero = numero;
        this.clientNom = clientNom;
        this.montantHT = montantHT;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.dateValidite = dateValidite;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }

    public BigDecimal getMontantHT() { return montantHT; }
    public void setMontantHT(BigDecimal montantHT) { this.montantHT = montantHT; }

    public StatutDevis getStatut() { return statut; }
    public void setStatut(StatutDevis statut) { this.statut = statut; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateValidite() { return dateValidite; }
    public void setDateValidite(LocalDate dateValidite) { this.dateValidite = dateValidite; }
}