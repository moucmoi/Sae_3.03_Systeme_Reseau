import java.io.*;
import java.net.Socket;

/**
 * Classe ControleurClient.
 * Elle gère les interactions avec un joueur spécifique, y compris les connexions, 
 * les demandes de jeu, et la gestion des parties actives. Elle communique avec un 
 * serveur pour manipuler les joueurs en attente et les parties en cours.
 * 
 * Elle implémente l'interface {@link Runnable} pour permettre son exécution dans
 * un thread séparé.
 */
public class ControleurClient implements Runnable {

    private final Serveur serveur;
    private final Socket socket;
    private String nomJoueur;
    private PrintWriter sortie;
    private BufferedReader entree;
    private ControleurClient opposant;

    /**
     * Constructeur de la classe {@code ControleurClient}.
     *
     * @param serveur Le serveur qui gère les connexions et les parties.
     * @param socket La socket représentant la connexion du client.
     */
    public ControleurClient(Serveur serveur, Socket socket) {
        this.serveur = serveur;
        this.socket = socket;
    }

    /**
     * Récupère le nom du joueur associé à ce contrôleur.
     *
     * @return Le nom du joueur.
     */
    public String getNomJoueur() {
        return this.nomJoueur;
    }

    /**
     * Méthode principale exécutée dans un thread pour gérer les commandes du client.
     * Elle attend et traite les commandes envoyées par le client jusqu'à la déconnexion.
     * commande traiter grâce à un switch/case.
     */
    @Override
    public void run() {
        try {
            entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sortie = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String commande = entree.readLine();
                if (commande == null) break;

                String[] mots = commande.split(" ");
                String action = mots[0];
                switch (action) {
                    case "connect":
                        connexion(mots);
                        break;
                    case "list":
                        liste();
                        break;
                    case "ask":
                        demander(mots);
                        break;
                    case "play":
                        jouer(mots);
                        break;
                    case "historique":
                        historique();
                        break;
                    case "deconnexion":
                        deconnexion();
                        return;
                    default:
                        envoyer("ERR Commande inconnue");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de communication avec le client : " + e.getMessage());
        } finally {
            finpartie();
        }
    }

    /**
     * Gère la connexion d'un joueur, enregistre son nom et l'ajoute à la liste des joueurs en attente.
     *
     * @param mot Les arguments de la commande.
     */
    private void connexion(String[] mot) {
        if (mot.length < 2) {
            envoyer("ERR Nom de joueur non fourni");
            return;
        }
        String name = mot[1];
        if (serveur.joueursEnAttente.containsKey(name)) {
            envoyer("ERR Nom de joueur déjà utilisé");
            return;
        }
        nomJoueur = name;
        serveur.joueursEnAttente.put(name, this);
        envoyer("OK Connecté en tant que " + name);
    }

    /**
     * Envoie la liste des joueurs en attente avec leurs statistiques de victoire.
     */
    private void liste() {
        StringBuilder response = new StringBuilder("Joueurs en attente :\n");
        serveur.joueursEnAttente.forEach((name, handler) -> {
            StatsJoueur stats = serveur.gestionnaireStats.getStats(name);
            response.append(String.format("%s - Victoires : %d, Taux : %.2f%%\n",
                    name, stats.getVictoires(), stats.getTauxVict()));
        });

        envoyer(response.toString());
    }

    /**
     * Gère la demande d'un joueur pour jouer contre un autre joueur.
     * Si l'adversaire accepte, la partie commence.
     *
     * @param mot Les arguments de la commande.
     */
    private void demander(String[] mot) {
        if (mot.length < 2) {
            envoyer("ERR Nom d'adversaire non fourni");
            return;
        }
        String opponent = mot[1];
        if (!serveur.joueursEnAttente.containsKey(opponent) || opponent.equals(nomJoueur)) {
            envoyer("ERR Adversaire non valide");
            return;
        }

        this.opposant = serveur.joueursEnAttente.get(opponent);
        this.opposant.opposant = this;

        envoyer("Votre demande de jouer a été envoyée à " + opponent + ". En attente d'acceptation...");

        opposant.envoyer("Vous avez reçu une demande de " + nomJoueur + " pour jouer. Tapez 'accept " + nomJoueur + "' pour accepter.");

        try {
            String response = opposant.entree.readLine();
            if (response != null && response.equals("accept " + nomJoueur)) {
                serveur.joueursEnAttente.remove(nomJoueur);
                serveur.joueursEnAttente.remove(opponent);

                GameSession session = new GameSession(nomJoueur, opponent);
                serveur.partiesActives.put(nomJoueur, session);
                serveur.partiesActives.put(opponent, session);

                envoyer("OK Partie démarrée avec " + opponent + " et c'est à vous de commencer !");
                opposant.envoyer("OK Partie démarrée avec " + nomJoueur);
            } else {
                envoyer("ERR Demande refusée ou invalide.");
            }
        } catch (IOException e) {
            envoyer("ERR Erreur de communication avec l'adversaire.");
        }
    }

    /**
     * Gère le coup joué par un joueur dans une partie active.
     * Vérifie la validité du coup et met à jour l'état du jeu.
     *
     * @param mot Les arguments de la commande.
     */
    private void jouer(String[] mot) {
        if (mot.length < 2 || !serveur.partiesActives.containsKey(nomJoueur)) {
            envoyer("ERR Aucune partie active ou commande invalide");
            return;
        }
        GameSession session = serveur.partiesActives.get(nomJoueur);
        int col;
        try {
            col = Integer.parseInt(mot[1]);
        } catch (NumberFormatException e) {
            envoyer("ERR Colonne invalide");
            return;
        }

        boolean test = session.jouerCoup(nomJoueur, col);
        if (!test) {
            envoyer("ERR Coup non valide");
            return;
        }

        opposant.envoyer("Plateau de jeu après le coup adverse :\n" + session.getEtatGrille());
        envoyer("Plateau de jeu après votre coup :\n" + session.getEtatGrille());

        if (session.verifierVictoire(nomJoueur)) {
            envoyer("Vous avez gagné !");
            opposant.envoyer("Vous avez perdu ...");
            serveur.gestionnaireStats.enregistrerVictoire(nomJoueur);
            serveur.gestionnaireStats.enregistrerDefaite(opposant.getNomJoueur());
            serveur.partiesActives.remove(nomJoueur);
            serveur.partiesActives.remove(opposant.getNomJoueur());
            serveur.joueursEnAttente.put(opposant.getNomJoueur(), opposant);
            serveur.joueursEnAttente.put(nomJoueur, this);
        } else if (session.estNul()) {
            envoyer("Match nul !");
            opposant.envoyer("Match nul !");
            serveur.gestionnaireStats.enregistrerEgalite(nomJoueur);
            serveur.gestionnaireStats.enregistrerEgalite(opposant.getNomJoueur());
            serveur.partiesActives.remove(nomJoueur);
            serveur.partiesActives.remove(opposant.getNomJoueur());
            serveur.joueursEnAttente.put(opposant.getNomJoueur(), opposant);
            serveur.joueursEnAttente.put(nomJoueur, this);
        }
    }

    /**
     * Affiche l'historique des parties du joueur.
     */
    private void historique() {
        envoyer(String.join("\n", Historique.getHisto(nomJoueur)));
    }

    /**
     * Gère la déconnexion d'un joueur.
     */
    private void deconnexion() {
        envoyer("OK Déconnecté");
        serveur.joueursEnAttente.remove(nomJoueur);
        serveur.partiesActives.remove(nomJoueur);
    }

    /**
     * Gère la fin d'une partie et ferme la connexion.
     */
    private void finpartie() {
        if (nomJoueur != null) {
            serveur.joueursEnAttente.remove(nomJoueur);
            serveur.partiesActives.remove(nomJoueur);
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Envoie un message au client connecté.
     *
     * @param message Le message à envoyer.
     */
    private void envoyer(String message) {
        sortie.println(message);
        sortie.println("");
    }
}
