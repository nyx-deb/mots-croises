import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PanneauScore extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final JLabel score = new JLabel();
    private final JLabel progression = new JLabel();
    private final JLabel indices = new JLabel();
    private final BarreProgression barre = new BarreProgression();

    public PanneauScore(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout(0, 16));
        setBackground(Apparence.SURFACE);

        JLabel titre = new JLabel("SCORE");
        titre.setFont(Apparence.PETIT.deriveFont(Font.BOLD));
        titre.setForeground(Apparence.OR);
        add(titre, BorderLayout.NORTH);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Apparence.SURFACE);

        score.setFont(Apparence.CHIFFRE);
        score.setForeground(Apparence.OR);
        score.setAlignmentX(LEFT_ALIGNMENT);
        contenu.add(score);

        JLabel legende = new JLabel("points");
        legende.setFont(Apparence.PETIT);
        legende.setForeground(Apparence.TEXTE_ATTENUE);
        legende.setAlignmentX(LEFT_ALIGNMENT);
        contenu.add(legende);
        contenu.add(Box.createVerticalStrut(16));

        progression.setFont(Apparence.CORPS);
        progression.setForeground(Apparence.TEXTE);
        progression.setAlignmentX(LEFT_ALIGNMENT);
        contenu.add(progression);
        contenu.add(Box.createVerticalStrut(8));
        barre.setAlignmentX(LEFT_ALIGNMENT);
        contenu.add(barre);
        contenu.add(Box.createVerticalStrut(16));

        indices.setFont(Apparence.PETIT);
        indices.setForeground(Apparence.TEXTE_ATTENUE);
        indices.setAlignmentX(LEFT_ALIGNMENT);
        contenu.add(indices);

        add(contenu, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        actualiser();
    }

    public void actualiser() {
        score.setText(String.valueOf(jeu.getScore()));
        progression.setText("Mots trouvés  " + jeu.getNbMotsTrouves() + " / " + jeu.getMots().size());
        indices.setText("Indices utilisés  " + jeu.getIndicesUtilises());
        barre.setValeur(jeu.getProgression());
    }

    private static final class BarreProgression extends JPanel {

        private static final long serialVersionUID = 1L;

        private int valeur;

        BarreProgression() {
            setPreferredSize(new Dimension(220, 10));
            setOpaque(false);
        }

        void setValeur(int nouvelleValeur) {
            valeur = nouvelleValeur;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphique) {
            Graphics2D g = (Graphics2D) graphique.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Apparence.BORDURE);
            g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 5, 5));
            int largeur = (int) (getWidth() * Math.max(0, Math.min(100, valeur)) / 100.0);
            if (largeur > 0) {
                g.setColor(Apparence.OR);
                g.fill(new RoundRectangle2D.Float(0, 0, largeur, getHeight(), 5, 5));
            }
            g.dispose();
        }
    }
}
