import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public final class PanneauGrille extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color FOND_LIBRE = Color.WHITE;
    private static final Color FOND_SELECTION = new Color(255, 236, 179);
    private static final Color FOND_TROUVE = new Color(198, 239, 206);
    private static final Color BORDURE = new Color(170, 170, 170);

    private final transient Jeu jeu;
    private final JTextField[][] cases = new JTextField[Jeu.TAILLE][Jeu.TAILLE];
    private transient Mot motSelectionne;
    private transient Runnable actionVerification;
    private transient Runnable actionSelection;
    private int derniereLigne = -1;
    private int derniereColonne = -1;

    public PanneauGrille(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Grille"));

        JPanel grille = new JPanel(new GridLayout(Jeu.TAILLE, Jeu.TAILLE, 4, 4));
        for (int ligne = 0; ligne < Jeu.TAILLE; ligne++) {
            for (int colonne = 0; colonne < Jeu.TAILLE; colonne++) {
                JTextField caseGrille = new JTextField();
                caseGrille.setDocument(new LettreDocument());
                caseGrille.setHorizontalAlignment(SwingConstants.CENTER);
                caseGrille.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
                caseGrille.setBorder(BorderFactory.createLineBorder(BORDURE));
                int l = ligne;
                int c = colonne;
                caseGrille.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent evenement) {
                        char lettre = Character.toUpperCase(evenement.getKeyChar());
                        if (lettre >= 'A' && lettre <= 'Z') {
                            evenement.consume();
                            jeu.saisirLettre(l, c, lettre);
                            actualiser();
                            avancer(l, c, 1);
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent evenement) {
                        if (evenement.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                            evenement.consume();
                            jeu.effacerLettre(l, c);
                            actualiser();
                            avancer(l, c, -1);
                        } else if (evenement.getKeyCode() == KeyEvent.VK_ENTER) {
                            evenement.consume();
                            if (actionVerification != null) {
                                actionVerification.run();
                            }
                        }
                    }
                });
                caseGrille.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evenement) {
                        List<Mot> presents = jeu.motsA(l, c);
                        if (!presents.isEmpty()) {
                            selectionnerMot(motSuivant(presents, l, c));
                            derniereLigne = l;
                            derniereColonne = c;
                        }
                        if (actionSelection != null) {
                            actionSelection.run();
                        }
                        cases[l][c].requestFocusInWindow();
                    }
                });
                cases[ligne][colonne] = caseGrille;
                grille.add(caseGrille);
            }
        }
        add(grille, BorderLayout.CENTER);
        add(new JLabel("Clic sur une case pour choisir un mot, clics répétés sur un croisement pour changer. Entrée : vérifier."), BorderLayout.SOUTH);
        actualiser();
    }

    public void setActionVerification(Runnable action) {
        this.actionVerification = action;
    }

    public void setActionSelection(Runnable action) {
        this.actionSelection = action;
    }

    public Mot getMotSelectionne() {
        return motSelectionne;
    }

    public void selectionnerMot(Mot mot) {
        motSelectionne = mot;
        derniereLigne = -1;
        derniereColonne = -1;
        actualiser();
        if (mot == null) {
            return;
        }
        for (int k = 0; k < mot.mot.length(); k++) {
            Position position = jeu.positionLettre(mot, k);
            if (jeu.getSaisie(position.getLigne(), position.getColonne()) == ' ') {
                cases[position.getLigne()][position.getColonne()].requestFocusInWindow();
                return;
            }
        }
    }

    public void actualiser() {
        for (int ligne = 0; ligne < Jeu.TAILLE; ligne++) {
            for (int colonne = 0; colonne < Jeu.TAILLE; colonne++) {
                JTextField caseGrille = cases[ligne][colonne];
                char lettre = jeu.getSaisie(ligne, colonne);
                caseGrille.setText(lettre == ' ' ? "" : String.valueOf(lettre));
                boolean trouve = jeu.estCaseMotTrouve(ligne, colonne);
                boolean selectionne = motSelectionne != null && jeu.contient(motSelectionne, ligne, colonne);
                caseGrille.setBackground(trouve ? FOND_TROUVE : selectionne ? FOND_SELECTION : FOND_LIBRE);
                caseGrille.setForeground(new Color(30, 30, 30));
                setBordure(caseGrille, selectionne && !trouve);
            }
        }
    }

    private Mot motSuivant(List<Mot> presents, int ligne, int colonne) {
        if (ligne != derniereLigne || colonne != derniereColonne) {
            return presents.get(0);
        }
        for (int i = 0; i < presents.size(); i++) {
            if (presents.get(i) == motSelectionne) {
                return presents.get((i + 1) % presents.size());
            }
        }
        return presents.get(0);
    }

    private void avancer(int ligne, int colonne, int sens) {
        Position voisine = sens > 0
            ? jeu.caseSuivante(motSelectionne, ligne, colonne)
            : jeu.casePrecedente(motSelectionne, ligne, colonne);
        if (voisine == null && sens > 0) {
            int nouvelleColonne = (colonne + 1) % Jeu.TAILLE;
            int nouvelleLigne = nouvelleColonne == 0 ? (ligne + 1) % Jeu.TAILLE : ligne;
            voisine = new Position(nouvelleLigne, nouvelleColonne);
        }
        if (voisine != null) {
            cases[voisine.getLigne()][voisine.getColonne()].requestFocusInWindow();
        }
    }

    private void setBordure(JTextField caseGrille, boolean selectionne) {
        if (selectionne) {
            caseGrille.setBorder(BorderFactory.createLineBorder(new Color(40, 90, 160), 2));
        } else {
            caseGrille.setBorder(BorderFactory.createLineBorder(BORDURE));
        }
    }

    private static final class LettreDocument extends PlainDocument {

        private static final long serialVersionUID = 1L;

        @Override
        public void insertString(int offset, String texte, AttributeSet attributs) throws BadLocationException {
            if (texte == null) {
                return;
            }
            String lettres = texte.toUpperCase().replaceAll("[^A-Z]", "");
            if (!lettres.isEmpty()) {
                super.insertString(offset, lettres.substring(0, 1), attributs);
            }
        }
    }
}
