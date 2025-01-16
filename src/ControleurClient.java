import java.io.*;
import java.net.Socket;

public class ControleurClient implements Runnable {
    private final Serveur serveur;
    private final Socket socket;
    private String nomJoueur;
    private PrintWriter sortie;
    private BufferedReader entree;
    private ControleurClient opposant;

    public ControleurClient(Serveur serveur, Socket socket) {
        this.serveur = serveur;
        this.socket = socket;
    }

    public String getNomJoueur() {
        return this.nomJoueur;
    }

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

    private void liste() {
        StringBuilder response = new StringBuilder("Joueurs en attente :\n");
        serveur.joueursEnAttente.forEach((name, handler) -> {
            StatsJoueur stats = serveur.gestionnaireStats.getStats(name);
            response.append(String.format("%s - Victoires : %d, Taux : %.2f%%\n",
                    name, stats.getVictoires(), stats.getTauxVict()));
        });

        envoyer(response.toString());
    }

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
    
        // Envoi d'un message à l'adversaire pour lui demander d'accepter
        opposant.envoyer("Vous avez reçu une demande de " + nomJoueur + " pour jouer. Tapez 'accept " + nomJoueur + "' pour accepter.");
    
        // Attente de la réponse de l'adversaire
        try {
            String response = opposant.entree.readLine();  // Attente de la réponse de l'adversaire
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

        // Envoie l'état du plateau après le coup
        opposant.envoyer("Plateau de jeu après le coup adverse :\n" + session.getEtatGrille());
        envoyer("Plateau de jeu après votre coup :\n" + session.getEtatGrille());

        if (session.verifierVictoire(nomJoueur)) {
            envoyer("Vous avez gagné !");
            opposant.envoyer("Vous avez perdu ...");
            serveur.gestionnaireStats.enregistrerVictoire(nomJoueur);
            serveur.gestionnaireStats.enregistrerDefaite(opposant.getNomJoueur());
            serveur.partiesActives.remove(nomJoueur);
            serveur.partiesActives.remove(opposant.getNomJoueur());

            // Les joueurs retournent dans la liste des joueurs en attente
            serveur.joueursEnAttente.put(opposant.getNomJoueur(), opposant);
            serveur.joueursEnAttente.put(nomJoueur, this);
        } else if (session.estNul()) {
            envoyer("Match nul !");
            opposant.envoyer("Match nul !");
            serveur.gestionnaireStats.enregistrerEgalite(nomJoueur);
            serveur.gestionnaireStats.enregistrerEgalite(opposant.getNomJoueur());
            serveur.partiesActives.remove(nomJoueur);
            serveur.partiesActives.remove(opposant.getNomJoueur());

            // Les joueurs retournent dans la liste des joueurs en attente
            serveur.joueursEnAttente.put(opposant.getNomJoueur(), opposant);
            serveur.joueursEnAttente.put(nomJoueur, this);
        }
    }

    // Pas fonctionnel
    private void historique() {
        envoyer(String.join("\n", Historique.getHisto(nomJoueur)));
    }

    private void deconnexion() {
        envoyer("OK Déconnecté");
        serveur.joueursEnAttente.remove(nomJoueur);
        serveur.partiesActives.remove(nomJoueur);
    }

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

    private void envoyer(String message) {
        sortie.println(message);
        sortie.println("");
    }
}