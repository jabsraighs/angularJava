.PHONY: help up up-d down build restart logs ps clean generate test-back test-front

help:
	@echo "Commandes disponibles :"
	@echo "  make up                    - Demarre tous les conteneurs (logs visibles)"
	@echo "  make up-d                  - Demarre tous les conteneurs en arriere-plan"
	@echo "  make down                  - Arrete tous les conteneurs"
	@echo "  make build                 - Reconstruit les images sans demarrer"
	@echo "  make restart               - Redemarre tous les conteneurs"
	@echo "  make logs                  - Affiche les logs en direct"
	@echo "  make ps                    - Liste les conteneurs et leur etat"
	@echo "  make clean                 - Arrete et supprime conteneurs + volumes (perd la BDD)"
	@echo "  make generate-<nom>        - Genere model + service + composants liste/form pour <nom>"
	@echo "                               ex: make generate-produit, make generate-commande"
	@echo "  make test-back             - Lance les tests Spring Boot (mvn test)"
	@echo "  make test-front            - Lance les tests Angular (ng test)"

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


generate-%:
	docker compose --profile tools run --rm ng generate interface core/models/$*
	docker compose --profile tools run --rm ng generate service core/services/$*
	docker compose --profile tools run --rm ng generate component features/$*/$*-list --standalone
	docker compose --profile tools run --rm ng generate component features/$*/$*-form --standalone

# --- Tests ---

test-back:
	docker compose --profile tools run --rm maven mvn test

test-front:
	docker compose --profile tools run --rm ng test --watch=false