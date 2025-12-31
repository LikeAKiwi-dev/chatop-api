# Chatop API

API REST sécurisée par JWT pour le portail Chatop.  
Cette application permet la gestion des utilisateurs, des locations et des messages.

## Prérequis

Java 17  
Maven  
MySQL  
Un IDE (IntelliJ IDEA recommandé)

## Installation du projet

Cloner le dépôt GitHub, puis construire le projet avec Maven.

Commande à exécuter :

git clone <URL_DU_REPO_GITHUB>  
cd chatop  
mvn clean install

## Installation de la base de données (MySQL)

### 1) Créer la base de données

Se connecter à MySQL puis créer la base :

CREATE DATABASE chatop_db;

### 2) Importer le schéma fourni

Le schéma de la base de données est fourni sous forme d’un fichier SQL dans le projet.

Importer le fichier SQL fourni dans la base `chatop_db` à l’aide de l’outil de votre choix (MySQL, MySQL Workbench, phpMyAdmin, etc.).

Exemple en ligne de commande :

mysql -u chatop_user -p chatop_db < fichier.sql

Remplacer `fichier.sql` par le nom réel du fichier SQL fourni dans le projet.

### 3) Configurer les variables d’environnement

L’application utilise des variables d’environnement pour la connexion à la base de données.

Variables à définir :

DB_HOST : hôte MySQL (ex : localhost)  
DB_PORT : port MySQL (ex : 3306)  
DB_NAME : nom de la base (ex : chatop_db)  
DB_USER : utilisateur MySQL  
DB_PASSWORD : mot de passe MySQL

Exemple Windows (PowerShell) :

setx DB_HOST localhost  
setx DB_PORT 3306  
setx DB_NAME chatop_db  
setx DB_USER chatop_user  
setx DB_PASSWORD chatop_password

Exemple macOS / Linux :

export DB_HOST=localhost  
export DB_PORT=3306  
export DB_NAME=chatop_db  
export DB_USER=chatop_user  
export DB_PASSWORD=chatop_password

### 4) Vérification au démarrage

L’application est configurée avec :

spring.jpa.hibernate.ddl-auto=validate

Au démarrage, Spring Boot vérifie que le schéma importé correspond aux entités JPA.
Si le fichier SQL a bien été importé, l’application démarre sans erreur.

## Configuration applicative

Le fichier utilisé est :

src/main/resources/application.properties

L’application est configurée pour :

- démarrer sur le port 3001
- utiliser MySQL via des variables d’environnement
- valider le schéma de base de données (ddl-auto=validate)
- gérer l’upload de fichiers (10 MB maximum)
- exposer Swagger à l’URL /swagger-ui.html

## Lancement de l’application

### Option 1 – Depuis IntelliJ IDEA (recommandé)

Le projet peut être lancé directement depuis IntelliJ IDEA, qui gère automatiquement Maven et les dépendances.

- Ouvrir le projet dans IntelliJ
- Lancer la classe `ChatopApplication`

Les variables d’environnement doivent être configurées dans la configuration Run/Debug d’IntelliJ.

---
Option 2 – Lancement via le Maven Wrapper

Le projet peut également être lancé sans IDE, via le Maven Wrapper inclus dans le dépôt.

```bash
./mvnw spring-boot:run
```

URL de l’API :

http://localhost:3001

## Documentation Swagger

Swagger UI :

http://localhost:3001/swagger-ui.html

Spécification OpenAPI :

http://localhost:3001/v3/api-docs

Swagger permet de consulter toutes les routes et de tester l’API directement.

## Authentification JWT

Certaines routes sont protégées par un token JWT.

Procédure :

1. Appeler POST /api/auth/register ou POST /api/auth/login
2. Récupérer le token JWT
3. Cliquer sur Authorize dans Swagger
4. Renseigner : Bearer <TOKEN_JWT>

## Fonctionnalités

- Authentification utilisateur JWT
- Gestion des locations
- Envoi de messages
- Consultation des informations utilisateur
- Upload d’images pour les locations

## Technologies utilisées

Java 17  
Spring Boot  
Spring Security JWT  
Spring Data JPA  
MySQL  
Swagger OpenAPI springdoc

## Structure du projet

controller : routes API  
service : logique métier  
repository : accès aux données  
dto : objets de transfert  
config : sécurité et configuration globale

## Branche de release

La version finale du projet est disponible sur la branche :

release/v1.0

## Auteur

Projet réalisé dans le cadre de la formation OpenClassrooms – Développement Java / Spring Boot - par Jordan Chatel
