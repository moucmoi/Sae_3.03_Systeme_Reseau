# Sae_3.03_Systeme_Reseau

## Description du projet

Ce projet consiste en une application client-serveur permettant à des utilisateurs de se connecter à un serveur, de défier d'autres joueurs et de participer à des parties où l'objectif est d'aligner quatre pions pour gagner. Le projet inclut plusieurs fonctionnalités, comme la gestion des connexions clients, les interactions entre joueurs, et des commandes pour contrôler le déroulement des parties.

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :

- **Git** : pour cloner le dépôt.
- **Java Development Kit (JDK)** : pour compiler et exécuter les fichiers Java.

## Installation

1. **Cloner le dépôt Git** :
   ```bash
   git clone https://github.com/moucmoi/Sae_3.03_Systeme_Reseau.git
   ```

2. **Accéder au répertoire du projet** :
   ```bash
   cd Sae_3.03_Systeme_Reseau
   ```

3. **Compiler les fichiers Java** :
   ```bash
   javac -d bin src/*.java
   ```

## Lancement du serveur et des clients

1. **Démarrer le serveur** :
   ```bash
   java -cp bin Serveur [numéro_de_port]
   ```

   Remplacez `[numéro_de_port]` par le port souhaité pour le serveur.

2. **Lancer un client** :
   Dans un nouveau terminal, exécutez :
   ```bash
   java -cp bin Client localhost [numéro_de_port]
   ```

   Remplacez `[numéro_de_port]` par le même numéro que celui utilisé pour le serveur.

## Protocole d'utilisation

| Commande                 | Description                                                                                     | Exemple                          |
|--------------------------|-------------------------------------------------------------------------------------------------|----------------------------------|
| `connect [nom_utilisateur]` | Se connecter au serveur avec le nom d'utilisateur spécifié.                                     | `connect Alice`                  |
| `list`                   | Afficher la liste des joueurs disponibles sur le serveur.                                        | `list`                           |
| `ask [nom_du_joueur]`    | Inviter un joueur à jouer une partie.                                                            | `ask Bob`                        |
| `accept`                 | Accepter une invitation à jouer. **Doit être saisie deux fois.**               | `accept`                         |
| `play [numéro_de_colonne]` | Jouer un pion dans la colonne spécifiée lors de votre tour.                                      | `play 3`                         |
| `historique`             | Afficher l'historique des parties jouées (actuellement non fonctionnel).                         | `historique`                     |
| `deconnexion`            | Se déconnecter du serveur et quitter la partie en cours. *(Actuellement, cette commande présente des problèmes de fonctionnement.)* | `deconnexion`                    |

## Fonctionnalités incluses

- **Serveur multiclients** :
  - Gère plusieurs connexions simultanées.
  - Permet aux joueurs de se connecter, de lister les joueurs disponibles et de se déconnecter.

- **Défis entre joueurs** :
  - Les joueurs peuvent inviter un autre joueur à participer à une partie en ligne.
  - Confirmation de défi et début de la partie automatisé.

- **Jeu interactif** :
  - Commandes pour jouer des coups, afficher la liste des joueurs, et consulter les statistiques.

- **Mise à jour des statistiques** :
  - Victoires, défaites et nombre de parties jouées enregistrés et affichés à la fin de chaque partie.

## Documentation

La documentation Javadoc est incluse dans le projet pour faciliter la compréhension et l'utilisation des différentes classes et méthodes. Pour la générer :

1. **Générer la Javadoc** :
   ```bash
   javadoc -d doc src/*.java
   ```

2. **Consulter la documentation** :
   Ouvrez le fichier `doc/index.html` dans votre navigateur web.

## Améliorations futures

- Finaliser l'implémentation de la commande `historique`.
- Corriger les problèmes liés à la commande `deconnexion`.

