public class StatsJoueur {
    private final String nomJoueur;
    private int victoires;
    private int defaites;
    private int egalites;

    public StatsJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public synchronized void incrVictoires() {
        victoires++;
    }

    public synchronized void incrDefaites() {
        defaites++;
    }

    public synchronized void incrEgalites() {
        egalites++;
    }

    public int getVictoires() {
        return victoires;
    }

    public double getTauxVict() {
        int total = victoires + defaites + egalites;
        return total == 0 ? 0 : (victoires / (double) total) * 100;
    }
}