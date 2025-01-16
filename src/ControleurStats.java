import java.util.concurrent.ConcurrentHashMap;

public class ControleurStats {
    private final ConcurrentHashMap<String, StatsJoueur> StatsJoueur = new ConcurrentHashMap<>();

    public StatsJoueur getStats(String nomDuJoueur) {
        return StatsJoueur.computeIfAbsent(nomDuJoueur, StatsJoueur::new);
    }

    public void enregistrerVictoire(String nomDuJoueur) {
        getStats(nomDuJoueur).incrVictoires();
    }

    public void enregistrerDefaite(String nomDuJoueur) {
        getStats(nomDuJoueur).incrDefaites();
    }

    public void enregistrerEgalite(String nomDuJoueur) {
        getStats(nomDuJoueur).incrEgalites();
    }
}