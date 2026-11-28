import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public final class PanneauDefinitions extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final PanneauGrille grille;
    private final DefaultListModel<Mot> modele = new DefaultListModel<>();
    private final JList<Mot> liste;
    private final JTextArea detail;
    private boolean synchronisation;

    public PanneauDefinitions(Jeu jeu, PanneauGrille grille) {
        this.jeu = jeu;
        this.grille = grille;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Définitions"));

        remplirModele();
        liste = new JList<>(modele);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setCellRenderer(new MotRenderer());
        liste.addListSelectionListener(evenement -> {
            if (evenement.getValueIsAdjusting() || synchronisation) {
                return;
            }
            Mot mot = liste.getSelectedValue();
            grille.selectionnerMot(mot);
            afficherDetail(mot);
        });
        add(new JScrollPane(liste), BorderLayout.CENTER);

        detail = new JTextArea(3, 24);
        detail.setEditable(false);
        detail.setLineWrap(true);
        detail.setWrapStyleWord(true);
        detail.setBackground(new Color(245, 245, 245));
        add(new JScrollPane(detail), BorderLayout.SOUTH);
        add(new JLabel("Cliquez sur une définition pour sélectionner le mot."), BorderLayout.NORTH);
    }

    public void selectionnerMot(Mot mot) {
        synchronisation = true;
        liste.setSelectedValue(mot, true);
        afficherDetail(mot);
        synchronisation = false;
    }

    public void reinitialiser() {
        synchronisation = true;
        modele.clear();
        remplirModele();
        liste.clearSelection();
        synchronisation = false;
    }

    public void actualiser() {
        liste.repaint();
        afficherDetail(liste.getSelectedValue());
    }

    private void remplirModele() {
        for (Mot mot : jeu.getMots()) {
            modele.addElement(mot);
        }
    }

    private void afficherDetail(Mot mot) {
        if (mot == null) {
            detail.setText("Sélectionnez une définition pour commencer.");
            return;
        }
        String etat = jeu.estTrouve(mot) ? "Trouvé" : "À trouver";
        detail.setText(mot.mot.length() + " lettres - " + mot.direction + " - " + etat + "\n" + mot.definition);
    }

    private final class MotRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index, boolean selectionne, boolean focus) {
            Component composant = super.getListCellRendererComponent(liste, valeur, index, selectionne, focus);
            if (valeur instanceof Mot mot) {
                String sens = "horizontal".equals(mot.direction) ? "H"
                    : "vertical".equals(mot.direction) ? "V" : "O";
                setText(sens + " (" + mot.mot.length() + ") : " + mot.definition);
                if (jeu.estTrouve(mot)) {
                    setForeground(new Color(0, 120, 0));
                } else if (!selectionne) {
                    setForeground(new Color(40, 40, 40));
                }
            }
            return composant;
        }
    }
}
