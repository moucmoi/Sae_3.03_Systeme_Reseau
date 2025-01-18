/**
 * Classe StatsJoueur.
 * Elle est responsable de la gestion des statistiques d'un joueur.
 * Elle garde une trace des victoires, des défaites et des égalités.
 */
public class StatsJoueur {
    private final String nomJoueur;
    private int victoires;
    private int defaites;
    private int egalites;

    /**
     * Constructeur de la classe StatsJoueur.
     * Initialise les statistiques du joueur.
     *
     * @param nomJoueur Le nom du joueur pour lequel les statistiques sont suivies.
     */
    public StatsJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    /**
     * Incrémente le nombre de victoires du joueur.
     * Cette méthode est synchronisée pour garantir que l'accès concurrent est sécurisé.
     */
    public synchronized void incrVictoires() {
        victoires++;
    }

    /**
     * Incrémente le nombre de défaites du joueur.
     * Cette méthode est synchronisée pour garantir que l'accès concurrent est sécurisé.
     */
    public synchronized void incrDefaites() {
        defaites++;
    }

    /**
     * Incrémente le nombre d'égalités du joueur.
     * Cette méthode est synchronisée pour garantir que l'accès concurrent est sécurisé.
     */
    public synchronized void incrEgalites() {
        egalites++;
    }

    /**
     * Récupère le nombre de victoires du joueur.
     *
     * @return Le nombre de victoires.
     */
    public int getVictoires() {
        return victoires;
    }

    /**
     * Calcule et retourne le taux de victoires du joueur en pourcentage.
     * Le taux est calculé comme (victoires / total des parties jouées) * 100.
     * Si aucune partie n'a été jouée, le taux est 0.
     *
     * @return Le taux de victoires en pourcentage.
     */
    public double getTauxVict() {
        int total = victoires + defaites + egalites;
        return total == 0 ? 0 : (victoires / (double) total) * 100;
    }

    /**
     * Récupère le nom du joueur.
     *
     * @return Le nom du joueur.
     */
    public String getNomJoueur() {
        return nomJoueur;
    }

    /**
     * Récupère le nombre de défaites du joueur.
     *
     * @return Le nombre de défaites.
     */
    public int getDefaites() {
        return defaites;
    }

    /**
     * Récupère le nombre d'égalités du joueur.
     *
     * @return Le nombre d'égalités.
     */
    public int getEgalites() {
        return egalites;
    }
}
