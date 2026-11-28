import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public final class PanneauDefinitions extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient Jeu jeu;
    private final DefaultListModel<Mot> modele = new DefaultListModel<>();
    private final JList<Mot> liste;
    private final JLabel detail;
    private transient Mot motSelectionne;
    private boolean synchronisation;

    public PanneauDefinitions(Jeu jeu) {
        this.jeu = jeu;
        setLayout(new BorderLayout(0, 14));
        setBackground(Apparence.FOND);

        remplirModele();
        liste = new JList<>(modele);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setCellRenderer(new MotRenderer());
        liste.setBackground(Apparence.SURFACE);
        liste.setBorder(BorderFactory.createEmptyBorder());
        liste.addListSelectionListener(evenement -> {
            if (evenement.getValueIsAdjusting() || synchronisation) {
                return;
            }
            motSelectionne = liste.getSelectedValue();
            afficherDetail();
        });
        JScrollPane defilement = new JScrollPane(liste);
        defilement.setBorder(BorderFactory.createLineBorder(Apparence.BORDURE));
        defilement.getViewport().setBackground(Apparence.SURFACE);
        add(defilement, BorderLayout.CENTER);

        detail = new JLabel();
        detail.setFont(Apparence.PETIT);
        detail.setForeground(Apparence.ATTENUE);
        add(detail, BorderLayout.SOUTH);
        afficherDetail();
    }

    public Mot getMotSelectionne() {
        return motSelectionne;
    }

    public void selectionnerMot(Mot mot) {
        synchronisation = true;
        motSelectionne = mot;
        liste.setSelectedValue(mot, true);
        synchronisation = false;
        afficherDetail();
    }

    public void reinitialiser() {
        synchronisation = true;
        modele.clear();
        remplirModele();
        liste.clearSelection();
        motSelectionne = null;
        synchronisation = false;
        afficherDetail();
    }

    public void actualiser() {
        liste.repaint();
        afficherDetail();
    }

    private void remplirModele() {
        for (Mot mot : jeu.getMots()) {
            modele.addElement(mot);
        }
    }

    private void afficherDetail() {
        if (motSelectionne == null) {
            detail.setText("Sélectionnez une définition.");
            return;
        }
        String sens = "horizontal".equals(motSelectionne.direction) ? "horizontal"
            : "vertical".equals(motSelectionne.direction) ? "vertical" : "oblique";
        String etat = jeu.estTrouve(motSelectionne) ? "trouvé" : "à trouver";
        detail.setText(motSelectionne.mot.length() + " lettres · " + sens + " · " + etat);
    }

    private final class MotRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index,
                boolean selectionne, boolean focus) {
            super.getListCellRendererComponent(liste, valeur, index, selectionne, focus);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
            setFont(Apparence.CORPS);
            setBackground(selectionne ? Apparence.ACCENT_LEGER : Apparence.SURFACE);
            setForeground(selectionne || jeu.estTrouve((Mot) valeur)
                ? Apparence.ACCENT : Apparence.TEXTE);
            if (valeur instanceof Mot mot) {
                String sens = "horizontal".equals(mot.direction) ? "H"
                    : "vertical".equals(mot.direction) ? "V" : "O";
                String marque = jeu.estTrouve(mot) ? "· " : "";
                setText(String.format("%02d  %s  %-24s", index + 1, sens, marque + mot.definition));
            }
            return this;
        }
    }
}
