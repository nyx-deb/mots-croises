import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Jeu {

    public static final int TAILLE = 5;
    public static final int POINTS_PAR_MOT = 10;
    public static final int POINTS_INDICE = 5;
    public static final int PENALITE_ERREUR = 2;

    private Terrain terrain;
    private final List<Mot> mots = new ArrayList<>();
    private final Set<Mot> motsTrouves = new HashSet<>();
    private final char[][] saisie = new char[TAILLE][TAILLE];
    private int score;
    private int indicesUtilises;

    public Jeu() {
        initialiser();
    }

    private void initialiser() {
        terrain = new Terrain();
        mots.clear();
        motsTrouves.clear();
        score = 0;
        indicesUtilises = 0;
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE; colonne++) {
                saisie[ligne][colonne] = ' ';
            }
        }

        Mot[] candidats = {
            new Mot("JEU", "horizontal", new Position(0, 1), "Activité ludique"),
            new Mot("US", "vertical", new Position(0, 3), "Abréviation courante"),
            new Mot("SOL", "oblique", new Position(3, 0), "Surface terrestre"),
            new Mot("DANSE", "horizontal", new Position(4, 0), "Suite de mouvements rythmés"),
            new Mot("PA", "vertical", new Position(0, 0), "Début de mot")
        };
        for (Mot candidat : candidats) {
            if (terrain.peutPlacer(candidat)) {
                terrain.placerMot(candidat);
                mots.add(candidat);
            }
        }
        terrain.remplirCasesVides();
    }

    public void reinitialiser() {
        initialiser();
    }

    public List<Mot> getMots() {
        return Collections.unmodifiableList(mots);
    }

    public int getScore() {
        return score;
    }

    public int getIndicesUtilises() {
        return indicesUtilises;
    }

    public int getNbMotsTrouves() {
        return motsTrouves.size();
    }

    public int getProgression() {
        if (mots.isEmpty()) {
            return 100;
        }
        return (int) Math.round(100.0 * motsTrouves.size() / mots.size());
    }

    public boolean estTermine() {
        return !mots.isEmpty() && motsTrouves.size() == mots.size();
    }

    public boolean estTrouve(Mot mot) {
        return motsTrouves.contains(mot);
    }

    public char getSaisie(int ligne, int colonne) {
        if (!estDansGrille(ligne, colonne)) {
            return ' ';
        }
        return saisie[ligne][colonne];
    }

    public char getSolution(int ligne, int colonne) {
        if (!estDansGrille(ligne, colonne)) {
            return ' ';
        }
        return terrain.solution[ligne][colonne];
    }

    public void saisirLettre(int ligne, int colonne, char lettre) {
        if (!estDansGrille(ligne, colonne) || !Character.isLetter(lettre)) {
            return;
        }
        saisie[ligne][colonne] = Character.toUpperCase(lettre);
    }

    public void effacerLettre(int ligne, int colonne) {
        if (estDansGrille(ligne, colonne)) {
            saisie[ligne][colonne] = ' ';
        }
    }

    public Position positionLettre(Mot mot, int index) {
        int ligne = mot.pos_depart.getLigne();
        int colonne = mot.pos_depart.getColonne();
        if ("horizontal".equals(mot.direction)) {
            colonne += index;
        } else if ("vertical".equals(mot.direction)) {
            ligne += index;
        } else if ("oblique".equals(mot.direction)) {
            ligne -= index;
            colonne += index;
        }
        return new Position(ligne, colonne);
    }

    public boolean contient(Mot mot, int ligne, int colonne) {
        if (mot == null) {
            return false;
        }
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = positionLettre(mot, k);
            if (position.getLigne() == ligne && position.getColonne() == colonne) {
                return true;
            }
        }
        return false;
    }

    public boolean estCaseMotTrouve(int ligne, int colonne) {
        for (Mot mot : motsTrouves) {
            if (contient(mot, ligne, colonne)) {
                return true;
            }
        }
        return false;
    }

    public Mot motA(int ligne, int colonne) {
        List<Mot> presents = motsA(ligne, colonne);
        return presents.isEmpty() ? null : presents.get(0);
    }

    public List<Mot> motsA(int ligne, int colonne) {
        List<Mot> presents = new ArrayList<>();
        for (Mot mot : mots) {
            if (contient(mot, ligne, colonne) && !motsTrouves.contains(mot)) {
                presents.add(mot);
            }
        }
        if (presents.isEmpty()) {
            for (Mot mot : mots) {
                if (contient(mot, ligne, colonne)) {
                    presents.add(mot);
                }
            }
        }
        return presents;
    }

    public Position caseSuivante(Mot mot, int ligne, int colonne) {
        return caseVoisine(mot, ligne, colonne, 1);
    }

    public Position casePrecedente(Mot mot, int ligne, int colonne) {
        return caseVoisine(mot, ligne, colonne, -1);
    }

    public boolean verifier(Mot mot) {
        if (mot == null || motsTrouves.contains(mot)) {
            return false;
        }
        if (estCorrect(mot)) {
            motsTrouves.add(mot);
            score += POINTS_PAR_MOT;
            return true;
        }
        score = Math.max(0, score - PENALITE_ERREUR);
        return false;
    }

    public int verifierGrille() {
        int ajoutes = 0;
        for (Mot mot : mots) {
            if (!motsTrouves.contains(mot) && estCorrect(mot)) {
                motsTrouves.add(mot);
                score += POINTS_PAR_MOT;
                ajoutes++;
            }
        }
        return ajoutes;
    }

    public Position donnerIndice(Mot mot) {
        if (mot == null || motsTrouves.contains(mot)) {
            return null;
        }
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = positionLettre(mot, k);
            int ligne = position.getLigne();
            int colonne = position.getColonne();
            if (saisie[ligne][colonne] != mot.mot.charAt(k)) {
                saisie[ligne][colonne] = mot.mot.charAt(k);
                indicesUtilises++;
                score = Math.max(0, score - POINTS_INDICE);
                return position;
            }
        }
        return null;
    }

    private boolean estCorrect(Mot mot) {
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = positionLettre(mot, k);
            if (saisie[position.getLigne()][position.getColonne()] != mot.mot.charAt(k)) {
                return false;
            }
        }
        return true;
    }

    private Position caseVoisine(Mot mot, int ligne, int colonne, int sens) {
        if (mot == null) {
            return null;
        }
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = positionLettre(mot, k);
            if (position.getLigne() == ligne && position.getColonne() == colonne) {
                int voisin = k + sens;
                if (voisin >= 0 && voisin < mot.mot.length()) {
                    return positionLettre(mot, voisin);
                }
            }
        }
        return null;
    }

    private boolean estDansGrille(int ligne, int colonne) {
        return ligne >= 0 && ligne < TAILLE && colonne >= 0 && colonne < TAILLE;
    }
}
