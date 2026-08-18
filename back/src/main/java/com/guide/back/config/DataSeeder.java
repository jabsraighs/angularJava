package com.guide.back.config;

import com.guide.back.domain.*;
import com.guide.back.repository.DevisRepository;
import com.guide.back.repository.FactureRepository;
import com.guide.back.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DevisRepository devisRepository;
    private final FactureRepository factureRepository;
    private final PasswordEncoder passwordEncoder;

    private static final BigDecimal TAUX_TVA = new BigDecimal("1.20");

    public DataSeeder(UserRepository userRepository,
                       DevisRepository devisRepository,
                       FactureRepository factureRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.devisRepository = devisRepository;
        this.factureRepository = factureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("Seed ignoré : des données existent déjà.");
            return;
        }

        seedUsers();
        List<Devis> devisList = seedDevis();
        seedFactures(devisList);

        System.out.println("✔ Données de démonstration insérées.");
    }

    private void seedUsers() {
        userRepository.save(new User("Bouaki", "Arthur", "arthur.bouaki@exemple.com", passwordEncoder.encode("password123")));
        userRepository.save(new User("Martin", "Sophie", "sophie.martin@exemple.com", passwordEncoder.encode("password123")));
        userRepository.save(new User("Nguyen", "Lucas", "lucas.nguyen@exemple.com", passwordEncoder.encode("password123")));
    }

    private List<Devis> seedDevis() {
        List<Devis> devisList = List.of(
                new Devis("DEV-2026-0001", "Dupont SARL", new BigDecimal("2400.00"),
                        StatutDevis.ACCEPTE, LocalDate.now().minusDays(20), LocalDate.now().plusDays(10)),
                new Devis("DEV-2026-0002", "Martin Consulting", new BigDecimal("1580.50"),
                        StatutDevis.ACCEPTE, LocalDate.now().minusDays(15), LocalDate.now().plusDays(15)),
                new Devis("DEV-2026-0003", "Lefèvre & Associés", new BigDecimal("4200.00"),
                        StatutDevis.EN_ATTENTE, LocalDate.now().minusDays(5), LocalDate.now().plusDays(25)),
                new Devis("DEV-2026-0004", "Petit Numérique", new BigDecimal("890.00"),
                        StatutDevis.REFUSE, LocalDate.now().minusDays(30), LocalDate.now().minusDays(5)),
                new Devis("DEV-2026-0005", "Studio Bernard", new BigDecimal("3150.75"),
                        StatutDevis.ACCEPTE, LocalDate.now().minusDays(8), LocalDate.now().plusDays(20)),
                new Devis("DEV-2026-0006", "Fournier Digital", new BigDecimal("670.00"),
                        StatutDevis.EN_ATTENTE, LocalDate.now().minusDays(2), LocalDate.now().plusDays(28))
        );

        return devisRepository.saveAll(devisList);
    }

    private void seedFactures(List<Devis> devisList) {
        // Seuls certains devis ACCEPTE sont déjà convertis en facture, pour montrer les deux états
        Devis devisDupont = devisList.get(0);   // ACCEPTE, converti
        Devis devisMartin = devisList.get(1);   // ACCEPTE, converti
        // devisList.get(4) (Studio Bernard) reste ACCEPTE mais PAS converti, pour tester le bouton "Convertir"

        factureRepository.save(creerFacture("FAC-2026-0001", devisDupont, StatutFacture.PAYEE, 30));
        factureRepository.save(creerFacture("FAC-2026-0002", devisMartin, StatutFacture.EN_RETARD, 15));
    }

    private Facture creerFacture(String numero, Devis devis, StatutFacture statut, int joursEcheance) {
        BigDecimal montantTTC = devis.getMontantHT().multiply(TAUX_TVA).setScale(2, RoundingMode.HALF_UP);
        return new Facture(
                numero,
                devis.getId(),
                devis.getClientNom(),
                devis.getMontantHT(),
                montantTTC,
                statut,
                LocalDate.now().minusDays(joursEcheance),
                LocalDate.now().minusDays(joursEcheance).plusDays(30)
        );
    }
}