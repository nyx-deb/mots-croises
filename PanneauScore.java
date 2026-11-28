import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public final class PanneauScore extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final JLabel valeurScore = new JLabel();
    private final JLabel valeurProgression = new JLabel();
    private final JLabel valeurIndices = new JLabel();
    private final JProgressBar barreProgression = new JProgressBar(0, 100);

    public PanneauScore(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Score et progression"));

        JPanel informations = new JPanel(new GridLayout(0, 2, 6, 6));
        informations.add(new JLabel("Score :"));
        informations.add(valeurScore);
        informations.add(new JLabel("Mots trouvés :"));
        informations.add(valeurProgression);
        informations.add(new JLabel("Indices utilisés :"));
        informations.add(valeurIndices);
        add(informations, BorderLayout.NORTH);

        barreProgression.setStringPainted(true);
        add(barreProgression, BorderLayout.CENTER);
        actualiser();
    }

    public void actualiser() {
        valeurScore.setText(String.valueOf(jeu.getScore()));
        valeurProgression.setText(jeu.getNbMotsTrouves() + " / " + jeu.getMots().size());
        valeurIndices.setText(String.valueOf(jeu.getIndicesUtilises()));
        barreProgression.setValue(jeu.getProgression());
    }
}
