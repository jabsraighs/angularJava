# Guide — Angular + Spring Boot + Docker

Application full-stack de gestion d'utilisateurs, construite comme démo d'architecture professionnelle : backend Spring Boot en couches avec tests TDD, frontend Angular en composants standalone, orchestration Docker Compose complète.

## Stack technique

| Couche | Techno |
| --- | --- |
| Frontend | Angular 21 (standalone components), TypeScript, RxJS |
| Backend | Spring Boot 3.3.4, Java 21, Spring Data JPA, Spring Security Crypto (BCrypt) |
| Base de données | PostgreSQL 16 |
| Tests backend | JUnit 5, Mockito, MockMvc, H2 (en mémoire) |
| Tests frontend | Jasmine/Karma |
| Infra | Docker, Docker Compose, nginx (reverse proxy + serveur statique) |
| Admin DB | Adminer |

## Architecture

┌─────────────┐      HTTP/JSON       ┌──────────────┐      SQL      ┌─────────────┐
│   Angular    │ ───────────────────▶ │  Spring Boot  │ ─────────────▶ │  PostgreSQL  │
│ (navigateur)  │ ◀─────────────────── │  (conteneur)   │ ◀───────────── │ (conteneur)   │
└─────────────┘   via proxy nginx    └──────────────┘               └─────────────┘

Le frontend ne parle **jamais** directement à la base de données. Toute la logique métier (validation, hachage de mot de passe, unicité d'email) vit côté backend. nginx sert les fichiers Angular compilés et fait office de reverse proxy vers l'API (`/api/*` → `backend:8081/*`), ce qui évite tout problème de CORS.

Backend en couches classiques :

controller → service → repository → base de données
                  ↕
              dto / mapper

## Prérequis

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inclut Docker Compose)
- `make` — optionnel mais recommandé. Sur Windows : `choco install make`, ou via WSL. Sans `make`, toutes les commandes ci-dessous restent utilisables directement en `docker compose ...`.

Aucun besoin d'installer Java, Node ou Angular CLI en local — tout tourne dans des conteneurs.

## Démarrage rapide

1. Cloner le projet, puis créer un fichier `.env` à la racine :

```env
POSTGRES_USER=guide
POSTGRES_PASSWORD=changeme
POSTGRES_DB=guide_db
```

1. Lancer l'ensemble :

```bash
make up
# ou sans make :
docker compose up --build
```

1. Accéder aux services :

| Service | URL |
| --- | --- |
| Frontend Angular | <http://localhost:4200> |
| API backend | <http://localhost:8081> |
| Adminer (admin base de données) | <http://localhost:8080> |
| PostgreSQL (accès direct, ex. DBeaver) | localhost:5432 |

Docker Compose respecte l'ordre de démarrage via des healthchecks : `postgres` (healthy) → `backend` (healthy) → `frontend`.

## Structure du projet

.
├── docker-compose.yml
├── Makefile
├── .env                    (à créer, non versionné)
│
├── back/                   Backend Spring Boot
│   ├── pom.xml
│   ├── Dockerfile
│   ├── README.md            (détail architecture + choix techniques backend)
│   └── src/
│       ├── main/java/com/guide/back/
│       │   ├── domain/       entités JPA
│       │   ├── dto/          objets d'échange API
│       │   ├── mapper/       conversion entité ↔ DTO
│       │   ├── repository/   accès base de données
│       │   ├── service/      logique métier
│       │   ├── controller/   endpoints REST
│       │   ├── exception/    gestion d'erreurs centralisée
│       │   └── config/       configuration Spring
│       └── test/java/com/guide/back/
│           ├── service/      tests unitaires (Mockito)
│           ├── mapper/       tests unitaires (hachage mot de passe)
│           ├── controller/   tests HTTP (MockMvc)
│           └── repository/   tests de persistance (H2)
│
└── front/website/          Frontend Angular
    ├── package.json
    ├── Dockerfile
    ├── nginx.conf
    └── src/app/
        ├── core/
        │   ├── models/       interfaces TypeScript (miroir des DTO backend)
        │   └── services/     appels HTTP
        └── features/
            └── users/
                ├── user-list/
                └── user-form/

## Commandes disponibles

### Via Makefile

| Commande | Effet |
| --- | --- |
| `make up` | Démarre tout (logs visibles) |
| `make up-d` | Démarre tout en arrière-plan |
| `make down` | Arrête tous les conteneurs |
| `make build` | Reconstruit les images sans démarrer |
| `make restart` | Redémarre tous les conteneurs |
| `make logs` | Suit les logs en direct |
| `make ps` | Liste les conteneurs et leur état |
| `make clean` | Arrête tout et supprime les volumes (⚠️ perd les données) |
| `make generate-users` | Génère model + service + composants Angular pour `users` |
| `make test-back` | Lance `mvn test` dans un conteneur jetable |
| `make test-front` | Lance `ng test` dans un conteneur jetable |

### Via npm (depuis `front/website/`)

| Commande | Effet |
| --- | --- |
| `npm run generate:model` | Génère l'interface `User` |
| `npm run generate:service` | Génère le service HTTP `UserService` |
| `npm run generate:list` | Génère le composant `user-list` |
| `npm run generate:form` | Génère le composant `user-form` |
| `npm run generate:users` | Enchaîne les 4 commandes ci-dessus |

## API — endpoints disponibles

| Méthode | URL | Description |
| --- | --- | --- |
| `POST` | `/users` | Créer un utilisateur (409 si l'email existe déjà) |
| `GET` | `/users` | Lister tous les utilisateurs |
| `GET` | `/users/{id}` | Récupérer un utilisateur |
| `PUT` | `/users/{id}` | Modifier un utilisateur |
| `DELETE` | `/users/{id}` | Supprimer un utilisateur |

Exemple de payload pour `POST /users` :

```json
{
  "nom": "Dupont",
  "prenom": "Marie",
  "email": "marie.dupont@mail.com",
  "motDePasse": "motdepasse123"
}
```

Depuis le frontend, tous les appels passent par `/api/users` — nginx retire le préfixe `/api` avant de transmettre au backend.

## Tests

Trois niveaux de tests côté backend, chacun isolant une seule couche :

| Test | Couche | Ce qui est mocké | Vitesse |
| --- | --- | --- | --- |
| `*ServiceImplTest` | Logique métier | Repository + mapper (Mockito) | Très rapide |
| `*MapperTest` | Conversion + hachage | Rien (vrai `BCryptPasswordEncoder`) | Rapide |
| `*ControllerTest` | HTTP (validation, codes de statut) | Service (`@MockBean`) | Rapide (`@WebMvcTest`) |
| `*RepositoryTest` | Persistance réelle | Rien (vraie base H2 en mémoire) | Rapide (`@DataJpaTest`) |

```bash
make test-back     # backend — aucun Docker Postgres requis, H2 en mémoire
make test-front     # frontend
```

Détail complet de la stratégie de tests dans [`back/README.md`](./back/README.md).

## Choix de sécurité notables

- **Mot de passe jamais stocké ni renvoyé en clair** : haché via BCrypt côté backend (`UserMapper`), et absent par construction du DTO de réponse (`UserResponseDTO` n'a pas de champ `motDePasse`).
- **Unicité de l'email sur deux niveaux** : vérification applicative (message d'erreur clair et rapide) + contrainte `UNIQUE` en base de données (garantie réelle contre les conditions de course).
- **Dépendance de sécurité minimale** : `spring-security-crypto` seul (pas `spring-boot-starter-security`), pour ne pas activer une authentification globale qui casserait le healthcheck Docker.

## Pistes d'amélioration

- Authentification réelle (JWT) sur la base du hachage déjà en place
- Migrations de schéma versionnées (Flyway/Liquibase) à la place de `ddl-auto: update`
- Tests d'intégration bout-en-bout avec Testcontainers (vraie instance Postgres éphémère)
- Pagination sur `GET /users`
