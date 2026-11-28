import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PanneauScore extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final JLabel valeur = new JLabel();
    private final JLabel progression = new JLabel();
    private final Barre barre = new Barre();

    public PanneauScore(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout(0, 8));
        setBackground(Apparence.FOND);

        valeur.setFont(Apparence.CHIFFRE);
        valeur.setForeground(Apparence.ACCENT);
        progression.setFont(Apparence.PETIT);
        progression.setForeground(Apparence.ATTENUE);

        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        ligne.setBackground(Apparence.FOND);
        ligne.add(valeur);
        JLabel unite = new JLabel("pts");
        unite.setFont(Apparence.PETIT);
        unite.setForeground(Apparence.ATTENUE);
        ligne.add(unite);
        ligne.add(Box.createHorizontalStrut(8));
        ligne.add(progression);
        add(ligne, BorderLayout.CENTER);
        add(barre, BorderLayout.SOUTH);
        actualiser();
    }

    public void actualiser() {
        valeur.setText(String.valueOf(jeu.getScore()));
        progression.setText(jeu.getNbMotsTrouves() + " / " + jeu.getMots().size());
        barre.setValeur(jeu.getProgression());
    }

    private static final class Barre extends JPanel {

        private static final long serialVersionUID = 1L;

        private int valeur;

        Barre() {
            setPreferredSize(new Dimension(180, 2));
            setOpaque(false);
        }

        void setValeur(int nouvelleValeur) {
            valeur = nouvelleValeur;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphique) {
            Graphics2D g = (Graphics2D) graphique.create();
            g.setColor(Apparence.BORDURE);
            g.fillRect(0, 0, getWidth(), getHeight());
            int largeur = (int) (getWidth() * Math.max(0, Math.min(100, valeur)) / 100.0);
            g.setColor(Apparence.ACCENT);
            g.fillRect(0, 0, largeur, getHeight());
            g.dispose();
        }
    }
}
