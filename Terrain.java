import java.util.Random;

public class Terrain {

    public static final int TAILLE = 10;

    private final char[][] solution = new char[TAILLE][TAILLE];

    public Terrain() {
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE; colonne++) {
                solution[ligne][colonne] = ' ';
            }
        }
    }

    public char getLettre(int ligne, int colonne) {
        return solution[ligne][colonne];
    }

    public void afficher() {
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE; colonne++) {
                System.out.print(solution[ligne][colonne]);
            }
            System.out.println();
        }
    }

    public void afficherRectangle() {
        StringBuilder separateur = new StringBuilder("+");
        for (int colonne = 0; colonne < TAILLE; colonne++) {
            separateur.append("---+");
        }
        System.out.println(separateur);
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            System.out.print("|");
            for (int colonne = 0; colonne < TAILLE; colonne++) {
                System.out.print(" " + solution[ligne][colonne] + " |");
            }
            System.out.println();
            System.out.println(separateur);
        }
    }

    public void placerMot(Mot mot) {
        if (!peutPlacer(mot)) {
            return;
        }
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = positionLettre(mot, k);
            solution[position.getLigne()][position.getColonne()] = mot.mot.charAt(k);
        }
    }

    public boolean peutPlacer(Mot mot) {
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = positionLettre(mot, k);
            if (position == null) {
                return false;
            }
            int ligne = position.getLigne();
            int colonne = position.getColonne();
            if (ligne < 0 || ligne >= TAILLE || colonne < 0 || colonne >= TAILLE) {
                return false;
            }
            char lettre = mot.mot.charAt(k);
            if (solution[ligne][colonne] != ' ' && solution[ligne][colonne] != lettre) {
                return false;
            }
        }
        return true;
    }

    public boolean placerSiPossible(Mot mot) {
        if (peutPlacer(mot)) {
            placerMot(mot);
            return true;
        }
        return false;
    }

    public void remplirAvecDictionnaire(Mot[] dictionnaire) {
        if (dictionnaire == null) {
            return;
        }
        for (Mot mot : dictionnaire) {
            if (mot != null) {
                placerSiPossible(mot);
            }
        }
    }

    public void remplirCasesVides() {
        Random random = new Random();
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE; colonne++) {
                if (solution[ligne][colonne] == ' ') {
                    solution[ligne][colonne] = (char) ('A' + random.nextInt(26));
                }
            }
        }
    }

    private Position positionLettre(Mot mot, int index) {
        int ligne = mot.pos_depart.getLigne();
        int colonne = mot.pos_depart.getColonne();
        switch (mot.direction) {
            case "horizontal" -> colonne += index;
            case "vertical" -> ligne += index;
            case "oblique" -> {
                ligne -= index;
                colonne += index;
            }
            case "oblique descendante" -> {
                ligne += index;
                colonne += index;
            }
            default -> {
                return null;
            }
        }
        return new Position(ligne, colonne);
    }
}
