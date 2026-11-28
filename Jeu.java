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

    public char getLettre(int ligne, int colonne) {
        return terrain.solution[ligne][colonne];
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

    public List<Position> casesLigne(Position debut, Position fin) {
        List<Position> cases = new ArrayList<>();
        if (debut == null || fin == null) {
            return cases;
        }
        int deltaLigne = fin.getLigne() - debut.getLigne();
        int deltaColonne = fin.getColonne() - debut.getColonne();
        int pasLigne = 0;
        int pasColonne = 0;
        int etapes;
        if (deltaLigne == 0 && deltaColonne == 0) {
            cases.add(new Position(debut.getLigne(), debut.getColonne()));
            return cases;
        }
        if (deltaLigne == 0) {
            pasColonne = Integer.signum(deltaColonne);
            etapes = Math.abs(deltaColonne);
        } else if (deltaColonne == 0) {
            pasLigne = Integer.signum(deltaLigne);
            etapes = Math.abs(deltaLigne);
        } else if (Math.abs(deltaLigne) == Math.abs(deltaColonne)) {
            pasLigne = Integer.signum(deltaLigne);
            pasColonne = Integer.signum(deltaColonne);
            etapes = Math.abs(deltaLigne);
        } else if (Math.abs(deltaLigne) > Math.abs(deltaColonne)) {
            pasLigne = Integer.signum(deltaLigne);
            etapes = Math.abs(deltaLigne);
        } else {
            pasColonne = Integer.signum(deltaColonne);
            etapes = Math.abs(deltaColonne);
        }
        for (int k = 0; k <= etapes; k++) {
            cases.add(new Position(debut.getLigne() + k * pasLigne, debut.getColonne() + k * pasColonne));
        }
        return cases;
    }

    public boolean estCaseMotTrouve(int ligne, int colonne) {
        for (Mot mot : motsTrouves) {
            for (int k = 0; k < mot.mot.length(); k++) {
                Position position = positionLettre(mot, k);
                if (position.getLigne() == ligne && position.getColonne() == colonne) {
                    return true;
                }
            }
        }
        return false;
    }

    public Mot chercherMot(Position debut, Position fin) {
        List<Position> selection = casesLigne(debut, fin);
        for (Mot mot : mots) {
            if (motsTrouves.contains(mot)) {
                continue;
            }
            List<Position> cibles = new ArrayList<>();
            for (int k = 0; k < mot.mot.length(); k++) {
                cibles.add(positionLettre(mot, k));
            }
            if (cibles.size() != selection.size()) {
                continue;
            }
            boolean correspond = true;
            for (Position cible : cibles) {
                boolean present = false;
                for (Position caseSelectionnee : selection) {
                    if (memePosition(cible, caseSelectionnee)) {
                        present = true;
                        break;
                    }
                }
                if (!present) {
                    correspond = false;
                    break;
                }
            }
            if (correspond) {
                return mot;
            }
        }
        return null;
    }

    public boolean validerMot(Mot mot) {
        if (mot == null || motsTrouves.contains(mot)) {
            return false;
        }
        motsTrouves.add(mot);
        score += POINTS_PAR_MOT;
        return true;
    }

    public void appliquerMalus() {
        score = Math.max(0, score - PENALITE_ERREUR);
    }

    public Mot prochainMotATrouver() {
        for (Mot mot : mots) {
            if (!motsTrouves.contains(mot)) {
                return mot;
            }
        }
        return null;
    }

    public Position donnerIndice(Mot mot) {
        if (mot == null || motsTrouves.contains(mot)) {
            return null;
        }
        indicesUtilises++;
        score = Math.max(0, score - POINTS_INDICE);
        return positionLettre(mot, 0);
    }

    private boolean memePosition(Position premiere, Position seconde) {
        return premiere.getLigne() == seconde.getLigne() && premiere.getColonne() == seconde.getColonne();
    }
}
