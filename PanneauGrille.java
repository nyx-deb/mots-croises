import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.JPanel;

public final class PanneauGrille extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final int TAILLE_CASE = 62;
    public static final int ESPACEMENT = 8;
    public static final int MARGE = 14;

    private static final int RAYON = 10;
    private static final Color FONDE_TROUVE = new Color(17, 80, 71);
    private static final Color SELECTION = new Color(227, 179, 65, 75);
    private static final Color SURVOL = new Color(148, 163, 184, 40);

    private final transient Jeu jeu;
    private transient Position debut;
    private transient Position fin;
    private transient Position survol;
    private transient Position indice;
    private transient BiConsumer<Position, Position> actionValidation;

    public PanneauGrille(Jeu jeu) {
        this.jeu = jeu;
        setBackground(Apparence.SURFACE);
        int cote = 2 * MARGE + Jeu.TAILLE * TAILLE_CASE + (Jeu.TAILLE - 1) * ESPACEMENT;
        setPreferredSize(new Dimension(cote, cote));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evenement) {
                Position caseChoisie = caseSousSouris(evenement);
                if (caseChoisie == null) {
                    return;
                }
                debut = caseChoisie;
                fin = caseChoisie;
                indice = null;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent evenement) {
                if (debut == null || fin == null) {
                    return;
                }
                Position depart = debut;
                Position arrivee = fin;
                debut = null;
                fin = null;
                repaint();
                if (actionValidation != null) {
                    actionValidation.accept(depart, arrivee);
                }
            }

            @Override
            public void mouseExited(MouseEvent evenement) {
                survol = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evenement) {
                Position caseChoisie = caseSousSouris(evenement);
                if (debut != null && caseChoisie != null) {
                    fin = caseChoisie;
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent evenement) {
                Position caseChoisie = caseSousSouris(evenement);
                if (caseChoisie != survol && (caseChoisie == null || survol == null
                    || !memePosition(caseChoisie, survol))) {
                    survol = caseChoisie;
                    repaint();
                }
            }
        });
    }

    public void setActionValidation(BiConsumer<Position, Position> action) {
        this.actionValidation = action;
    }

    public List<Position> getSelection() {
        return new ArrayList<>(jeu.casesLigne(debut, fin));
    }

    public void afficherIndice(Position position) {
        indice = position;
        repaint();
    }

    public void reinitialiser() {
        debut = null;
        fin = null;
        survol = null;
        indice = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphique) {
        super.paintComponent(graphique);
        Graphics2D g = (Graphics2D) graphique.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Position> selection = getSelection();
        for (int ligne = 0; ligne < Jeu.TAILLE; ligne++) {
            for (int colonne = 0; colonne < Jeu.TAILLE; colonne++) {
                Position position = new Position(ligne, colonne);
                int x = MARGE + colonne * (TAILLE_CASE + ESPACEMENT);
                int y = MARGE + ligne * (TAILLE_CASE + ESPACEMENT);
                boolean dansSelection = contient(selection, position);
                boolean trouve = jeu.estCaseMotTrouve(ligne, colonne);
                boolean survolCase = survol != null && memePosition(survol, position);
                boolean indiceCase = indice != null && memePosition(indice, position);

                Color fond = Apparence.SURFACE_CLAIRE;
                if (trouve) {
                    fond = FONDE_TROUVE;
                }
                if (survolCase && !dansSelection) {
                    fond = SURVOL;
                }
                if (dansSelection) {
                    fond = SELECTION;
                }
                g.setColor(fond);
                g.fill(new RoundRectangle2D.Float(x, y, TAILLE_CASE, TAILLE_CASE, RAYON, RAYON));

                g.setColor(dansSelection || indiceCase ? Apparence.OR : Apparence.BORDURE);
                if (indiceCase) {
                    g.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                        10f, new float[] {6f, 4f}, 0f));
                } else {
                    g.setStroke(dansSelection
                        ? new BasicStroke(2f)
                        : new BasicStroke(1f));
                }
                g.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, TAILLE_CASE - 1, TAILLE_CASE - 1,
                    RAYON, RAYON));

                String lettre = String.valueOf(jeu.getLettre(ligne, colonne));
                g.setColor(dansSelection ? Apparence.OR : trouve ? Apparence.SUCCES : Apparence.TEXTE);
                g.setFont(Apparence.LETTRE);
                FontMetrics mesures = g.getFontMetrics();
                int xTexte = x + (TAILLE_CASE - mesures.stringWidth(lettre)) / 2;
                int yTexte = y + (TAILLE_CASE - mesures.getHeight()) / 2 + mesures.getAscent();
                g.drawString(lettre, xTexte, yTexte);
                g.setStroke(new BasicStroke());
            }
        }
        g.dispose();
    }

    private Position caseSousSouris(MouseEvent evenement) {
        int x = evenement.getX() - MARGE;
        int y = evenement.getY() - MARGE;
        int pas = TAILLE_CASE + ESPACEMENT;
        int colonne = x / pas;
        int ligne = y / pas;
        if (ligne < 0 || ligne >= Jeu.TAILLE || colonne < 0 || colonne >= Jeu.TAILLE) {
            return null;
        }
        if (x % pas >= TAILLE_CASE || y % pas >= TAILLE_CASE) {
            return null;
        }
        return new Position(ligne, colonne);
    }

    private boolean contient(List<Position> positions, Position cherchee) {
        for (Position position : positions) {
            if (memePosition(position, cherchee)) {
                return true;
            }
        }
        return false;
    }

    private boolean memePosition(Position premiere, Position seconde) {
        return premiere.getLigne() == seconde.getLigne() && premiere.getColonne() == seconde.getColonne();
    }
}
