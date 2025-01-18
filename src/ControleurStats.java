import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe ControleurStats.
 * Cette classe gère les statistiques des joueurs. Elle permet de suivre les victoires, défaites et égalités de chaque joueur.
 * Les statistiques sont stockées dans une ConcurrentHashMap, permettant un accès thread-safe.
 */
public class ControleurStats {
    
    private final ConcurrentHashMap<String, StatsJoueur> StatsJoueur = new ConcurrentHashMap<>();

    /**
     * Récupère les statistiques d'un joueur. Si le joueur n'a pas encore de statistiques, elles sont créées.
     *
     * @param nomDuJoueur Le nom du joueur pour lequel on souhaite obtenir les statistiques.
     * @return L'objet {@link StatsJoueur} représentant les statistiques du joueur.
     */
    public StatsJoueur getStats(String nomDuJoueur) {
        if (!StatsJoueur.containsKey(nomDuJoueur)) {
            StatsJoueur.put(nomDuJoueur, new StatsJoueur(nomDuJoueur));
        }
        return StatsJoueur.get(nomDuJoueur);
    }

    /**
     * Enregistre une victoire pour un joueur.
     *
     * @param nomDuJoueur Le nom du joueur ayant gagné.
     */
    public void enregistrerVictoire(String nomDuJoueur) {
        getStats(nomDuJoueur).incrVictoires();
    }

    /**
     * Enregistre une défaite pour un joueur.
     *
     * @param nomDuJoueur Le nom du joueur ayant perdu.
     */
    public void enregistrerDefaite(String nomDuJoueur) {
        getStats(nomDuJoueur).incrDefaites();
    }

    /**
     * Enregistre une égalité pour un joueur.
     *
     * @param nomDuJoueur Le nom du joueur ayant fait égalité.
     */
    public void enregistrerEgalite(String nomDuJoueur) {
        getStats(nomDuJoueur).incrEgalites();
    }
}
