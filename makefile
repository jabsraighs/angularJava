.PHONY: help up up-d down build restart logs ps clean test-back test-front component service guard interceptor interface generate-auth-kit

help:
	@echo "Commandes disponibles :"
	@echo "  make up                              - Demarre tous les conteneurs (logs visibles)"
	@echo "  make up-d                            - Demarre tous les conteneurs en arriere-plan"
	@echo "  make down                            - Arrete tous les conteneurs"
	@echo "  make build                           - Reconstruit les images sans demarrer"
	@echo "  make restart                         - Redemarre tous les conteneurs"
	@echo "  make logs                             - Affiche les logs en direct"
	@echo "  make ps                              - Liste les conteneurs et leur etat"
	@echo "  make clean                           - Arrete et supprime conteneurs + volumes (perd la BDD)"
	@echo "  make generate-<nom>                  - Genere model + service + composants liste/form pour <nom>"
	@echo "                                          ex: make generate-produit, make generate-commande"
	@echo "  make component p=<path>              - Genere un composant standalone au chemin donne"
	@echo "                                          ex: make component p=shared/ui/button"
	@echo "  make service p=<path>                - Genere un service au chemin donne"
	@echo "                                          ex: make service p=core/auth/auth"
	@echo "  make guard p=<path>                  - Genere un guard au chemin donne"
	@echo "                                          ex: make guard p=core/auth/auth"
	@echo "  make interceptor p=<path>            - Genere un interceptor au chemin donne"
	@echo "                                          ex: make interceptor p=core/auth/auth"
	@echo "  make interface p=<path>              - Genere une interface au chemin donne"
	@echo "                                          ex: make interface p=core/models/user"
	@echo "  make generate-auth-kit               - Genere tout le kit UI + auth (button, input, badge,"
	@echo "                                          navbar, login, facture-list, services, guard, interceptor, interfaces)"
	@echo "  make test-back                       - Lance les tests Spring Boot (mvn test)"
	@echo "  make test-front                      - Lance les tests Angular (ng test)"

up:
	docker compose up --build

up-d:
	docker compose up --build -d

down:
	docker compose down

build:
	docker compose build

restart:
	docker compose restart

logs:
	docker compose logs -f

ps:
	docker compose ps

clean:
	docker compose down -v

# --- CRUD complet (model + service + list + form) ---

generate-%:
	docker compose --profile tools run --rm ng generate interface core/models/$*
	docker compose --profile tools run --rm ng generate service core/services/$*
	docker compose --profile tools run --rm ng generate component features/$*/$*-list --standalone
	docker compose --profile tools run --rm ng generate component features/$*/$*-form --standalone

generate-interface-%:
	docker compose --profile tools run --rm ng generate interface core/models/$*

generate-service-%:
	docker compose --profile tools run --rm ng generate service core/services/$*

generate-features-%:
	docker compose --profile tools run --rm ng generate component features/$*/$*-list --standalone

# --- Generateurs generiques (chemin libre via p=...) ---

component:
	docker compose --profile tools run --rm ng generate component $(p) --standalone

service:
	docker compose --profile tools run --rm ng generate service $(p)

guard:
	docker compose --profile tools run --rm ng generate guard $(p)

interceptor:
	docker compose --profile tools run --rm ng generate interceptor $(p)

interface:
	docker compose --profile tools run --rm ng generate interface $(p)

# --- Kit UI + auth complet en une commande ---

generate-auth-kit:
	docker compose --profile tools run --rm ng generate component shared/ui/button --standalone
	docker compose --profile tools run --rm ng generate component shared/ui/input --standalone
	docker compose --profile tools run --rm ng generate component shared/ui/badge --standalone
	docker compose --profile tools run --rm ng generate component shared/components/navbar --standalone
	docker compose --profile tools run --rm ng generate component features/auth/login --standalone
	docker compose --profile tools run --rm ng generate component features/factures/facture-list --standalone
	docker compose --profile tools run --rm ng generate service core/auth/auth
	docker compose --profile tools run --rm ng generate service core/services/facture
	docker compose --profile tools run --rm ng generate guard core/auth/auth
	docker compose --profile tools run --rm ng generate interceptor core/auth/auth
	docker compose --profile tools run --rm ng generate interface core/models/user
	docker compose --profile tools run --rm ng generate interface core/models/facture

# --- Tests ---

test-back:
	docker compose --profile tools run --rm maven mvn test

test-front:
	docker compose --profile tools run --rm ng test --watch=false